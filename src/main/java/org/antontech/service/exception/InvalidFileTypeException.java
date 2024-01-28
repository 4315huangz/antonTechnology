package org.antontech.service.exception;

public class InvalidFileTypeException extends RuntimeException{
    public InvalidFileTypeException() {super();}

    public InvalidFileTypeException(String message) {super(message);}
}
