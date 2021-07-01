package ru.trickyfoxy.lab8.utils;

import ru.trickyfoxy.lab8.collection.Route;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerAnswer implements Serializable {
    public String message;
    public String session_token;
    public ServerAnswerStatus status = ServerAnswerStatus.OK;
    public ArrayList<Route> storage;

    public ServerAnswer() {
    }

    public ServerAnswer(String message) {
        this.message = message;
    }

    public ServerAnswer(String message, String session_token) {
        this.message = message;
        this.session_token = session_token;
    }

    public ServerAnswer(ArrayList<Route> storage, String session_token) {
        this.storage = storage;
        this.session_token = session_token;
    }

    public ServerAnswer(String message, String session_token, ServerAnswerStatus status) {
        this.message = message;
        this.session_token = session_token;
        this.status = status;
    }

    public ServerAnswer(String message, String session_token, ServerAnswerStatus status, ArrayList<Route> storage) {
        this.message = message;
        this.session_token = session_token;
        this.status = status;
        this.storage = storage;
    }
}
