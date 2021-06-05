package ru.trickyfoxy.lab7.utils;

public class Connect {
    public static final int timeOfSessionLife = 100000;
    public byte[] byteArray = new byte[0];
    public double startTime = System.currentTimeMillis();
    public String session = null;
    public double expireTime = 0;
    public String username;

    public Connect() {

    }

    public void createSession(String session) {
        this.session = session;
        expireTime = System.currentTimeMillis() + timeOfSessionLife;
    }
}