package com.example.finsec_finalfinalnajud;

import java.io.Serializable;

public class Schedule implements Serializable  {
    private String name;
    private String date;
    private String description;
    private String amount;

    public Schedule() {
    }

    public Schedule(String name, String amount, String date, String description) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
