package companie.services;

import companie.model.Zbor;
import companie.persistence.ValidationException;

public interface ICompanieObserver {
    void biletCumparat(Zbor zbor, int nrLocuriCumparate) throws ValidationException;
}
