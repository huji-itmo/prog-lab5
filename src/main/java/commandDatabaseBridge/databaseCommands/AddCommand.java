package commandDatabaseBridge.databaseCommands;

import commands.CommandArgument;
import commands.ElementCommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import database.Database;
import database.DatabaseEntity;

import java.util.Map;

public class AddCommand<T extends DatabaseEntity> implements OverloadedCommand {

    Database<T> database;

    public AddCommand(Database<T> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        if (!args.isBlank()) {
            StringBuilder outBuilder = new StringBuilder();

            T element = database.createElementFromString(
                    args,
                    outBuilder::append,
                    (line) -> {throw new CommandException(line);});

            database.addElement(element);

            return outBuilder.toString();
        }
        else {
            T element = database.createElementFromInput(
                    System.out::println,
                    System.err::println);

            database.addElement(element);

            return "Created element: (" + element.getValues(",") + ")";
        }
    }

    @Override
    public String getDescription() {
        return "Adds a new element based on user's input.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new ElementCommandArgument(database)
        };
    }
}
