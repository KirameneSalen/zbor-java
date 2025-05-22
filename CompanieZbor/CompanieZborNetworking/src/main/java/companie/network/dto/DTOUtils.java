package companie.network.dto;

import companie.model.Turist;
import companie.model.User;
import companie.model.Zbor;

public class DTOUtils {
    public static User getFromDTO(UserDTO usdto){
        int id = usdto.getId();
        String username=usdto.getUsername();
        String pass=usdto.getPasswd();
        return new User(id, username, pass);

    }
    public static UserDTO getDTO(User user){
        int id=user.getId();
        String username=user.getUsername();
        String pass=user.getPassword();
        return new UserDTO(id, username, pass);
    }

    public static ZborDTO getDTO(Zbor zbor){
        int id = zbor.getId();
        String destinatie=zbor.getDestinatie();
        String dataPlecarii=zbor.getDataPlecarii();
        String oraPlecarii=zbor.getOraPlecarii();
        String aeroport=zbor.getAeroport();
        int nrLocuriDisponibile=zbor.getNrLocuriDisponibile();
        return new ZborDTO(id,destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
    }

    public static Zbor getFromDTO(ZborDTO zborDTO){
        int id = zborDTO.getId();
        String destinatie=zborDTO.getDestinatie();
        String dataPlecarii=zborDTO.getDataPlecarii();
        String oraPlecarii=zborDTO.getOraPlecarii();
        String aeroport=zborDTO.getAeroport();
        int nrLocuriDisponibile=zborDTO.getNrLocuriDisponibile();
        return new Zbor(id, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
    }

    public static Zbor[] getFromDTO(ZborDTO[] zborDTOs){
        Zbor[] zbors=new Zbor[zborDTOs.length];
        for(int i=0;i<zborDTOs.length;i++){
            if(zborDTOs[i]!=null){
                zbors[i]=getFromDTO(zborDTOs[i]);
            }
        }
        return zbors;
    }

    public static ZborDTO[] getDTO(Zbor[] zbors){
        ZborDTO[] zborDTOs=new ZborDTO[zbors.length];
        for(int i=0;i<zbors.length;i++){
            if(zbors[i]!=null){
                zborDTOs[i]=getDTO(zbors[i]);
            }
        }
        return zborDTOs;
    }

    public static Turist getFromDTO(TuristDTO turistDTO){
        int id = turistDTO.getId();
        String nume = turistDTO.getTuristName();
        return new Turist(id, nume);
    }

    public static TuristDTO getDTO(Turist turist){
        int id = turist.getId();
        String nume = turist.getNume();
        return new TuristDTO(id, nume);
    }

    public static Turist[] getFromDTO(TuristDTO[] turistDTOS){
        Turist[] turists =new Turist[turistDTOS.length];
        for(int i=0;i<turistDTOS.length;i++){
            if(turistDTOS[i]!=null){
                turists[i]=getFromDTO(turistDTOS[i]);
            }
        }
        return turists;
    }

    public static TuristDTO[] getDTO(Turist[] turists){
        TuristDTO[] turistDTOS=new TuristDTO[turists.length];
        for(int i=0;i<turists.length;i++){
            if(turists[i]!=null){
                turistDTOS[i]=getDTO(turists[i]);
            }
        }
        return turistDTOS;
    }
}
