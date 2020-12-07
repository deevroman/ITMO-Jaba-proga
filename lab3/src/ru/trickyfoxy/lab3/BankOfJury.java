package ru.trickyfoxy.lab3;

import java.util.ArrayList;
import java.util.Objects;

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
    public String toString() {
        return "BankOfJury{" +
                "arrayOfJury=" + arrayOfJury +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankOfJury that = (BankOfJury) o;
        return Objects.equals(arrayOfJury, that.arrayOfJury);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrayOfJury);
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
