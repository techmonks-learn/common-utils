package com.tm.common.exceptionhandling;

import com.tm.common.constants.Constants;
import org.slf4j.MDC;

public class MDCUtils {
    public static void put(Exception e) {
        MDC.put(Constants.MDC.X_ERROR_TYPE, e.getClass().getSimpleName());
    }
}
