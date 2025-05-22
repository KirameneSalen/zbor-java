package companie.model;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "turist")
public class Turist implements companie.model.Entity<Integer>{
    private int id;
    String nume;

    public Turist() {
    }

    public Turist(Integer id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    @Column(name = "name")
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    @Id
    @GeneratedValue(generator="increment")
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Turist turist = (Turist) o;
        return id == turist.id && Objects.equals(nume, turist.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume);
    }

    @Override
    public String toString() {
        return "Turist{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                '}';
    }
}
