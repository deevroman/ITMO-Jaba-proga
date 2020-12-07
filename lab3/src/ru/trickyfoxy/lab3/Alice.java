package ru.trickyfoxy.lab3;

public class Alice extends Human {
    private Memory memory;

    public Alice() {
        super("Алиса");
        this.memory = new Memory();
    }

    public void standup(BankOfJury bank) {
        if (this.memory.check(MemoryActionType.VYROCTANIE) == null) {
            this.overturn(bank);
            MemoryAction tmp = this.memory.check(MemoryActionType.OPROKINED);
            if (tmp != null) {
                System.out.println(this.getName() + " вспомнила что " + tmp.getDescription());
            } else {
                this.memory.save(new MemoryAction(MemoryActionType.OPROKINED, "опрокинула " + bank.getName()));
            }
        }
    }

    public void overturn(Overturnable obj) {
        System.out.println(this.getName() + " опрокидывает " + obj.getName());
        this.memory.save(new MemoryAction(MemoryActionType.OPROKINED, "опрокинула " + obj.getName()));
        obj.overturn();
    }

    public void removeFromMemory(MemoryActionType type) {
        memory.removeFromMemory(type);
        if (type == MemoryActionType.VYROCTANIE)
            System.out.println(this.getName() + " забывает, что опрокинула");
    }

    public void VYROSTI() {
        this.memory.save(new MemoryAction(MemoryActionType.VYROCTANIE, "выросла за посленее время"));
        System.out.println(this.getName() + "выросла");
    }
}
