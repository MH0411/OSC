package com.example.lenovo.osc.Cart;

import com.example.lenovo.osc.Stocks.Stock;

/**
 * Created by Lenovo on 11/12/2015.
 */
public class CartItem {
    private Stock stock;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
