package com.elmohandes.smart_learnning.helpClasses;

public class User {
    String username,email,phone,fname;

    public User(){

    }

    public User(String username, String email, String phone, String fname) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.fname = fname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
