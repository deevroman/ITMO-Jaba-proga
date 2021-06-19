package ru.trickyfoxy.lab2.Pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.trickyfoxy.lab2.Attacks.DarkPulse;
import ru.trickyfoxy.lab2.Attacks.FlashCannon;
import ru.trickyfoxy.lab2.Attacks.Swagger;
import ru.trickyfoxy.lab2.Attacks.Thunder;

public class Deoxys extends Pokemon {

    public Deoxys() {
        super();
        init();
    }

    public Deoxys(String s, int i) {
        super(s, i);
        init();
    }

    private void init() {
        this.setStats(50, 150, 50, 150, 50, 150);
        this.addType(Type.PSYCHIC);
        this.addMove(new DarkPulse());
        this.addMove(new Swagger());
        this.addMove(new Thunder());
        this.addMove(new FlashCannon());
    }
}