package neo.cookscorner.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String s) {
        super(s);
    }
}