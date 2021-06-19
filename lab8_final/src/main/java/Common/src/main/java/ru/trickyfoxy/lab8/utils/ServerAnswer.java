package ru.trickyfoxy.lab8.utils;

import java.io.Serializable;

public class ServerAnswer implements Serializable {
    public String message;
    public String session_token;
    public ServerAnswerStatus status = ServerAnswerStatus.OK;
    public ServerAnswer(String message) {
        this.message = message;
    }

    public ServerAnswer(String message, String session_token) {
        this.message = message;
        this.session_token = session_token;
    }

    public ServerAnswer(String message, String session_token, ServerAnswerStatus status) {
        this.message = message;
        this.session_token = session_token;
        this.status = status;
    }
}
