package ru.trickyfoxy.lab8.utils;

public class Connect {
    public static final int timeOfSessionLife = 100000;
    public byte[] byteArray = new byte[0];
    public double startTime = System.currentTimeMillis();

    public String username;
    public String session = null;
    public double sessionExpireTime = 0;

    public void createSession(String session) {
        this.session = session;
        sessionExpireTime = System.currentTimeMillis() + timeOfSessionLife;
    }
}