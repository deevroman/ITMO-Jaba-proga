package ru.trickyfoxy.lab4;

import java.util.Objects;

public class MemoryAction {
    private final MemoryActionType type;
    private final String text;

    public MemoryActionType getType() {
        return type;
    }

    public String getDescription() {
        return text;
    }

    public MemoryAction(MemoryActionType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAction that = (MemoryAction) o;
        return type == that.type && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text);
    }

    @Override
    public String toString() {
        return "MemoryAction{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
