package neo.cookscorner.exceptions;

public class BadCredentialForbiddenException extends RuntimeException{
    public BadCredentialForbiddenException(String message) {
        super(message);
    }
}
