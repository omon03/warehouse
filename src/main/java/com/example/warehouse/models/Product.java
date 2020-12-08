package com.example.warehouse.models;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String name;

    private int totalAmount = 0;
    private String shortName;
    private String characteristics;

    protected Product() { }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public Product(String name, String shortName, String characteristics) {
        this.name = name;
        this.shortName = shortName;
        this.characteristics = characteristics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
}
