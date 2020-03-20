package com.monolit.mobilerealty.RealtorObjects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey
    @NonNull
    private String id1c;
    private String title;
    private String description;
    private String date;
    private String author;
    private String deadline;

    public Task(String title, String description, String date, String deadline, String author, String id1c) {
        this.title = title;
        this.date = date;
        this.author = author;
        this.id1c = id1c;
        this.deadline = deadline;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getId1c() {
        return id1c;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setId1c(String id1c) {
        this.id1c = id1c;
    }
}
