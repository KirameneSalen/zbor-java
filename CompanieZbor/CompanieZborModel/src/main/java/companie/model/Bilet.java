package companie.model;

import java.util.Objects;

public class Bilet implements Entity<Integer>{
    private int id;
    private Zbor zbor;

    public Bilet(Integer id, Zbor zbor) {
        this.id = id;
        this.zbor = zbor;
    }

    public Zbor getZbor() {
        return zbor;
    }

    public void setZbor(Zbor zbor) {
        this.zbor = zbor;
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
        Bilet bilet = (Bilet) o;
        return id == bilet.id && Objects.equals(zbor, bilet.zbor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zbor);
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "id=" + id +
                ", zbor=" + zbor +
                '}';
    }
}
