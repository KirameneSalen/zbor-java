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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CompanieClientObjectWorker implements Runnable, ICompanieObserver {

    private ICompanieServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public CompanieClientObjectWorker(ICompanieServices server, Socket connection) {
        System.out.println("CompanieClientObjectWorker constructor");
        this.server = server;
        this.connection = connection;
        try{
            System.out.println("server before output");
            output=new ObjectOutputStream(connection.getOutputStream());
            System.out.println("server after output");
            output.flush();
            System.out.println("server before input");
            input=new ObjectInputStream(connection.getInputStream());
            System.out.println("server after input");
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse((Response) response);
                }
            } catch (EOFException e) {
                e.printStackTrace();
                connected=false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            UserDTO udto=logReq.getUser();
            User user= DTOUtils.getFromDTO(udto);
            try {
                User user1 = server.login(user, this);
                return new LoggedInResponse(DTOUtils.getDTO(user1));
            } catch (ValidationException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            UserDTO udto=logReq.getUser();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return new OkResponse();

            } catch (ValidationException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof CumparaBiletRequest){
            System.out.println("Cumpara Bilet request ...");
            CumparaBiletRequest cbreq=(CumparaBiletRequest)request;
            ZborDTO zdto=cbreq.getZbor();
            Zbor zbor=DTOUtils.getFromDTO(zdto);
            TuristDTO[] turistDTOS = cbreq.getTurists();
            Turist[] turists = DTOUtils.getFromDTO(turistDTOS);
            try{
                server.cumparaBilet(zbor, cbreq.getNrLocuriCumparate(), turists, this);
                return new OkResponse();
            } catch (ValidationException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetZboruriRequest){
            System.out.println("GetZboruri request ...");
            GetZboruriRequest getZboruriRequest=(GetZboruriRequest)request;
            try {
                Zbor[] zborListResponse = server.getZboruri(getZboruriRequest.getDestinatie(), getZboruriRequest.getDataPlecarii());
                for (int i = 0; i < zborListResponse.length; i++) {
                    System.out.println("GetZboruri response: "+zborListResponse[i]);
                }
                ZborDTO[] zborListResponseDTO = DTOUtils.getDTO(zborListResponse);
                return new GetZboruriResponse(zborListResponseDTO);
            } catch (ValidationException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void biletCumparat(Zbor zbor, int nrLocuriCumparate) throws ValidationException {
        ZborDTO zborDTO = DTOUtils.getDTO(zbor);
        System.out.println("Zbor updated "+zborDTO);
        try{
            sendResponse(new CumparaBiletResponse(zborDTO, nrLocuriCumparate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
