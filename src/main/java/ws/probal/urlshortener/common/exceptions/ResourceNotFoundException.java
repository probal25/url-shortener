package ws.probal.urlshortener.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String key) {
        super("Url with key: " + key + " not found");
    }
}
