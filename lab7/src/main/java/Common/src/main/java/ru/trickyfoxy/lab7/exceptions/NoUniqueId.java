package ru.trickyfoxy.lab7.exceptions;

public class NoUniqueId extends RuntimeException {
    public NoUniqueId(String message) {
        super(message);
    }
}
