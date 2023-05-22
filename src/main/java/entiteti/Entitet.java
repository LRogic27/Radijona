package entiteti;

import java.io.Serializable;

public class Entitet implements Serializable {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Entitet(long id) {
        this.id = id;
    }
}
