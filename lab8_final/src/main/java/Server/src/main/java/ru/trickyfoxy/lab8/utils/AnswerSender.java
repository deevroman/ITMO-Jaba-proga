package ru.trickyfoxy.lab8.utils;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class AnswerSender {
    private AnswerSender() {}

    public static void sendAnswer(SocketChannel socketChannel, ServerAnswer answer) throws IOException {
        socketChannel.write(ByteBuffer.wrap(Objects.requireNonNull(Serialization.serializeObject(answer))));
    }
}