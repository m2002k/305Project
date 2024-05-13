package pkg305project;

import java.util.ArrayList;

public class Association extends User{
    
    private String name;
    private String cause;
    private ArrayList<String> cities=new ArrayList<>();

    public Association(String name, String cause, int PhoneNumber, String password) {
        super(PhoneNumber, password);
        this.name = name;
        this.cause = cause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void addCity(String name) {
        this.cities.add(name);
    }
    
}
