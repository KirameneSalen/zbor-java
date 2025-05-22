package repository.hibernate;

import companie.model.User;
import companie.persistence.IUserRepository;
import companie.persistence.ValidationException;
import org.hibernate.Session;

public class UserRepositoryHibernate implements IUserRepository {

    @Override
    public User findByUsername(String username) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from User user where user.username=:username", User.class).setParameter("username", username).uniqueResult();
        }
    }

    @Override
    public Iterable<User> getAll() {
        try(Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public User add(User entity) throws ValidationException {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return entity;
    }

    @Override
    public User update(User entity) throws ValidationException {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            System.out.println("In update, am gasit userul cu id: " + entity.getId());
            session.update(entity);
            session.flush();
        });
        return entity;
    }
}
