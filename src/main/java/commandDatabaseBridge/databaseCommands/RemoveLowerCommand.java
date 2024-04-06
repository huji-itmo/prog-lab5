package commandDatabaseBridge.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import database.Database;

public class RemoveLowerCommand implements OverloadedCommand {

    private final Database<?> database;

    public RemoveLowerCommand(Database<?> decorator) {
        this.database = decorator;
    }

    @Override
    public String execute(String args) throws CommandException {

        try {

            int count = database.removeGreaterOrLowerThanId(Integer.parseInt(args), false);

            return "Removed " + count + " elements.";
        }
        catch (NumberFormatException e) {
            throw new CommandException("id should be a number!");
        }
    }

    @Override
    public String getDescription() {
        return "Removes an element by given key and value.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("id", "the id that all elements with greater id will be removed", false)
        };
    }
}
