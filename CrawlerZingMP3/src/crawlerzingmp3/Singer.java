/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

/**
 *
 * @author cpu11165-local
 */
public class Singer {
    private String name;
    private String realName;
    private short dob;
    private String country;
    private String description;

    public Singer() {
    }

    public Singer(String name, String realName, short dob, String country, String description) {
        this.name = name;
        this.realName = realName;
        this.dob = dob;
        this.country = country;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public short getDob() {
        return dob;
    }

    public void setDob(short dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    
}
