package com.monolit.mobilerealty.Model;

public class Reservation {
    private String object;
    private String client;
    private String data;
    private int status;
    private String reservation;
    private String id1C;
    private String id;
    private int isCRM;

    public Reservation(String object, String client, String data, int status, int isCRM, String reservation, String id, String id1C) {
        this.object = object;
        this.client = client;
        this.data = data;
        this.status = status;
        this.reservation = reservation;
        this.id = id;
        this.id1C = id1C;
        this.isCRM = isCRM;
    }

    public String getObject() {
        return object;
    }

    public String getClient() {
        return client;
    }

    public String getData() {
        return data;
    }

    public String getReservation() {
        return reservation;
    }

    public String getId1C() {
        return id1C;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public int getIsCRM() {
        return isCRM;
    }
}
