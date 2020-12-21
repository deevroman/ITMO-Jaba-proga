package ru.trickyfoxy.lab3;

import java.util.Objects;

public class Alice extends Human {
    private Memory memory;

    public Alice() {
        super("Алиса");
        this.memory = new Memory();
    }

    public void standup(BankOfJury bank) {
        if (this.memory.check(MemoryActionType.GROWTHED) == null) {
            this.overturn(bank);
            MemoryAction tmp = this.memory.check(MemoryActionType.OVERTURNED);
            if (tmp != null) {
                System.out.println(this.getName() + " вспомнила что " + tmp.getDescription());
            } else {
                this.memory.save(new MemoryAction(MemoryActionType.OVERTURNED, "опрокинула " + bank.getName()));
            }
        }
    }

    public void overturn(Overturnable obj) {
        System.out.println(this.getName() + " опрокидывает " + obj.getName());
        this.memory.save(new MemoryAction(MemoryActionType.OVERTURNED, "опрокинула " + obj.getName()));
        obj.overturn();
    }

    public void removeFromMemory(MemoryActionType type) {
        memory.removeFromMemory(type);
        if (type == MemoryActionType.GROWTHED)
            System.out.println(this.getName() + " забывает, что выросла");
        else if (type == MemoryActionType.OVERTURNED)
            System.out.println(this.getName() + " забывает, что опрокинула");
    }

    @Override
    public String toString() {
        return "Alice{" +
                "memory=" + memory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alice alice = (Alice) o;
        return Objects.equals(memory, alice.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memory);
    }

    public void growth() {
        this.memory.save(new MemoryAction(MemoryActionType.GROWTHED, "выросла за посленее время"));
        System.out.println(this.getName() + " выросла");
    }
}
