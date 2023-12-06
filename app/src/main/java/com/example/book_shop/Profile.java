package com.example.book_shop;

import java.io.Serializable;

public class Profile implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String intrests;
    private String location;
    public Profile(){
        this.name = "User";
        this.email = "user@gmail.com";
        this.phone = "Add your phone";
        this.intrests = "Add your intrests";
        this.location = "Add your location";
    }

    public Profile(String uid,String name,String email,String phone,String intrests,String location){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.intrests = intrests;
        this.location = location;
    }



    public  void setName(String name){
        this.name = name;
    }
    public  void setEmail(String email){
        this.email = email;
    }
    public  void setPhone(String phone){
        this.phone = phone;
    }
    public  void setIntrests(String intrests){
        this.intrests = intrests;
    }
    public  void setLocation(String location){
        this.location = location;
    }

    public  String getName(){
        return name;
    }
    public  String getEmail(){
        return email;
    }
    public  String getPhone(){
        return phone;
    }
    public  String getIntrests(){
        return intrests;
    }
    public  String getLocation(){
        return location;
    }
    public  String profile_inf(){
        return name + " " + email + " " + phone + " " + intrests + " " + location;
    }
}
