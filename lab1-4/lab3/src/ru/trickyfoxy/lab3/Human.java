package ru.trickyfoxy.lab3;

abstract public class Human implements Named {
    private final String name;

    public String getName() {
        return name;
    }


    public Human(String name) {
        this.name = name;
    }

    public void scream(String text) {
        System.out.println(this.name + " крикнула:\"" + text + "\"");
    }
}
