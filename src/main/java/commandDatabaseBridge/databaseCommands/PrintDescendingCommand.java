package commandDatabaseBridge.databaseCommands;

import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import database.Database;

public class PrintDescendingCommand implements Command {

    private final Database<?> database;

    public PrintDescendingCommand(Database<?> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        String out = database.getElementsDescending();

        if (out.isBlank()) {
            throw new CommandException("The database is empty!");
        }
        return out;
    }

    @Override
    public String getDescription() {
        return "Prints out all database entries in descending order by id.";
    }
}
