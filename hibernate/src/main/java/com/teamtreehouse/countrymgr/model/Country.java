package com.teamtreehouse.countrymgr.model;

import jakarta.persistence.*;

@Entity
public class Country {

    @Id
    private String code;

    @Column
    private String name;

    @Column
    private Double internetUsers;

    @Column
    private Double adultLiteracyRate;

    // Default constructor for JPA
    public Country() {}

    // Builder constructor
    public Country(CountryBuilder builder) {
        this.name = builder.name;
        this.code = builder.code;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    // ToString method
    @Override
    public String toString() {
        return "Country{" +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers='" + internetUsers + '\'' +
                ", adultLiteracyRate='" + adultLiteracyRate + '\'' +
                '}';
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

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    // Builder
    public static class CountryBuilder {
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder(String name, String code) {
            this.code = code;
            this.name = name;
        }

        public CountryBuilder withInternetUsers(Double internetUsers) {
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }
}