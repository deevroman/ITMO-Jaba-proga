package ru.trickyfoxy.lab5.collection;

import org.jetbrains.annotations.NotNull;
import ru.trickyfoxy.lab5.exceptions.InvalidRouteFieldException;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * Класс описывающий Route
 */
@XmlRootElement(name = "route")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {"id", "name", "coordinates", "creationDate", "from", "to", "distance"})
public class Route implements Comparable<Route> {

    @NotNull
    @XmlElement(name = "id", required = true)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NotNull
    @XmlElement(name = "name", required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @XmlElement(name = "coordinates", required = true)
    private Coordinates coordinates; //Поле не может быть null
    @NotNull
    @XmlElement(name = "creationDate", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date creationDate = new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @NotNull
    @XmlElement(name = "from")
    private LocationFrom from; //Поле не может быть null
    @XmlElement(name = "to")
    private LocationTo to; //Поле может быть null
    private float distance; //Значение поля должно быть больше 1

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public void setFrom(LocationFrom from) {
        this.from = from;
    }

    public LocationTo getTo() {
        return to;
    }

    public void setTo(LocationTo to) {
        this.to = to;
    }

    @XmlElement(name = "distance")
    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if (distance <= 1) {
            throw new InvalidRouteFieldException("Поле distance класса Route lолжно быть больше 1");
        }
        this.distance = distance;
    }

    public Route(Long id) {
        this.id = id;
        this.name = "";
        this.coordinates = new Coordinates();
        this.creationDate = new Date();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route(Long id, String name, Coordinates coordinates, Date creationDate, LocationFrom from, LocationTo to, float distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route() {

    }

    @Override
    public int compareTo(Route o) {
        return Comparator.comparing(Route::getId)
//                .thenComparing(Route::getName)
//                .thenComparing(Route::getCoordinates)
//                .thenComparing(Route::getCreationDate)
//                .thenComparing(Route::getFrom)
//                .thenComparing(Route::getTo)
//                .thenComparing(Route::getDistance)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + new SimpleDateFormat("yyyy-MM-dd").format(creationDate) +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Float.compare(route.distance, distance) == 0 && id.equals(route.id) && name.equals(route.name) && coordinates.equals(route.coordinates) && from.equals(route.from) && Objects.equals(to, route.to);
    }

}
