package commands.nativeCommands;

import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;

import java.util.stream.Collectors;

public class HistoryCommand implements Command {
    private final AbstractCommandProcessor commandProcessor;
    public HistoryCommand(AbstractCommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }
    @Override
    public String execute(String args) throws CommandException {
        if (!args.isBlank()) {
            throw new IllegalCommandSyntaxException("History command can only except no arguments.", this);
        }

        return commandProcessor.getHistory().stream()
                .map(key -> key + "\n")
                .collect(Collectors.joining())
                .trim();
    }

    @Override
    public String getDescription() {
        return "Prints out names of the last 11 commands that are executed.";
    }
}
