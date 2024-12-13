package com.teamtreehouse.countrymgr.model;

import jakarta.persistence.*;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String internetUsers;

    @Column
    private String adultLiteracyRate;

    // Default constructor for JPA
    public Country() {}

    // ToString method
    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers='" + internetUsers + '\'' +
                ", adultLiteracyRate='" + adultLiteracyRate + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(String internetUsers) {
        this.internetUsers = internetUsers;
    }

    public String getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(String adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }
}
