package ru.trickyfoxy.lab7.exceptions;

public class TimeoutConnectionException extends Exception {
    public TimeoutConnectionException(String message) {
        super(message);
    }

    public TimeoutConnectionException() {
        super("Превышено время ожидания от сервера");
    }
}
