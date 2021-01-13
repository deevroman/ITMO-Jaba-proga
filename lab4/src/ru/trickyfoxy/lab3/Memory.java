package ru.trickyfoxy.lab3;

import java.util.ArrayList;
import java.util.Objects;

public class Memory implements Memoryble {
    private ArrayList<MemoryAction> arrayOfAction;

    public Memory() {
        this.arrayOfAction = new ArrayList<>();
    }

    @Override
    public void removeFromMemory(MemoryActionType action) {
        for (MemoryAction item : arrayOfAction) {
            if (item.getType() == action) {
                arrayOfAction.remove(item);
                break;
            }
        }
    }


    @Override
    public void save(MemoryAction action) {
        arrayOfAction.add(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memory memory = (Memory) o;
        return Objects.equals(arrayOfAction, memory.arrayOfAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrayOfAction);
    }

    @Override
    public MemoryAction check(MemoryActionType action) {
        for (MemoryAction item : arrayOfAction) {
            if (item.getType() == action) {
                return item;
            }
        }
        return null;
    }

}