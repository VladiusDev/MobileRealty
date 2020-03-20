package com.monolit.mobilerealty.RealtorObjects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class Reservation {

    @PrimaryKey
    @NonNull
    private String id1c;
    private String object;
    private String client;
    private String data;
    private int status;
    private String reservation;
    private int isCRM;

    public Reservation(String object, String client, String data, int status, int isCRM, String reservation, String id1c) {
        this.object = object;
        this.client = client;
        this.data = data;
        this.status = status;
        this.reservation = reservation;
        this.id1c = id1c;
        this.isCRM = isCRM;
    }

    @NonNull
    public String getId1c() {
        return id1c;
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

    public int getStatus() {
        return status;
    }

    public String getReservation() {
        return reservation;
    }

    public int getIsCRM() {
        return isCRM;
    }

    public void setId1c(@NonNull String id1c) {
        this.id1c = id1c;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public void setIsCRM(int isCRM) {
        this.isCRM = isCRM;
    }
}
