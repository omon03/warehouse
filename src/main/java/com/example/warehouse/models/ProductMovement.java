package com.example.warehouse.models;

import javax.persistence.*;
//import java.util.Date;
import java.sql.Date;

@Entity
@Table(name = "PRODUCT_MOVEMENT")
public class ProductMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goods_id")
    private Product product;

    private String operationType;
    private String date;
    private int changeInQuantity;

    protected ProductMovement() { }

    public ProductMovement(Product product, String operationType, String date, int changeInQuantity) {
        this.product = product;
        this.operationType = operationType;
        this.date = date;
        this.changeInQuantity = changeInQuantity;
    }

    public String getProductName() {
        return product != null ? product.getName() : "<none>";
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getChangeInQuantity() {
        return changeInQuantity;
    }

    public void setChangeInQuantity(int changeInQuantity) {
        this.changeInQuantity = changeInQuantity;
    }
}
