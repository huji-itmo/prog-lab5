package commandDatabaseBridge.databaseCommands;

import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import commands.nativeCommands.Command;
import database.Database;

public class ShowCommand implements Command {
    Database<?> database;
    public ShowCommand(Database<?> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        if (!args.isBlank()) {
            throw new IllegalCommandSyntaxException("Command show doesn't accept any arguments!", this);
        }

        return database.getAllElements();
    }
    @Override
    public String getDescription() {
        return "Prints out all entries in database.";
    }
}
