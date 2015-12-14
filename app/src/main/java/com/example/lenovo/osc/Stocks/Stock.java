package com.example.lenovo.osc.Stocks;

import com.example.lenovo.osc.Cart.Saleable;

import java.io.Serializable;

/**
 * Created by Lenovo on 6/12/2015.
 */
public class Stock implements Saleable, Serializable {
    private static final long serialVersionUID = -4073256626483275668L;

    private String objectID;
    private String stockID;
    private String name;
    private String category;
    private Double cost;
    private Double price;
    private int quantity;
    private String location;
    private String description;
    private String supplierName;
    private String status;

    public Stock(String objectID, String stockID, String name, String category, Double cost,
                 Double price, int quantity, String location, String description, String supplierName,
                 String status) {
        this.objectID = objectID;
        this.stockID = stockID;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
        this.description = description;
        this.supplierName = supplierName;
        this.status = status;
    }

    public Stock(String objectID, String stockID, String name, Double price, int quantity,
                 String description) {
        this.objectID = objectID;
        this.stockID = stockID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Stock)) return false;

        return (this.stockID == ((Stock) o).getStockID());
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = hash * prime + (stockID == null ? 0 : stockID.hashCode());
        hash = hash * prime + (name == null ? 0 : name.hashCode());
        hash = hash * prime + (price == null ? 0 : price.hashCode());
        hash = hash * prime + (description == null ? 0 : description.hashCode());

        return hash;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
