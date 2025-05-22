package companie.network.objectprotocol;

import companie.model.Turist;
import companie.model.User;
import companie.model.Zbor;
import companie.network.dto.DTOUtils;
import companie.network.dto.TuristDTO;
import companie.network.dto.UserDTO;
import companie.network.dto.ZborDTO;
import companie.persistence.ValidationException;
import companie.services.ICompanieObserver;
import companie.services.ICompanieServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompanieServerObjectProxy implements ICompanieServices {

    private String host;
    private int port;

    private ICompanieObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public CompanieServerObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses= new LinkedBlockingQueue<>();
    }

    @Override
    public User login(User user, ICompanieObserver client) throws ValidationException {
        initializeConnection();
        System.out.println("Connection ok");
        UserDTO udto= DTOUtils.getDTO(user);
        sendRequest(new LoginRequest(udto));
        System.out.println("Sending login request");
        Response response=readResponse();
        System.out.println("Response received");
        if (response instanceof LoggedInResponse){
            this.client=client;
            UserDTO userDTO = ((LoggedInResponse) response).getUser();
            return DTOUtils.getFromDTO(userDTO);
        }
        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new ValidationException(err.getMessage());
        }
        return null;
    }

    @Override
    public Zbor[] getZboruri(String destinatie, String dataPlecarii) throws ValidationException {
        System.out.println("getZboruri");
        sendRequest(new GetZboruriRequest(destinatie, dataPlecarii));
        System.out.println("Sending GetZboruri request");
        Response response=readResponse();
        System.out.println("Response received");
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ValidationException(err.getMessage());
        }
        GetZboruriResponse getZboruriResponse = (GetZboruriResponse)response;
        ZborDTO[] zboruriDTO = getZboruriResponse.getZboruri();
        return DTOUtils.getFromDTO(zboruriDTO);
    }

    @Override
    public void logout(User user, ICompanieObserver client) throws ValidationException {
        UserDTO udto=DTOUtils.getDTO(user);
        sendRequest(new LogoutRequest(udto));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ValidationException(err.getMessage());
        }
        closeConnection();
    }

    @Override
    public void cumparaBilet(Zbor zbor, int nrLocuri, Turist[] turistList, ICompanieObserver client) throws ValidationException {
        ZborDTO zborDTO=DTOUtils.getDTO(zbor);
        TuristDTO[] turistDTOS = DTOUtils.getDTO(turistList);
        System.out.println("cumparaBilet before request");
        sendRequest(new CumparaBiletRequest(zborDTO, turistDTOS, nrLocuri));
        System.out.println("Sending CumparaBilet request");
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ValidationException(err.getMessage());
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ValidationException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ValidationException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ValidationException {
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            synchronized(qresponses){
                qresponses.wait();
            }
            response=qresponses.take();
            System.out.println("Response received");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ValidationException {
        try {
            connection=new Socket(host,port);
            System.out.println("Connected to "+host+":"+port+": "+connection.toString());
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            System.out.println("Output to "+host+":"+port+": "+output.toString());
            input=new ObjectInputStream(connection.getInputStream());
            System.out.println("Input to "+host+":"+port+": "+input.toString());
            finished=false;
            System.out.println("Finished");
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        System.out.println("Starting reader");
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){
        if(update instanceof CumparaBiletResponse cumparaBiletResponse){
            Zbor zbor = DTOUtils.getFromDTO(cumparaBiletResponse.getZbor());
            System.out.println("Zbor: "+zbor);
            try{
                client.biletCumparat(zbor, cumparaBiletResponse.getNrLocuriCumparate());
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }
    private class ReaderThread implements Runnable{
        public void run() {
            System.out.println("ReaderThread entered");
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse)response);
                    }else{
//                        responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        synchronized (responses){
//                            responses.notify();
//                        }
                        synchronized(qresponses){
                            qresponses.notify();
                        }
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
