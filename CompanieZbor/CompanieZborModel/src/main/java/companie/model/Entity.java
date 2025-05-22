package companie.model;

import java.util.Objects;

public interface Entity<ID> {
    void setId(ID id);
    ID getId();
}

