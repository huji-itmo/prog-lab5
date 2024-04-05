package commandDatabaseBridge.databaseCommands;

import commands.CommandArgument;
import commands.ElementCommandArgument;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import commands.nativeCommands.OverloadedCommand;
import dataStructs.exceptions.IllegalValueException;
import database.Database;
import database.DatabaseEntity;

public class UpdateByIdCommand<T extends DatabaseEntity> implements OverloadedCommand {

    private final Database<T> database;

    public UpdateByIdCommand(Database<T> database) {
        this.database = database;
    }

    @Override
    public String execute(String args) throws CommandException {
        if (args.isBlank()) {
            throw new IllegalCommandSyntaxException("Arguments of the command update can't be empty!", this);
        }
        try {
            String[] argsSplit = args.split(" ", 2);

            long id = Long.parseLong(argsSplit[0]);


            if (argsSplit.length == 1) {
                T element = database.updateElementById(
                        id,
                        database.createElementFromInput(
                                System.out::println,
                                System.err::println));


                return "Updated element with id: " + id + ", value: " + "(" + element.getValues(",") + ")";
            }
            else if (argsSplit.length == 2) {
                StringBuilder outBuilder = new StringBuilder();

                database.updateElementById(
                        id,
                        database.createElementFromString(
                                argsSplit[1],
                                outBuilder::append,
                                (line) -> { throw new CommandException(line); }));

                return outBuilder.toString();
            }
            else {
                throw new IllegalCommandSyntaxException("Wrong number of parameters.", this);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalCommandSyntaxException("Expected a number in {id}.", this);
        }
        catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Updates the element based on it's ID, by creating a new element and replacing with an old one.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("id", "The id of an element that will be replaced.", false),
                new ElementCommandArgument(database)
        };
    }
}
