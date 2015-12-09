package com.example.lenovo.osc.Order;

/**
 * Created by Lenovo on 4/12/2015.
 */
public class Order {

    private String objectId;
    private String name;
    private int quantity;
    private double amount;
    private String orderDate;
    private String receiveDate = null;
    private String deliverDate = null;

    public Order(String objectId, String name, int quantity, double amount, String orderDate, String receiveDate) {
        this.objectId = objectId;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
        this.orderDate = orderDate;
        this.receiveDate = receiveDate;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }
}
