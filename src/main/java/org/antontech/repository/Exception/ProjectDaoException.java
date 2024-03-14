package org.antontech.repository.Exception;

public class ProjectDaoException extends RuntimeException {
    public ProjectDaoException() { super(); }

    public ProjectDaoException(String message) {
        super(message);
    }

    public ProjectDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
