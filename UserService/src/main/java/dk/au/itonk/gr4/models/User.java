package dk.au.itonk.gr4.models;

import org.bson.Document;

import java.math.BigInteger;
import java.security.MessageDigest;


/**
 * Created by aske on 4/27/16.
 * <p>
 * Tried to make the class 'User' - impossible in intellij
 */
public class User {
    private Long id;
    private String fName;
    private String lName;
    private String address;
    private String city;
    private int zipcode;
    private String country;
    private int phone;
    private String email;
    private String password;

    public User() {

    }

    public User(Long id, String fName, String lName,
                String address, String city, int zipcode, String country, int phone, String email, String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.password = this.hash(password);
    }

    public User(Document document) {
        this.id = document.getLong("user_id");
        this.fName = document.getString("fName");
        this.lName = document.getString("lName");
        this.address = document.getString("address");
        this.city = document.getString("city");
        this.zipcode = document.getInteger("zipcode");
        this.country = document.getString("country");
        this.phone = document.getInteger("phone");
        this.email = document.getString("email");
        this.password = document.getString("password");
    }

    public String hash(String password) {
        String retval;
        retval = null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());
            retval = new BigInteger(1, m.digest()).toString(32);
        } catch (java.security.NoSuchAlgorithmException e) {
            e.getMessage();
        }
        return retval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public String getCountry() {
        return country;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        this.password = this.hash(pw);
    }

    public Document getMongoDBDocument() {
        Document d = new org.bson.Document();
        d.put("user_id", this.id);
        d.put("fName", this.fName);
        d.put("lName", this.lName);
        d.put("address", this.address);
        d.put("city", this.city);
        d.put("zipcode", this.zipcode);
        d.put("country", this.country);
        d.put("phone", this.phone);
        d.put("email", this.email);
        d.put("password", this.password);

        return d;
    }
}
