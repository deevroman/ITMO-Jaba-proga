package ru.trickyfoxy.lab3;

public class Jury extends Human implements Overturnable {
    public Jury(String name) {
        super(name);
    }

    @Override
    public void overturn() {
        System.out.println(this.getName() + ", опрокинувшись и пролетев вверх тормашками на головы публики, начал биться по полу");
    }

}
