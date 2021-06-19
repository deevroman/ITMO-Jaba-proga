package ru.trickyfoxy.lab8.exceptions;

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
