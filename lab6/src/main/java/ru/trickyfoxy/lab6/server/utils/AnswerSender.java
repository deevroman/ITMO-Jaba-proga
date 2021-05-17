package ru.trickyfoxy.lab6.server.utils;

import ru.trickyfoxy.lab6.utils.Serialization;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class AnswerSender {
    public static void sendAnswer(SocketChannel socketChannel, String answer) throws IOException {
        socketChannel.write(ByteBuffer.wrap(Objects.requireNonNull(Serialization.SerializeObject(answer))));
    }
}