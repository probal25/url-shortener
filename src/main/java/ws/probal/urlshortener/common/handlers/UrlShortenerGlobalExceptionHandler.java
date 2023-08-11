package ws.probal.urlshortener.common.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ws.probal.urlshortener.common.exceptions.InvalidUrlException;
import ws.probal.urlshortener.common.exceptions.ResourceNotFoundException;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class UrlShortenerGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("Resource not found", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("exception_type", "This is a business exception");
        problemDetail.setType(URI.create("http://localhost/error"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidUrlException.class)
    ProblemDetail handleInvalidUrlException(InvalidUrlException e) {
        log.error("Url is invalid", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Url is invalid");
        problemDetail.setProperty("exception_type", "This is a validation exception");
        problemDetail.setType(URI.create("http://localhost/error"));
        return problemDetail;
    }


}
