package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.*;

public class Thunder extends SpecialMove {
    public Thunder() {
        super(Type.ELECTRIC, 110, 70);
    }

    @Override
    protected String describe() {
        return "uses Thunder";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setCondition(new Effect().chance(0.3).condition(Status.PARALYZE));
    }
}

