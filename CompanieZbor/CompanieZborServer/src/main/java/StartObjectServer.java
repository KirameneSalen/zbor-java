import companie.network.utils.AbstractServer;
import companie.network.utils.CompanieObjectConcurrentServer;
import companie.network.utils.ServerException;
import companie.persistence.*;
import companie.services.ICompanieServices;
import companie.server.CompanieServerImpl;
import repository.hibernate.TuristRepositoryHibernate;
import repository.hibernate.UserRepositoryHibernate;
import repository.jdbc.*;

import java.io.IOException;
import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/companieserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find companieserver.properties "+e);
            return;
        }
//        IUserRepository userRepo=new UserRepositoryJDBC(serverProps);
        IUserRepository userRepo=new UserRepositoryHibernate();
        IBiletRepository biletRepo=new BiletRepositoryJDBC(serverProps);
//        ITuristRepository turistRepo=new TuristRepositoryJDBC(serverProps);
        ITuristRepository turistRepo=new TuristRepositoryHibernate();
        ITuristBiletRepository turistBiletRepo=new TuristBiletRepositoryJDBC(serverProps);
        IZborRepository zborRepo=new ZborRepositoryJDBC(serverProps);
        ICompanieServices chatServerImpl=new CompanieServerImpl(userRepo, biletRepo, zborRepo, turistRepo, turistBiletRepo);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("companie.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new CompanieObjectConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
