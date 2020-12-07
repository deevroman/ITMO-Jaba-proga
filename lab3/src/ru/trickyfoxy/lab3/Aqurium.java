package ru.trickyfoxy.lab3;

import java.util.ArrayList;

public class Aqurium implements Overturnable {
    private ArrayList<Fish> fishes;
    private final String name = "Аквариум";

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
