package ru.trickyfoxy.lab8.exceptions;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
