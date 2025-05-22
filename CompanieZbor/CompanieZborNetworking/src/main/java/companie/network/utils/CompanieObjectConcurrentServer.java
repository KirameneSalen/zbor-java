package companie.network.utils;

import companie.network.objectprotocol.CompanieClientObjectWorker;
import companie.services.ICompanieServices;

import java.net.Socket;

public class CompanieObjectConcurrentServer extends AbsConcurrentServer {
    private ICompanieServices companieServices;
    public CompanieObjectConcurrentServer(int port, ICompanieServices chatServer) {
        super(port);
        this.companieServices = chatServer;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        System.out.println("CompanieObjectConcurrentServer: Creating new CompanieClientObjectWorker");
        CompanieClientObjectWorker worker=new CompanieClientObjectWorker(companieServices, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
