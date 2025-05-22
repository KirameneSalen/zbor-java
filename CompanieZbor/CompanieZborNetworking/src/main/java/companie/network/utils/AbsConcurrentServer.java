package companie.network.utils;

import java.net.Socket;

public abstract class AbsConcurrentServer extends AbstractServer {

    public AbsConcurrentServer(int port) {
        super(port);
        System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client) {
        System.out.println("Client " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
        Thread tw=createWorker(client);
        System.out.println("Worker started");
        tw.start();
    }

    protected abstract Thread createWorker(Socket client) ;


}
