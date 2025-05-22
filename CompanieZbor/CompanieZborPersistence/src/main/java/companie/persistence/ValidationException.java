package companie.persistence;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
