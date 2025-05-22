package companie.persistence;

import companie.model.Turist;

public interface ITuristRepository extends IRepository<Integer, Turist>{
    Turist findOne(String nume);
    Turist findById(int id);
}
