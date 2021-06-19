package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.*;

public class Pichu extends Pokemon {

    public Pichu() {
        super();
        init();
    }

    public Pichu(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(20, 40, 15, 35, 35, 60);
        this.addType(Type.ELECTRIC);
        this.addMove(new Thunderbolt());
        this.addMove(new Thunder());
    }
}

