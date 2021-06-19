package ru.trickyfoxy.lab2;

import ru.ifmo.se.pokemon.*;
import ru.trickyfoxy.lab2.Pokemons.*;

public class Main {

    public static void main(String[] args) {
        Battle b = new Battle();
        Deoxys p1 = new Deoxys("1", 1);
        Tyrogue p2 = new Tyrogue("2", 1);
        Hitmonlee p3 = new Hitmonlee("3", 1);
        Pichu p4 = new Pichu("4", 1);
        Pikachu p5 = new Pikachu("5", 44);
        Raichu p6 = new Raichu("6", 1);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}
