package com.example.lenovo.osc.Cart;

import com.example.lenovo.osc.Cart.Cart;

/**
 * Created by amidalilah on 08-Dec-15.
 */
public class CartHelper {
    private static Cart cart = new Cart();

    /**
     * Retrieve the shopping cart. Call this before perform any manipulation on the shopping cart.
     *
     * @return the shopping cart
     */
    public static Cart getCart() {
        if (cart == null) {
            cart = new Cart();
        }

        return cart;
    }
}