package ws.probal.urlshortener.common.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ws.probal.urlshortener.common.logger.TraceLoggerAspect;

@Aspect
@Component
public class ApplicationTracingAspect extends TraceLoggerAspect {


    @Pointcut("execution(* ws.probal.urlshortener.controller..*(..)))")
    public void applicationControllerAspect() {}

    @Pointcut("execution(* ws.probal.urlshortener.service..*.*(..)))")
    public void applicationServiceAspect() {}

    @Around("applicationControllerAspect() && applicationServiceAspect()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
