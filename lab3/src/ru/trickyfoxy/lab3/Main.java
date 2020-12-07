package ru.trickyfoxy.lab3;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Alice alice = new Alice();

        Fish fish1 = new Fish("Золотая рыбка 1");
        Fish fish2 = new Fish("Золотая рыбка 2");
        ArrayList<Fish> fishArray = new ArrayList<>();
        fishArray.add(fish1);
        fishArray.add(fish2);
        Aqurium aqurium = new Aqurium(fishArray);
        alice.overturn(aqurium);


        alice.scream("Я-а!");
        alice.removeFromMemory(MemoryActionType.VYROCTANIE);

        Jury jury1 = new Jury("Присяжный 1");
        Jury jury2 = new Jury("Присяжный 2");
        ArrayList<Jury> juryArray = new ArrayList<>();
        juryArray.add(jury1);
        juryArray.add(jury2);
        BankOfJury bank = new BankOfJury(juryArray);
        alice.standup(bank);
    }
}
