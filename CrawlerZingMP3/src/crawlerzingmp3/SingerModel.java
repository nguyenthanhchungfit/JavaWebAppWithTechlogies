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
public class SingerModel {
    private String id;
    private String name;
    private String realName;
    private String dob;
    private String country;
    private String description;

    public SingerModel() {
    }

    public SingerModel(String id, String name, String realName, String dob, String country, String description) {
        this.id = id;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SingerModel{" + "id=" + id + "\nname=" + name + "\nrealName=" + realName + "\ndob=" + dob + "\ncountry=" + country + "\ndescription=" + description + '}';
    }
    
    
    
}
