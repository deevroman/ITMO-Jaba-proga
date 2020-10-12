package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.*;

public class LowSweep extends PhysicalMove {
    public LowSweep() {
        super(Type.FIGHTING, 65, 100);
    }

    @Override
    protected String describe() {
        return "uses Low Sweep";
    }

    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setCondition(new Effect().stat(Stat.SPEED, -1));
    }
}
