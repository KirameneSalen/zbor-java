package companie.persistence;

import companie.model.User;

public interface IUserRepository extends IRepository<Integer, User> {
    User findByUsername(String username);
}
