package ru.trickyfoxy.lab8.utils;

import ru.trickyfoxy.lab8.commands.Command;

import java.io.Serializable;

public class Event implements Serializable {
    public Command cmd = null;
    public String login = null;
    public String pass = null;
    public String session = null;

    public boolean onetimeQuery = false;

    public EventType eventType = null;

    public Event(Command cmd, EventType eventType, String session) {
        this.cmd = cmd;
        this.eventType = eventType;
        this.session = session;
        this.onetimeQuery = true;
    }

    public Event(Command cmd, EventType eventType, String session, boolean onetimeQuery) {
        this.cmd = cmd;
        this.eventType = eventType;
        this.session = session;
        this.onetimeQuery = onetimeQuery;
    }

    public Event(Command cmd, EventType eventType) {
        this.cmd = cmd;
        this.eventType = eventType;
        this.onetimeQuery = true;
    }

    public Event(String login, String pass, EventType eventType) {
        this.login = login;
        this.pass = pass;
        this.eventType = eventType;
        this.onetimeQuery = true;
    }

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "cmd=" + cmd +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", session='" + session + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}
