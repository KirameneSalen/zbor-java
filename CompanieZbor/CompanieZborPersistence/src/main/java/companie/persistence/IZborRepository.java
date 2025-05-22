package companie.persistence;

import companie.model.Zbor;

public interface IZborRepository extends IRepository<Integer, Zbor>{
    Zbor findOne(int id);
    Iterable<Zbor> filter(String destinatie, String dataPlecarii);
}
