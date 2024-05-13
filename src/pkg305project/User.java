package pkg305project;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int PhoneNumber;
    protected String password;

    public User(int PhoneNumber, String password) {
        this.PhoneNumber = PhoneNumber;
        this.password = password;
    }

    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(int PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
