package com.monolit.mobilerealty.Model;

public class Task {

    private String title;
    private String description;
    private String date;
    private String author;
    private String deadline;
    private String id1C;
    private int id;

    public Task(String title, String description, String date, String deadline, String author, int id, String id1C) {
        this.title = title;
        this.date = date;
        this.author = author;
        this.id = id;
        this.id1C = id1C;
        this.deadline = deadline;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getId1C() {
        return id1C;
    }

    public int getId() {
        return id;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }
}
