package companie.services;

import companie.model.Turist;
import companie.model.Zbor;
import companie.persistence.ValidationException;
import companie.model.User;

public interface ICompanieServices {
    User login(User user, ICompanieObserver client) throws ValidationException;
    Zbor[] getZboruri(String destinatie, String dataPlecarii) throws ValidationException;
    void logout(User user, ICompanieObserver client) throws ValidationException;
    void cumparaBilet(Zbor zbor, int nrLocuri, Turist[] turistList, ICompanieObserver client) throws ValidationException;
}
