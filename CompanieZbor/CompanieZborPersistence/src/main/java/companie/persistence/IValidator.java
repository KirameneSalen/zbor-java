package companie.persistence;

public interface IValidator<E> {
    void validate(E entity) throws ValidationException;
}
