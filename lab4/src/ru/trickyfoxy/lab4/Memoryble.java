package ru.trickyfoxy.lab4;

public interface Memoryble {
    public void removeFromMemory(MemoryActionType action);

    public void save(MemoryAction action);

    public MemoryAction check(MemoryActionType action);
}
