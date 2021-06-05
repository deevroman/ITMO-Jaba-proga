package ru.trickyfoxy.lab7.commands;

import ru.trickyfoxy.lab7.collection.RouteStorage;
import ru.trickyfoxy.lab7.utils.ReadWriteInterface;

import java.io.IOException;

public class Help extends Command {
    public Help() {
        name = "help";
        description = "вывести справку по доступным командам";
    }

    @Override
    public void fillArgument(ReadWriteInterface readWriteInterface) {
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage, String username) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Command cmd : CommandsManager.getInstance().getAllCommands()) {
            sb.append(cmd.getName()).append(": ").append(cmd.getDescription()).append("\n");
        }
        readWriteInterface.write(sb.toString());
        return null;
    }
}
