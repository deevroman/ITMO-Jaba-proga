package ru.trickyfoxy.lab8;

public class DatabaseConfig {
    public static final String URI = System.getenv("database_uri");
    public static final String USER = System.getenv("database_user");
    public static final String PASSWORD = System.getenv("database_password");
}
