package ru.trickyfoxy.lab8.collection;

import ru.trickyfoxy.lab8.exceptions.InvalidRouteFieldException;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
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
public class Route implements Comparable<Route>, Serializable {

    @XmlElement(name = "id", required = true)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @XmlElement(name = "name", required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @XmlElement(name = "coordinates", required = true)
    private Coordinates coordinates; //Поле не может быть null
    @XmlElement(name = "creationDate", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date creationDate = new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @XmlElement(name = "from")
    private LocationFrom from; //Поле не может быть null
    @XmlElement(name = "to")
    private LocationTo to; //Поле может быть null
    private float distance; //Значение поля должно быть больше 1

    public String getCreator() {
        return creator;
    }

    public void setCreator(String username) {
        this.creator = username;
    }

    private String creator;

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
            throw new InvalidRouteFieldException("Поле distance класса Route должно быть больше 1");
        }
        this.distance = distance;
    }

    public Route(Long id) {
        this.id = id;
        this.name = "";
        this.coordinates = new Coordinates();
        this.creationDate = new Date();
        this.from = new LocationFrom();
        this.to = new LocationTo();
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

    public String[] toStringArray() {
        return new String[]{
                id.toString(),
                name,
                coordinates.getX().toString(),
                String.valueOf(coordinates.getY()),
                new SimpleDateFormat("yyyy-MM-dd").format(creationDate),
                from.getX().toString(),
                from.getY().toString(),
                from.getZ().toString(),
                from.getName(),
                to.getX().toString(),
                to.getY().toString(),
                to.getName(),
                String.valueOf(distance),
                creator
        };

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

    public String printHTML() {
        return "<html>" + "id=" + id + "<br>" +
                "name='" + name + '\'' + "<br>" +
                "coordinates_x=" + coordinates.getX() + "<br>" +
                "coordinates_y=" + coordinates.getY() + "<br>" +
                "creationDate=" + new SimpleDateFormat("yyyy-MM-dd").format(creationDate) + "<br>" +
                "from_x=" + from.getX() + "<br>" +
                "from_y=" + from.getY() + "<br>" +
                "from_z=" + from.getZ() + "<br>" +
                "from_name=" + from.getName() + "<br>" +
                "to_x=" + to.getX() + "<br>" +
                "to_y=" + to.getY() + "<br>" +
                "to_name=" + to.getName() + "<br>" +
                "distance=" + distance + "<br>" +
                "creator=" + creator + "</html>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Float.compare(route.distance, distance) == 0 && id.equals(route.id) && name.equals(route.name) && coordinates.equals(route.coordinates) && from.equals(route.from) && Objects.equals(to, route.to);
    }

    public void validate() {
        if (name == null || name.equals("")) throw new InvalidRouteFieldException("name не может быть пустым");
        if (coordinates == null) throw new InvalidRouteFieldException("name не может быть пустым");
        coordinates.setX(coordinates.getX());
        coordinates.setY(coordinates.getY());
        if (from == null) throw new InvalidRouteFieldException("locationFrom не может быть пустым");
        from.setX(from.getX());
        from.setY(from.getY());
        from.setZ(from.getZ());
        from.setName(from.getName());
        if (to == null) throw new InvalidRouteFieldException("locationTo не может быть пустым");
        to.setX(to.getX());
        to.setY(to.getY());
        to.setName(to.getName());
        if (distance <= 1) throw new InvalidRouteFieldException("locationTo не может быть пустым");
    }
}
