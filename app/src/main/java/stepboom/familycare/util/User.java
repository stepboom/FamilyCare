package stepboom.familycare.util;

/**
 * Created by Stepboom on 11/17/2017.
 */

public class User {

    private String macAddress;
    private String name;
    private String description;
    private String role;
    private String status;

    public User (String macAddress, String data){
        /* Eiei / Wtf / Parent / 0 */
        this.macAddress = macAddress;
        this.name = data.substring(0,data.indexOf('/')).trim();
        data = data.substring(data.indexOf('/')+1);
        this.description = data.substring(0,data.indexOf('/')).trim();
        data = data.substring(data.indexOf('/')+1);
        this.role = data.substring(0,data.indexOf('/')).trim();
        data = data.substring(data.indexOf('/')+1);
        this.status = data.trim();
    }

    public String getInformation(){
        return this.name + "/" + this.description + "/" + this.role + "/" + this.status;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
