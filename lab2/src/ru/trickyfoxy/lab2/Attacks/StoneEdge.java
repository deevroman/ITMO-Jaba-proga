package ru.trickyfoxy.lab2.Attacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class StoneEdge extends PhysicalMove {
    public StoneEdge() {
        super(Type.ROCK, 100, 80);
    }

    @Override
    protected String describe() {
        return "uses Stone Edge";
    }

    @Override
    protected double calcCriticalHit(Pokemon att, Pokemon def) {
        if (att.getStat(Stat.SPEED) / 512.0D > Math.random()) {
            System.out.println("critical");
            return 2.0D;
        } else {
            return 1.0D;
        }
    }
}
