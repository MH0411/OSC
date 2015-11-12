package com.example.lenovo.osc.Users;

/**
 * Created by Lenovo on 30/10/2015.
 */
public class User {

    private String objectID;
    private String userID;
    private String password;
    private String userType;
    private String name;
    private String noIC;
    private String noTel;
    private String address;
    private String email;
    private String company;
    private String status;

    public User(String objectID, String userID, String name){
        this.objectID = objectID;
        this.userID = userID;
        this.name = name;
    }

    public User(String objectID, String userID, String password, String name, String noIC,
                String noTel, String address, String email, String status) {

        this.objectID = objectID;
        this.userID = userID;
        this.password = password;
        this.name = name;
        this.noIC = noIC;
        this.noTel = noTel;
        this.email = email;
        this.address = address;
        this.status = status;
    }

    public User(String objectID, String userID, String password, String name, String noIC,
                String noTel, String email, String address, String company, String status) {

        this.objectID = objectID;
        this.userID = userID;
        this.password = password;
        this.name = name;
        this.noIC = noIC;
        this.noTel = noTel;
        this.email = email;
        this.address = address;
        this.company = company;
        this.status = status;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoIC() {
        return noIC;
    }

    public void setNoIC(String noIC) {
        this.noIC = noIC;
    }

    public String getNoTel() {
        return noTel;
    }

    public void setNoTel(String noTel) {
        this.noTel = noTel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
