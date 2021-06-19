package ru.trickyfoxy.lab3;

public interface Memoryble {
    public void removeFromMemory(MemoryActionType action);

    public void save(MemoryAction action);

    public MemoryAction check(MemoryActionType action);
}
