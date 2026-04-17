package com.smartcampus.api.model;

public class Sensor {

    private String id;
    private String type;
    private double value;

    public Sensor() {}

    public Sensor(String id, String type, double value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}