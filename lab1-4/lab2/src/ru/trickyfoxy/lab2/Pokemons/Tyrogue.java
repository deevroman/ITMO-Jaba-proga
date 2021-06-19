package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.*;

public class Tyrogue extends Pokemon {

    public Tyrogue() {
        super();
        init();
    }

    public Tyrogue(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(35, 35, 35, 35, 35, 35);
        this.addType(Type.FIGHTING);
        this.addMove(new Facade());
        this.addMove(new Swagger());
        this.addMove(new LowSweep());
    }
}
