package ru.trickyfoxy.lab8.collection;

import ru.trickyfoxy.lab8.exceptions.InvalidFieldException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Класс описывающий LocationTo
 */
@XmlRootElement(name = "to")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {"x", "y", "name"})
public class LocationTo implements Comparable<LocationTo>, Serializable {
    public Integer x; //Поле не может быть null
    public Integer y; //Поле не может быть null
    public String name; //Длина строки не должна быть больше 588, Поле может быть null

    public LocationTo() {
    }

    public LocationTo(Integer x, Integer y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @XmlElement(name = "x")
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) throws InvalidFieldException {
        if (x == null) {
            throw new InvalidFieldException("Поле x не может быть null");
        }
        this.x = x;
    }

    @XmlElement(name = "y")
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) throws InvalidFieldException {
        if (y == null) {
            throw new InvalidFieldException("Поле y не может быть null");
        }
        this.y = y;
    }

    @XmlElement(name = "name")

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidFieldException {
        if (name.equals("")) {
            throw new InvalidFieldException("Поле name не может быть пустым");
        }
        this.name = name;
    }

    @Override
    public int compareTo(LocationTo o) {
        return Comparator.comparing(LocationTo::getX)
                .thenComparing(LocationTo::getY)
                .thenComparing(LocationTo::getName)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "LocationTo{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationTo that = (LocationTo) o;
        return x.equals(that.x) && y.equals(that.y) && Objects.equals(name, that.name);
    }

}
