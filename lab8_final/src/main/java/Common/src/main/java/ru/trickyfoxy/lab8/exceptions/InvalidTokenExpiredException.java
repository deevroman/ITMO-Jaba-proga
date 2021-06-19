package ru.trickyfoxy.lab8.exceptions;

public class InvalidTokenExpiredException extends Exception{
    public InvalidTokenExpiredException(String message) {
        super(message);
    }
}
