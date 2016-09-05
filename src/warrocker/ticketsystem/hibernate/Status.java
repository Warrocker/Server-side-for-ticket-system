package warrocker.ticketsystem.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Status  implements Serializable {
    private int id;
    private String caption;
    private String ucolor;
    private Status(){}
    public Status(int id, String caption, String ucolor) {
        this.id = id;
        this.caption = caption;
        this.ucolor = ucolor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUcolor() {
        return ucolor;
    }

    public void setUcolor(String ucolor) {
        this.ucolor = ucolor;
    }

}
