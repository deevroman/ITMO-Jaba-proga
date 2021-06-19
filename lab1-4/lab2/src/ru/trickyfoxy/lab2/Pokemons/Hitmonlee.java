package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.*;


public class Hitmonlee extends Pokemon {

    public Hitmonlee() {
        super();
        init();
    }

    public Hitmonlee(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(50, 120, 53, 35, 110, 87);
        this.addType(Type.FIGHTING);
        this.addMove(new Facade());
        this.addMove(new Swagger());
        this.addMove(new LowSweep());
        this.addMove(new StoneEdge());
    }
}
