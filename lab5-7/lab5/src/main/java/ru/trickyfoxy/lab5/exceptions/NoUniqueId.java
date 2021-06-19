package ru.trickyfoxy.lab5.exceptions;

public class NoUniqueId extends RuntimeException {
    public NoUniqueId(String message) {
        super(message);
    }
}
