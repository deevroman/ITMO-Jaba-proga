package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 85);
    }

    @Override
    protected String describe() {
        return "uses Swagger";
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.confuse();
        pokemon.setCondition(new Effect().stat(Stat.ATTACK, 2));
    }
}
