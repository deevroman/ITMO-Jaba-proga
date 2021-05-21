package ru.trickyfoxy.lab6.exceptions;

public class NoUniqueId extends RuntimeException {
    public NoUniqueId(String message) {
        super(message);
    }
}
