package org.antontech.repository.Exception;

public class RoleDaoException extends RuntimeException {
    public RoleDaoException() { super(); }

    public RoleDaoException(String message) {
        super(message);
    }

    public RoleDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
