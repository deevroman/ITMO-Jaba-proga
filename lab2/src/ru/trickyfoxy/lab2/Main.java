package ru.trickyfoxy.lab2;

import ru.ifmo.se.pokemon.*;

public class Main {

    public static class Deoxys extends Pokemon {

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

    public static class Tyrogue extends Pokemon {

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

    public static class Hitmonlee extends Pokemon {

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

    public static class Pichu extends Pokemon {

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

    public static class Pikachu extends Pokemon {

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

    public static class Raichu extends Pokemon {

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
            this.addMove(new Thunderbolt());
            this.addMove(new Thunderbolt());
            this.addMove(new Thunderbolt());
        }
    }

    private static class DarkPulse extends SpecialMove {
        public DarkPulse() {
            super(Type.DARK, 80, 100);
        }

        @Override
        protected String describe() {
            return "uses Dark Pulse";
        }

        @Override
        protected void applyOppEffects(Pokemon pokemon) {
            if (Math.random() < 0.2) {
                Effect.flinch(pokemon);
            }
        }
    }

    private static class Swagger extends StatusMove {
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

    private static class Thunder extends SpecialMove {
        public Thunder() {
            super(Type.ELECTRIC, 110, 70);
        }

        @Override
        protected String describe() {
            return "uses Thunder";
        }

        @Override
        protected void applyOppEffects(Pokemon pokemon) {
            pokemon.setCondition(new Effect().chance(0.3).condition(Status.PARALYZE));
        }
    }

    private static class FlashCannon extends SpecialMove {
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

    private static class Facade extends PhysicalMove {
        public Facade() {
            super(Type.DARK, 80, 100);
        }

        @Override
        protected String describe() {
            return "uses Facade";
        }


        @Override
        protected void applyOppEffects(Pokemon pokemon) {
            if (pokemon.getCondition() == Status.BURN || pokemon.getCondition() == Status.POISON || pokemon.getCondition() == Status.PARALYZE) {
                pokemon.setCondition(new Effect().attack(140));
            }
        }
    }

    private static class LowSweep extends PhysicalMove {
        public LowSweep() {
            super(Type.FIGHTING, 65, 100);
        }

        @Override
        protected String describe() {
            return "uses Low Sweep";
        }

        protected void applyOppEffects(Pokemon pokemon) {
            pokemon.setCondition(new Effect().stat(Stat.SPEED, -1));
        }
    }

    private static class StoneEdge extends PhysicalMove {
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

    private static class Thunderbolt extends SpecialMove {
        public Thunderbolt() {
            super(Type.ELECTRIC, 90, 100);
        }

        @Override
        protected void applyOppEffects(Pokemon pokemon) {
            pokemon.setCondition(new Effect().chance(0.1).condition(Status.PARALYZE));
        }

        @Override
        protected String describe() {
            return "uses Thunderbolt";
        }
    }

    private static class Slam extends PhysicalMove {
        public Slam() {
            super(Type.NORMAL, 80, 75);
        }

        @Override
        protected String describe() {
            return "uses Slam";
        }
    }

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
