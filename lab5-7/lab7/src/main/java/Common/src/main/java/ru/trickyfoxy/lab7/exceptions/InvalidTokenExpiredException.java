package ru.trickyfoxy.lab7.exceptions;

public class InvalidTokenExpiredException extends Exception{
    public InvalidTokenExpiredException(String message) {
        super(message);
    }
}
