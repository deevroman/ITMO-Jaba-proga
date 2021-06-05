package ru.trickyfoxy.lab7.exceptions;

public class NotFountId extends Exception {
    public NotFountId() {
        super("Такого id нет в коллекции");
    }

    public NotFountId(String message) {
        super(message);
    }
}
