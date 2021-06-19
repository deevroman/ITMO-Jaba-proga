package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.*;

public class Pikachu extends Pokemon {

    public Pikachu() {
        super();
        init();
    }

    public Pikachu(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(35, 55, 40, 50, 50, 90);
        this.addType(Type.ELECTRIC);
        this.addMove(new Thunderbolt());
        this.addMove(new Thunder());
        this.addMove(new Slam());
    }
}
