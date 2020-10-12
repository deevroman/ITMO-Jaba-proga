package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.*;

public class Raichu extends Pokemon {

    public Raichu() {
        super();
        init();
    }

    public Raichu(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(60, 90, 55, 90, 80, 110);
        this.addType(Type.ELECTRIC);
        this.addMove(new Thunderbolt());
        this.addMove(new Thunder());
        this.addMove(new Slam());
//        this.addMove(new DoubleTeam());
    }
}
