package ru.trickyfoxy.lab7.exceptions;

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
