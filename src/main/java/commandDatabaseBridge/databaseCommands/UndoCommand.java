package commandDatabaseBridge.databaseCommands;

import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoCommand implements Command {
    private final Database<?> database;

    public UndoCommand(Database<?> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        boolean res = database.undo();

        if (!res) {
            throw new CommandException("Nothing to undo.");
        }
        return "";

    }

    @Override
    public String getDescription() {
        return "Undoes last changes";
    }


}
