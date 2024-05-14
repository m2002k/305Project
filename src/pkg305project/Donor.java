
package pkg305project;

public class Donor extends User{
    
    private String Fname;
    private String Lname;
    private String city;
    private String street= null;

    public Donor(String Fname, String Lname, String city, String PhoneNumber, String password) {
        super(PhoneNumber, password);
        this.Fname = Fname;
        this.Lname = Lname;
        this.city = city;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String Fname) {
        this.Fname = Fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String Lname) {
        this.Lname = Lname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}