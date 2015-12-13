package com.example.lenovo.osc;

/**
 * Created by amidalilah on 08-Dec-15.
 */

/**
 * Throw this exception to indicate invalid quantity to be used on a shopping cart product.
 */
public class QuantityOutOfRangeException extends RuntimeException {
    private static final long serialVersionUID = 44L;

    private static final String DEFAULT_MESSAGE = "Quantity is out of range";

    public QuantityOutOfRangeException() {
        super(DEFAULT_MESSAGE);
    }

    public QuantityOutOfRangeException(String message) {
        super(message);
    }
}
