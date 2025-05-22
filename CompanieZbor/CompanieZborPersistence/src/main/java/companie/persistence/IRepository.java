package companie.persistence;

import companie.model.Entity;

public interface IRepository<T, E extends Entity<T>> {
    /**
     *
     * @return all entities
     */
    Iterable<E> getAll();

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the given entity is saved
     *          otherwise returns the entity (id already exists)
     * @throws ValidationException
     *          if the entity is not valid
     * @throws IllegalArgumentException
     *          if the given entity is null.
     */
    E add(E entity) throws ValidationException;

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                  otherwise return the entity - (e.g. id does not exist).
     * @throws IllegalArgumentException
     *              if the given entity is null.
     * @throws ValidationException
     *              if the entity is not valid.
     */
    E update(E entity) throws ValidationException;
}
