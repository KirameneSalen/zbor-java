package companie.server;

import companie.model.*;
import companie.persistence.*;
import companie.services.ICompanieObserver;
import companie.services.ICompanieServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompanieServerImpl implements ICompanieServices {
    private IUserRepository userRepository;
    private IBiletRepository biletRepository;
    private IZborRepository zborRepository;
    private ITuristRepository turistRepository;
    private ITuristBiletRepository turistBiletRepository;

    private Map<Integer, ICompanieObserver> loggedClients;

    private static final Logger logger = LogManager.getLogger(CompanieServerImpl.class);

    public CompanieServerImpl(IUserRepository userRepository, IBiletRepository biletRepository, IZborRepository zborRepository, ITuristRepository turistRepository, ITuristBiletRepository turistBiletRepository) {
        this.userRepository = userRepository;
        this.biletRepository = biletRepository;
        this.zborRepository = zborRepository;
        this.turistRepository = turistRepository;
        this.turistBiletRepository = turistBiletRepository;
        loggedClients=new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(User user, ICompanieObserver client) throws ValidationException {
        logger.traceEntry();
        User user1 = userRepository.findByUsername(user.getUsername());
        if (user1 == null) {
            throw new ValidationException("Username not found");
        }
        else if (!user.getPassword().equals(user1.getPassword())) {
            throw new ValidationException("Wrong password");
        } else {
            logger.trace("Logged in");
            loggedClients.put(user1.getId(), client);
            logger.trace("Added client to logged clients");
        }
        logger.traceExit();
        return user1;
    }

    @Override
    public synchronized Zbor[] getZboruri(String destinatie, String dataPlecarii) throws ValidationException {
        logger.traceEntry();
        ArrayList<Zbor> zborList = new ArrayList<>();
        zborRepository.filter(destinatie, dataPlecarii).iterator().forEachRemaining(zborList::add);
        Zbor[] zboruri = new Zbor [zborList.size()];
        zboruri = zborList.toArray(zboruri);
        logger.traceExit();
        return zboruri;
    }

    @Override
    public synchronized void cumparaBilet(Zbor zbor, int nrLocuri, Turist[] turistList, ICompanieObserver client) throws ValidationException {
        if (nrLocuri > zbor.getNrLocuriDisponibile()){
            throw new ValidationException("Locuri disponibile insuficiente");
        }
        zbor.setNrLocuriDisponibile(zbor.getNrLocuriDisponibile() - nrLocuri);
        zborRepository.update(zbor);
        Bilet bilet = new Bilet(0, zbor);
        bilet = biletRepository.add(bilet);
        for (Turist turist : turistList) {
            Turist turistFound = turistRepository.findOne(turist.getNume());
            if (turistFound == null) {
                turistFound = turistRepository.add(turist);
            }
            TuristBilet turistBilet = new TuristBilet(0, turistFound, bilet);
            turistBilet = turistBiletRepository.add(turistBilet);
        }
        loggedClients.forEach((k,v)->{
            v.biletCumparat(zbor, nrLocuri);
        });
        logger.traceExit();
    }

    @Override
    public synchronized void logout(User user, ICompanieObserver client) throws ValidationException {
        logger.traceEntry();
        logger.trace("Logged out");
        logger.traceExit();
    }
}
