package ru.trickyfoxy.lab6.collection;

import org.jetbrains.annotations.NotNull;
import ru.trickyfoxy.lab6.exceptions.InvalidFieldException;

import javax.xml.bind.annotation.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * Класс описываниющий LocationFrom
 */
@XmlRootElement(name = "from")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {"x", "y", "z", "name"})
public class LocationFrom implements Comparable<LocationFrom> {
    @NotNull
    public Integer x; //Поле не может быть null
    @NotNull
    public Double y; //Поле не может быть null
    @NotNull
    public Float z; //Поле не может быть null
    public String name; //Длина строки не должна быть больше 588, Поле может быть null

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
    public Double getY() {
        return y;
    }

    public void setY(Double y) throws InvalidFieldException {
        if (y == null) {
            throw new InvalidFieldException("Поле y не может быть null");
        }
        this.y = y;
    }

    @XmlElement(name = "z")
    public Float getZ() {
        return z;
    }

    public void setZ(Float z) throws InvalidFieldException {
        if (z == null) {
            throw new InvalidFieldException("Поле z не может быть null");
        }
        this.z = z;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidFieldException {
        if (name.equals("")) {
            throw new InvalidFieldException("Поле name не может быть пустым");
        }
        if (name.length() > 588) {
            throw new InvalidFieldException("Длина поля name должна быть не больше 588 символов");
        }
        this.name = name;
    }

    public LocationFrom() {
    }


    public LocationFrom(Integer x, Double y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public int compareTo(LocationFrom o) {
        return Comparator.comparing(LocationFrom::getX)
                .thenComparing(LocationFrom::getY)
                .thenComparing(LocationFrom::getZ)
                .thenComparing(LocationFrom::getName)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "LocationFrom{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationFrom that = (LocationFrom) o;
        return x.equals(that.x) && y.equals(that.y) && z.equals(that.z) && Objects.equals(name, that.name);
    }

}
