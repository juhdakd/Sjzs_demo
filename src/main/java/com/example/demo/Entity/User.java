package com.example.demo.Entity;

public class User {
    private String name;
    private String gender;
    private int userId;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "User [name=" + name + ", gender=" + gender + ", userId=" + userId + "]";
    }
    
}
