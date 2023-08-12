package ws.probal.urlshortener.common.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class TraceLoggerAspect extends BaseLoggerAspect {

    private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    private static final Logger traceLogger = LoggerFactory.getLogger("traceLogger");

    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestData = formatRequestParameters(joinPoint);
        traceLogger.trace(String.format("Invoking :: %s", requestData));

        Object response;

        try {
            response = joinPoint.proceed();
            String jsonResponse = serializeResponseToJson(joinPoint, response);
            String formattedJsonResponse = String.format("Invocation returned :: %s", jsonResponse);
            traceLogger.trace(formattedJsonResponse);
        } catch (Exception e) {
            errorLogger.error(e.getMessage(), e);
            traceLogger.trace("Exception occurred: " + e.getMessage());
            throw e;
        }

        return response;
    }
}
