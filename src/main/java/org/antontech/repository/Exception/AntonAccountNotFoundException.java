package org.antontech.repository.Exception;

public class AntonAccountNotFoundException extends RuntimeException{
    public AntonAccountNotFoundException() { super(); }

    public AntonAccountNotFoundException(String message) {
        super(message);
    }

}
