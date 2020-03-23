package com.monolit.mobilerealty.RealtorObjects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "realtyObjects")
public class RealtyObject{

    @PrimaryKey
    @NonNull
    private String id1c;
    private String name;
    private String status;
    private Double square;
    private Double price;
    private Double amount;
    private int id;
    private int rooms;
    private int section;
    private int floor;
    private int apartment;
    private int statusId;

    public RealtyObject(String name, String status, int statusId, Double price, Double amount, Double square, int section, int floor, int rooms, int apartment, String id1c) {
        this.name = name;
        this.status = status;
        this.price = price;
        this.square = square;
        this.amount = amount;
        this.section = section;
        this.floor = floor;
        this.rooms = rooms;
        this.id1c = id1c;
        this.apartment = apartment;
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getId1c() {
        return id1c;
    }

    public Double getSquare() {
        return square;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public int getRooms() {
        return rooms;
    }

    public int getSection() {
        return section;
    }

    public int getFloor() {
        return floor;
    }

    public int getApartment() {
        return apartment;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId1c(String id1c) {
        this.id1c = id1c;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
