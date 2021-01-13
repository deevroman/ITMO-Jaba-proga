package ru.trickyfoxy.lab4;

public class Jury extends Human implements Overturnable {
    public Jury(String name) {
        super(name, Gender.NONE);
    }

    @Override
    public void overturn() {
        System.out.println(this.getName() + ", опрокинувшись и пролетев вверх тормашками на головы публики, начал биться по полу");
    }

}
