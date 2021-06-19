package ru.trickyfoxy.lab8.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import ru.trickyfoxy.lab8.collection.*;
import ru.trickyfoxy.lab8.exceptions.InvalidLoginException;
import ru.trickyfoxy.lab8.exceptions.NotFountId;

import java.sql.*;
import java.util.Random;
import java.util.stream.Collectors;

public class DatabaseManager {
    String uri;
    String user;
    String password;

    public DatabaseManager(String uri, String user, String password) throws SQLException {
        this.uri = uri;
        this.user = user;
        this.password = password;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public boolean register(String username, String password) throws SQLException, InvalidLoginException {
        Connection connection = DriverManager.getConnection(this.uri, this.user, this.password);
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new InvalidLoginException("Имя пользователя уже существует");
            }
        }
        String password_hash = BCrypt.hashpw(password, BCrypt.gensalt());
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password_hash);
            preparedStatement.execute();
        }
        return true;
    }

    public String generateSession() {
        String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return new Random().ints(64, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public boolean authClient(String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(this.uri, this.user, this.password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from users WHERE username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String correct_hash = resultSet.getString("password_hash");
            return BCrypt.checkpw(password, correct_hash);
        }
        return false;
    }


    public Long insertToDB(Route route, String user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            String[] generatedColumns = {"id"};
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO s311693.collection (id, name, coordinates_x, coordinates_y, \"creationDate\", \"locationFrom_x\",\n" +
                    "                                \"locationFrom_y\", \"locationFrom_z\", \"locationFrom_name\", \"locationTo_x\", \"locationTo_y\",\n" +
                    "                                \"locationTo_name\", distance, creator)\n" +
                    "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n", generatedColumns);
            preparedStatement.setString(1, route.getName());
            preparedStatement.setDouble(2, route.getCoordinates().getX());
            preparedStatement.setLong(3, route.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(route.getCreationDate().getTime()));
            preparedStatement.setInt(5, route.getFrom().getX());
            preparedStatement.setDouble(6, route.getFrom().getY());
            preparedStatement.setDouble(7, route.getFrom().getZ());
            preparedStatement.setString(8, route.getFrom().getName());
            preparedStatement.setInt(9, route.getTo().getX());
            preparedStatement.setInt(10, route.getTo().getY());
            preparedStatement.setString(11, route.getTo().getName());
            preparedStatement.setDouble(12, route.getDistance());
            preparedStatement.setString(13, user);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void clear(String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from collection WHERE creator = ?");
            preparedStatement.setString(1, username);
            preparedStatement.execute();
        }
    }

    public Double getMaxByDistance(String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT max(distance) from users WHERE username = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            } else {
                return Double.MIN_VALUE;
            }
        }
    }

    public Double getMinByDistance(String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT min(distance) from users WHERE creator = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            } else {
                return Double.MAX_VALUE;
            }
        }
    }

    public void updateById(Long id, Route route, String username) throws SQLException, NotFountId {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  s311693.collection SET  (name, coordinates_x, coordinates_y, \"creationDate\", \"locationFrom_x\",\n" +
                    "                                \"locationFrom_y\", \"locationFrom_z\", \"locationFrom_name\", \"locationTo_x\", \"locationTo_y\",\n" +
                    "                                \"locationTo_name\", distance, creator)\n" +
                    "= (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) where creator = ?;\n");
            preparedStatement.setString(1, route.getName());
            preparedStatement.setDouble(2, route.getCoordinates().getX());
            preparedStatement.setLong(3, route.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(route.getCreationDate().getTime()));
            preparedStatement.setInt(5, route.getFrom().getX());
            preparedStatement.setDouble(6, route.getFrom().getY());
            preparedStatement.setDouble(7, route.getFrom().getZ());
            preparedStatement.setString(8, route.getFrom().getName());
            preparedStatement.setInt(9, route.getTo().getX());
            preparedStatement.setInt(10, route.getTo().getY());
            preparedStatement.setString(11, route.getTo().getName());
            preparedStatement.setDouble(12, route.getDistance());
            preparedStatement.setString(13, username);
            preparedStatement.setString(14, username);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFountId();
            }
        }
    }

    public boolean removeById(Long id, String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM collection WHERE id = ? AND creator = ? ");
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, username);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows != 0;
        }
    }

    public RouteStorage getCollection() throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            RouteStorage rs = new RouteStorageImpl();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from collection");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Route route = new Route();
                route.setId(resultSet.getLong("id"));
                route.setName(resultSet.getString("name"));
                route.setCoordinates(new Coordinates(resultSet.getFloat("coordinates_x"),
                        resultSet.getInt("coordinates_y")));
                route.setCreationDate(resultSet.getTimestamp("creationDate"));
                route.setFrom(new LocationFrom(resultSet.getInt("locationFrom_x"),
                        resultSet.getDouble("locationFrom_y"),
                        resultSet.getFloat("locationFrom_z"),
                        resultSet.getString("locationFrom_name")
                ));
                route.setTo(new LocationTo(resultSet.getInt("locationTo_x"),
                        resultSet.getInt("locationTo_y"),
                        resultSet.getString("locationTo_name")
                ));
                route.setDistance(resultSet.getFloat("distance"));
                route.setCreator(resultSet.getString("creator"));
                rs.add(route);
            }
            return rs;
        }
    }

    public boolean removeByDistance(float distance, String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM collection WHERE distance = ? AND creator = ? ");
            preparedStatement.setFloat(1, distance);
            preparedStatement.setString(2, username);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows != 0;
        }
    }

    public void removeGreater(Route route, String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM collection WHERE distance > ? AND creator = ? ");
            preparedStatement.setFloat(1, route.getDistance());
            preparedStatement.setString(2, username);
            int affectedRows = preparedStatement.executeUpdate();
        }
    }
}
