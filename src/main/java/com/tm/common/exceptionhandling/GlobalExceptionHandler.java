package com.tm.common.exceptionhandling;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String X_B3_TRACE_ID = "X-B3-TraceId";

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    Map<String, Object> unauthorizedAccess(Exception e) {
        Map<String, Object> exception = new HashMap<>();
        exception.put("status", 403);
        exception.put("error", "Authorization Error");
        exception.put("exception", "org.springframework.security.access.AccessDeniedException");
        exception.put("message", e.getMessage());
        MDCUtils.put(e);
        LOG.error(ESAPI.encoder().encodeForHTML(e.getMessage()), e);
        return exception;
    }

    /**
     * Global  exception handler
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {
            Exception.class,
            IllegalArgumentException.class,
            ValidationException.class,
//            NotFoundException.class,
//            NotImplementedException.class,
            ConstraintViolationException.class
    })
    protected ResponseEntity<Object> handleCustomException(RuntimeException ex, WebRequest request) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        return sanitizeException(ex, request);
    }

    private ResponseEntity<Object> sanitizeException(Exception ex, WebRequest request) {
        if (ex instanceof IllegalArgumentException) {
            return new ResponseEntity<>(ex.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConstraintViolationException || ex instanceof ValidationException || ex instanceof org.springframework.dao.DataIntegrityViolationException) {
            ErrorResponse errorResponse = handleExceptionMessage(ex);
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//        } else if ((ex instanceof ResourceNotFoundException)) {
//            return new ResponseEntity<>(ex.toString(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.addFieldError("Error", "", ex.toString());
            errorResponse.addFieldError("Message", "", ex.getMessage());
            errorResponse.setErrorId(MDC.get(X_B3_TRACE_ID));
            return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    private ErrorResponse handleExceptionMessage(Exception ex) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        ErrorResponse errorResponse = new ErrorResponse();
        if (ex instanceof ConstraintViolationException) {
            ((ConstraintViolationException) ex).getConstraintViolations().forEach(error -> {
                String field = StringUtils.substringAfter(error.getPropertyPath().toString(), ".");
                errorResponse.addFieldError(field, error.getMessageTemplate(), error.getMessage());
            });
        } else if (ex instanceof DataIntegrityViolationException) {
            String message = ((DataIntegrityViolationException) ex).getMostSpecificCause().getLocalizedMessage();
            errorResponse.addFieldError("", "", message);

        } else if (ex instanceof ValidationException) {
            if (((ValidationException) ex).getMessage().contains("@@@")) {
                String[] error = ((ValidationException) ex).getMessage().split("@@@");
                errorResponse.addFieldError(error[2], error[1], error[0]);
            } else {
                errorResponse.addFieldError("", "", ex.getMessage());
            }
        }
        errorResponse.setErrorId(MDC.get(X_B3_TRACE_ID));
        return errorResponse;
    }

    /**
     * Override the default implementation of the ResponseEntityExceptionHandler
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        String unsupported = "Unsupported content type: " + ex.getContentType();
        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        return new ResponseEntity<>(unsupported + supported, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        return formatExceptionMessage(HttpStatus.NOT_FOUND, ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        return formatExceptionMessage(HttpStatus.METHOD_NOT_ALLOWED, ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        return formatExceptionMessage(HttpStatus.NOT_FOUND, ex);
    }

    private ResponseEntity<Object> formatExceptionMessage(HttpStatus status, Exception ex) {
        MDCUtils.put(ex);
        LOG.error(ESAPI.encoder().encodeForHTML(ex.getMessage()), ex);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addFieldError("message", status.toString(), ex.getMessage());
        errorResponse.setErrorId(MDC.get(X_B3_TRACE_ID));
        logger.error(ESAPI.encoder().encodeForHTML(errorResponse.toString()), ex);
        return new ResponseEntity(errorResponse, status);
    }
}
