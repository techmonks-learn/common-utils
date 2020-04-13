package com.tm.common.constants;

public class Constants {
    public static class MDC{
        public static String X_REQUEST_PATH = "X-RequestPath";
        public static String X_IP_ADDRESS = "X-IPAddress";
        public static String X_B3_TRACE_ID = "X-B3-TraceId";
        public static String X_B3_SPAN_ID = "X-B3-SpanId";
        public static String X_B3_PARENT_SPAN_ID = "X-B3-ParentSpanId"; //Not Used?
        public static String X_SPAN_EXPORT = "X-Span-Export"; //Not Used?
        public static String EVENT_PRODUCER_SERVICE_NAME = "Produced-By";
        public static String X_ERROR_TYPE = "X-ErrorType";

    }

    public static class Authorization{
        public static String SPACE=" ";
        public static String BEARER_TOKEN="BEARER";
    }
}
