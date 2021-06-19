package ru.trickyfoxy.lab8.exceptions;

public class NoUniqueId extends RuntimeException {
    public NoUniqueId(String message) {
        super(message);
    }
}
