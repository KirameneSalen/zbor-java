package companie.network.dto;

import java.io.Serializable;

public class BiletDTO implements Serializable {
    private int id;
    private int zborId;

    public BiletDTO(int id, int zborId) {
        this.id = id;
        this.zborId = zborId;
    }

    public int getZborId() {
        return zborId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BiletDTO{" +
                "id=" + id +
                ", zborId=" + zborId +
                '}';
    }
}
