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

    private OperationType operationType;
    private Date date;
    private int changeInQuantity;

    protected ProductMovement() { }

    public ProductMovement(Product product, OperationType operationType, Date date, int changeInQuantity) {
        this.product = product;  // TODO
        this.operationType = operationType;
        this.product.setTotalAmount( this.product.getTotalAmount() + (
                operationType.equals(OperationType.DELIVERY_OF_GOODS) ? changeInQuantity : -changeInQuantity) );
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getChangeInQuantity() {
        return changeInQuantity;
    }

    public void setChangeInQuantity(int changeInQuantity) {
        this.changeInQuantity = changeInQuantity;
    }
}
