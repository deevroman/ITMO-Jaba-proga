package ru.trickyfoxy.lab3;

public class Fish implements Named {
    private String name;

    public Fish(String name) {
        this.name = name;
        System.out.println(this.getName() + " создана");
    }

    public void takeAwayWater() {
        System.out.println(this.getName() + " начала биться");
    }

    @Override
    public String getName() {
        return name;
    }
}
