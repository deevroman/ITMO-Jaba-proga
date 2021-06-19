package ru.trickyfoxy.lab8.collection;

import ru.trickyfoxy.lab8.exceptions.InvalidFieldException;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Comparator;


/**
 * Класс описываниющий Coordinates
 */
@XmlRootElement(name="coordinates")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {"x", "y"})
public class Coordinates implements Comparable<Coordinates>, Serializable {
    public final static float maxX = 717;
    public final static float maxY = 735;
    @NotNull
    private Float x; //Максимальное значение поля: 717, Поле не может быть null
    private long y; //Максимальное значение поля: 735

    public Coordinates(Float x, long y) {
        this.x = x;
        this.y = y;
    }

    @NotNull
    @XmlElement(name="x")
    public Float getX() {
        return x;
    }

    public void setX(Float x) throws InvalidFieldException {
        if (x > maxX) {
            throw new InvalidFieldException("Поле x класса coordinates должно быть не больше " + maxX);
        }
        this.x = x;
    }

    @NotNull
    @XmlElement(name="y")
    public long getY() {
        return y;
    }

    public void setY(long y) throws InvalidFieldException {
        if (y > maxY) {
            throw new InvalidFieldException("Поле y класса coordinates должно быть не больше 735" + maxY);
        }
        this.y = y;
    }

    public Coordinates() {

    }


    @Override
    public int compareTo(Coordinates o) {
        return Comparator.comparing(Coordinates::getX)
                .thenComparing(Coordinates::getY)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y && x.equals(that.x);
    }

}
