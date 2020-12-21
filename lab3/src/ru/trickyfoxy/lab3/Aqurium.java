package ru.trickyfoxy.lab3;

import java.util.ArrayList;
import java.util.Objects;

public class Aqurium implements Overturnable {
    private ArrayList<Fish> fishes;
    private final String name = "Аквариум";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aqurium aqurium = (Aqurium) o;
        return Objects.equals(fishes, aqurium.fishes);
    }

    @Override
    public String toString() {
        return "Aqurium{" +
                "fishes=" + fishes +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(fishes, name);
    }

    @Override
    public String getName() {
        return name;
    }

    public Aqurium(ArrayList<Fish> fishes) {
        this.fishes = fishes;
        System.out.println(this.getName() + " с рыбками создан");
    }

    @Override
    public void overturn() {
        System.out.println(this.getName() + " опрокинулся");
        for (Fish fish : fishes) {
            fish.takeAwayWater();
        }
        fishes.clear();
    }

}
