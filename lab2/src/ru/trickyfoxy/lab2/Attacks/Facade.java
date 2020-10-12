package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.DARK, 80, 100);
    }

    @Override
    protected String describe() {
        return "uses Facade";
    }


    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (pokemon.getCondition() == Status.BURN || pokemon.getCondition() == Status.POISON || pokemon.getCondition() == Status.PARALYZE) {
            pokemon.setCondition(new Effect().attack(140));
        }
    }
}
