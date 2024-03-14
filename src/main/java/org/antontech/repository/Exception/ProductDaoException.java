package org.antontech.repository.Exception;

public class ProductDaoException extends RuntimeException {
    public ProductDaoException() { super(); }

    public ProductDaoException(String message) {
        super(message);
    }

    public ProductDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
