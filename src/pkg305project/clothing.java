package pkg305project;

import java.io.Serializable;

public class clothing implements Serializable {
    
    private int ID;
    private String type;
    private String size; 
    
    public clothing(int ID, String type, String size) {
        this.ID = ID;
        this.type = type;
        this.size = size;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    
}
