package ru.trickyfoxy.lab4;

import java.util.ArrayList;
import java.util.Objects;

public class BankOfJury implements Overturnable {
    private ArrayList<Jury> arrayOfJury;
    private ArrayList<Jury> currentArrayOfJury;
    private int countJury = 0;

    @Override
    public String getName() {
        return "Cкамья с присяжными";
    }

    public BankOfJury(ArrayList<Jury> arrayOfPrisyagnye, int count) {
        if(arrayOfPrisyagnye.size() != count){
            throw new OverflowBankException();
        }
        countJury = count;
        this.currentArrayOfJury = this.arrayOfJury = arrayOfPrisyagnye;
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
    public void overturn() throws BrokeTribunal {
        System.out.println(this.getName() + " опрокинулась");
        for (Jury prisyagnye : currentArrayOfJury) {
            prisyagnye.overturn();
        }
        currentArrayOfJury.clear();
        if (countJury != currentArrayOfJury.size()) {
            throw new BrokeTribunal();
        }
    }

    public void recovery(){
        this.currentArrayOfJury = arrayOfJury;
    }
}
