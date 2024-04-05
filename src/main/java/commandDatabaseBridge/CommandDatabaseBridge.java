package commandDatabaseBridge;

import commandDatabaseBridge.databaseCommands.*;
import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import console.ExitCommand;
import database.Database;
import database.DatabaseEntity;

public class CommandDatabaseBridge<T extends DatabaseEntity> {
    CommandProcessor processor;
    Database<T> database;

    public CommandDatabaseBridge(AbstractCommandProcessor processor, Database<T> database) {
        processor.getCommands().put("info", new InfoCommand(database));
        ExitCommand command = (ExitCommand) processor.getCommands().get("exit");
        processor.getCommands().put("exit", new ExitDatabaseCommand());
        processor.getCommands().put("add", new AddCommand<>(database));
        processor.getCommands().put("show", new ShowCommand(database));
        processor.getCommands().put("update", new UpdateByIdCommand<>(database));
        processor.getCommands().put("save", new SaveCommand(database));
        processor.getCommands().put("clear", new ClearCommand(database));
        processor.getCommands().put("remove_by_id", new RemoveByIdCommand(database));
        processor.getCommands().put("add_if_min", new AddIfMinCommand<>(database));
        processor.getCommands().put("remove_greater", new RemoveGreaterCommand(database));
        processor.getCommands().put("remove_lower", new RemoveLowerCommand(database));
        processor.getCommands().put("sum_of_average_mark", new SumOfAverageMarkCommand(database));
        processor.getCommands().put("print_descending", new PrintDescendingCommand(database));
        processor.getCommands().put("count_less_than_form_of_education", new CountLessThanFormOfEducationCommand(database));
    }
}
