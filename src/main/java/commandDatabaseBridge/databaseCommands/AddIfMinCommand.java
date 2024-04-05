package commandDatabaseBridge.databaseCommands;

import commands.CommandArgument;
import commands.ElementCommandArgument;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import commands.nativeCommands.OverloadedCommand;
import database.Database;
import database.DatabaseEntity;

public class AddIfMinCommand<T extends DatabaseEntity> implements OverloadedCommand {

    private final Database<T> database;
    public AddIfMinCommand(Database<T> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        if (!args.isBlank()) {
            StringBuilder outBuilder = new StringBuilder();

            database.createElementFromString(
                    args,
                    outBuilder::append,
                    (line) -> {throw new CommandException(line); });

            return outBuilder.toString();
        }

        try {
            T element = database.createElementFromInput(System.out::println, System.err::println);

            if (!database.addIfMin(element)) {
                throw new CommandException("not found min!");
            }
            /*
             * This code tells that you can't add new element AFTER you typed it out
             */

            return "Added element: (" + element.getValues(",") + ")";
        }
        catch (NumberFormatException e) {
            throw new IllegalCommandSyntaxException(e.getMessage(), this);
        }
    }

    @Override
    public String getDescription() {
        return "Compares the elements by Students Count and add if the element you created has student count lower than any other element.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new ElementCommandArgument(database)
        };
    }
}
