package ru.trickyfoxy.lab3;

import java.util.ArrayList;

public class BankOfJury implements Overturnable {
    private ArrayList<Jury> arrayOfJury;
    @Override
    public String getName() {
        return "Cкамья с присяжными";
    }

    public BankOfJury(ArrayList<Jury> arrayOfPrisyagnye) {
        this.arrayOfJury = arrayOfPrisyagnye;
    }

    @Override
    public void overturn() {
        System.out.println(this.getName() + " опрокинулась");
        for (Jury prisyagnye : arrayOfJury) {
            prisyagnye.overturn();
        }
        arrayOfJury.clear();
    }
}
