package repository.hibernate;

import companie.model.Turist;
import companie.model.User;
import companie.persistence.ITuristRepository;
import companie.persistence.ValidationException;
import org.hibernate.Session;

public class TuristRepositoryHibernate implements ITuristRepository {
    @Override
    public Turist findOne(String nume) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Turist turist where turist.nume=:nume", Turist.class).setParameter("nume", nume).uniqueResult();
        }
    }

    @Override
    public Turist findById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Turist where id=:idM ", Turist.class)
                    .setParameter("idM", id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Iterable<Turist> getAll() {
        try(Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Turist", Turist.class).getResultList();
        }
    }

    @Override
    public Turist add(Turist entity) throws ValidationException {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return entity;
    }

    @Override
    public Turist update(Turist entity) throws ValidationException {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            System.out.println("In update, am gasit turistul cu id: " + entity.getId());
            session.update(entity);
            session.flush();
        });
        return entity;
    }
}
