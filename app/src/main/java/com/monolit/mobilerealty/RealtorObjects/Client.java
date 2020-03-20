package com.monolit.mobilerealty.RealtorObjects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients")
public class Client {

    @PrimaryKey
    @NonNull
    private String id1c;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String manager;

    public Client(String name, String phone, String email, String address, String manager, String id1c) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.manager = manager;
        this.id1c = id1c;
    }

    @NonNull
    public String getId1c() {
        return id1c;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getManager() {
        return manager;
    }

    public void setId1c(@NonNull String id1c) {
        this.id1c = id1c;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
