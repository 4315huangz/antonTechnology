package org.antontech.repository.Exception;

public class UserDaoException extends RuntimeException{
    public UserDaoException() { super(); }

    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
