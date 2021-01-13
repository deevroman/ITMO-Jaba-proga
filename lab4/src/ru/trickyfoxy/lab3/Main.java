package ru.trickyfoxy.lab3;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws BrokeTribunal {
        Alice alice = new Alice();
        alice.growth();

        Fish fish1 = new Fish("Золотая рыбка 1");
        Fish fish2 = new Fish("Золотая рыбка 2");
        ArrayList<Fish> fishArray = new ArrayList<>();
        fishArray.add(fish1);
        fishArray.add(fish2);
        Aqurium aqurium = new Aqurium(fishArray);
        alice.overturn(aqurium);

        Human king = new Human("Король", Gender.MALE) {
            @Override
            public String getName() {
                return super.getName();
            }

            @Override
            public void scream(String text) {
                System.out.println(getName() + " крикнул:\"" + text + "\"");
            }
        };

        try {
            alice.scream("Я-а!");
            alice.removeFromMemory(MemoryActionType.GROWTHED);

            Jury jury1 = new Jury("Присяжный 1");
            Jury jury2 = new Jury("Присяжный 2");
            ArrayList<Jury> juryArray = new ArrayList<>();
            juryArray.add(jury1);
            juryArray.add(jury2);
            BankOfJury bank = new BankOfJury(juryArray, 2);
            alice.standup(bank);
        } catch (BrokeTribunal e) {
            king.scream("Судоговорение не может продолжаться");
            king.scream("пока все присяжные не будут, как подобает, водворены на место. ВСЕ!");
        }

    }
}
