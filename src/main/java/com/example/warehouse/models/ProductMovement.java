package com.example.warehouse.models;

import javax.persistence.*;
import java.util.Date;

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
    private Date date;
    private int count;

    protected ProductMovement() { }

    public ProductMovement(Product product, String operationType, Date date, int count) {
        this.product = product;
        this.operationType = operationType;
        this.date = date;
        this.count = count;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
