package ru.trickyfoxy.lab5.commands;

import ru.trickyfoxy.lab5.collection.RouteStorage;
import ru.trickyfoxy.lab5.utils.ReadWriteInterface;

import java.io.IOException;

public class Help extends Command {
    public Help() {
        name = "help";
        description = "вывести справку по доступным командам";
    }

    @Override
    public String execute(ReadWriteInterface readWriteInterface, RouteStorage routeStorage) throws IOException {
        for (Command cmd : CommandsManager.getInstance().getAllCommands()) {
            readWriteInterface.writeln(cmd.getName() + ": " + cmd.getDescription());
        }
        return null;
    }
}
