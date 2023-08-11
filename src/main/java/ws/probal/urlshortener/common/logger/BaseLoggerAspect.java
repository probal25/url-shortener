package ws.probal.urlshortener.common.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import ws.probal.urlshortener.common.annotations.SensitiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public abstract class BaseLoggerAspect {

    @Pointcut("@within(ws.probal.urlshortener.common.annotations.NoLogging)")
    public void noLogging() {
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String BLANK = " - ";

    protected String serializeResponseToJson(ProceedingJoinPoint joinPoint, Object object) throws JsonProcessingException {
        if (Objects.isNull(object) || doesReturnTypeHaveSensitiveInformation(joinPoint)) {
            return BLANK;
        }

        String response = objectMapper.writeValueAsString(object);

        return String.format("class: %s | method: %s | response: %s",
                joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), response);
    }

    protected String formatRequestParameters(JoinPoint joinPoint) throws JsonProcessingException {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String[] parameterNames = methodSignature.getParameterNames();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        String requestParameters = objectMapper.writeValueAsString(parameterNames);

        StringBuilder sb = new StringBuilder();
        int index = 0;

        for (Object param : args) {

            if (param instanceof BindingResult) {
                index++;
                continue;
            }

            if (Objects.isNull(param)) {
                if (index > 0) sb.append(" | ");
                sb.append(" - ");
            } else if (doParametersHaveSensitiveAnnotation(index, parameterAnnotations)) {
                if (index > 0) sb.append(" | ");
                sb.append("********");
                index++;
                continue;
            } else {
                if (index > 0) sb.append(" | ");
                String jsonParam = objectMapper.writeValueAsString(param);
                sb.append(jsonParam);
            }
            index++;
        }
        return String.format("\nClass: %s \nMethod: %s \nArguments Name: %s \nArguments Value: %s\n", className, methodName, requestParameters, sb);
    }


    protected boolean doesReturnTypeHaveSensitiveInformation(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.isAnnotationPresent(SensitiveData.class);
    }

    protected boolean doParametersHaveSensitiveAnnotation(int index, Annotation[][] paramAnnotations) {
        Annotation[] annotations = paramAnnotations[index];
        return Arrays
                .stream(annotations)
                .anyMatch(SensitiveData.class::isInstance);
    }
}
