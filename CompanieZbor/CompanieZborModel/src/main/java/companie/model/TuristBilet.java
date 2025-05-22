package companie.model;

import java.util.Objects;

public class TuristBilet implements Entity<Integer>{
    private int id;
    private Turist turist;
    private Bilet bilet;

    public TuristBilet(Integer id, Turist turist, Bilet bilet) {
        this.id = id;
        this.turist = turist;
        this.bilet = bilet;
    }

    public Turist getTurist() {
        return turist;
    }

    public void setTurist(Turist turist) {
        this.turist = turist;
    }

    public Bilet getBilet() {
        return bilet;
    }

    public void setBilet(Bilet bilet) {
        this.bilet = bilet;
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
        TuristBilet that = (TuristBilet) o;
        return id == that.id && Objects.equals(turist, that.turist) && Objects.equals(bilet, that.bilet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, turist, bilet);
    }

    @Override
    public String toString() {
        return "TuristBilet{" +
                "id=" + id +
                ", turist=" + turist +
                ", bilet=" + bilet +
                '}';
    }
}
