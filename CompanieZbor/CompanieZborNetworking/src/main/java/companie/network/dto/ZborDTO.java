package companie.network.dto;

import java.io.Serializable;

public class ZborDTO implements Serializable {
    private int id;
    private String destinatie;
    private String dataPlecarii;
    private String oraPlecarii;
    private String aeroport;
    private int nrLocuriDisponibile;

    public ZborDTO(int id, String destinatie, String dataPlecarii, String oraPlecarii, String aeroport, int nrLocuriDisponibile) {
        this.id = id;
        this.destinatie = destinatie;
        this.dataPlecarii = dataPlecarii;
        this.oraPlecarii = oraPlecarii;
        this.aeroport = aeroport;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public String getDataPlecarii() {
        return dataPlecarii;
    }

    public void setDataPlecarii(String dataPlecarii) {
        this.dataPlecarii = dataPlecarii;
    }

    public String getOraPlecarii() {
        return oraPlecarii;
    }

    public void setOraPlecarii(String oraPlecarii) {
        this.oraPlecarii = oraPlecarii;
    }

    public String getAeroport() {
        return aeroport;
    }

    public void setAeroport(String aeroport) {
        this.aeroport = aeroport;
    }

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public void setNrLocuriDisponibile(int nrLocuriDisponibile) {
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ZborDTO{" +
                "id=" + id +
                ", destinatie='" + destinatie + '\'' +
                ", dataPlecarii='" + dataPlecarii + '\'' +
                ", oraPlecarii='" + oraPlecarii + '\'' +
                ", aeroport='" + aeroport + '\'' +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                '}';
    }
}
