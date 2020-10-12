package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.*;

public class FlashCannon extends SpecialMove {
    public FlashCannon() {
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected String describe() {
        return "uses Flash Cannon";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setCondition(new Effect().stat(Stat.SPECIAL_DEFENSE, -1));
    }
}
