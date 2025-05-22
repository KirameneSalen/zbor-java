package companie.network.dto;

import java.io.Serializable;

public class TuristBiletDTO implements Serializable {
    private int id;
    private int turistId;
    private int biletId;

    public TuristBiletDTO(int id, int turistId, int biletId) {
        this.id = id;
        this.turistId = turistId;
        this.biletId = biletId;
    }

    public int getTuristId() {
        return turistId;
    }

    public int getBiletId() {
        return biletId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TuristBiletDTO{" +
                "id=" + id +
                ", turistId=" + turistId +
                ", biletId=" + biletId +
                '}';
    }
}
