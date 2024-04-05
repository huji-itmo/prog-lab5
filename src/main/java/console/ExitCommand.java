package console;

import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import commands.nativeCommands.Command;

public class ExitCommand implements Command {
    @Override
    public String execute(String args) throws CommandException {
        if (!args.isBlank()) {
            throw new IllegalCommandSyntaxException("Exit command expects no arguments.", this);
        }

        System.exit(0);
        return "exiting...";
    }
    @Override
    public String getDescription() {
        return "exists the program.";
    }
}
