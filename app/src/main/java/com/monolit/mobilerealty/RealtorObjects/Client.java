package com.monolit.mobilerealty.Model;

public class Client {

    private String name;
    private String phone;
    private String email;
    private String address;
    private String manager;
    private String id;
    private String id1C;

    public Client(String name, String phone, String email, String address, String manager, String id, String id1C) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.manager = manager;
        this.id1C = id1C;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public String getId1C() {
        return id1C;
    }
}
