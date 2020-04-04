package com.tm.common.exceptionhandling;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private List<FieldError> error = new ArrayList<>();
    private String errorId;

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public List<FieldError> getError() {
        return error;
    }

    public void setError(List<FieldError> error) {
        this.error = error;
    }

    public void addFieldError(String field, String code, String message) {
        FieldError err = new FieldError(field, code, message);
        error.add(err);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                ", errorId='" + errorId + '\'' +
                '}';
    }
}
