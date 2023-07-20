package ws.probal.urlshortener.common.exceptions;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String givenUrl) {
        super(givenUrl + ": is not a valid URL.");
    }
}
