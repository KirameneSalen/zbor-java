package companie.network.dto;

import java.io.Serializable;

public class TuristDTO implements Serializable {
    private int id;
    private String turistName;

    public TuristDTO(int id, String turistName) {
        this.id = id;
        this.turistName = turistName;
    }

    public String getTuristName() {
        return turistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TuristDTO{" +
                "id=" + id +
                ", turistName='" + turistName + '\'' +
                '}';
    }
}
