package companie.model;

import java.io.Serializable;
import java.util.Objects;

public class Zbor implements Entity<Integer>, Serializable {
    private int id;
    private String destinatie;
    private String dataPlecarii;
    private String oraPlecarii;
    private String aeroport;
    private int nrLocuriDisponibile;

    public Zbor() {
        this.id = 0;
        this.destinatie = "";
        this.dataPlecarii = "";
        this.oraPlecarii = "";
        this.nrLocuriDisponibile = 0;
    }

    public Zbor(Integer id, String destinatie, String dataPlecarii, String oraPlecarii, String aeroport, int nrLocuriDisponibile) {
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

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Zbor zbor = (Zbor) o;
        return id == zbor.id && nrLocuriDisponibile == zbor.nrLocuriDisponibile && Objects.equals(destinatie, zbor.destinatie) && Objects.equals(dataPlecarii, zbor.dataPlecarii) && Objects.equals(oraPlecarii, zbor.oraPlecarii) && Objects.equals(aeroport, zbor.aeroport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
    }

    @Override
    public String toString() {
        return "Zbor{" +
                "id=" + id +
                ", destinatie='" + destinatie + '\'' +
                ", dataPlecarii='" + dataPlecarii + '\'' +
                ", oraPlecarii='" + oraPlecarii + '\'' +
                ", aeroport='" + aeroport + '\'' +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                '}';
    }
}
