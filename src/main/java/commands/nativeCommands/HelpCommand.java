package commands.nativeCommands;

import commands.*;
import commands.exceptions.CommandDoesntExistsException;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;

import java.util.Arrays;
import java.util.Map;

public class HelpCommand implements OverloadedCommand {
    private final AbstractCommandProcessor commandProcessor;

    public HelpCommand(AbstractCommandProcessor processor) {
        commandProcessor = processor;
    }
    @Override
    public String execute(String args) throws CommandException {
        if (args.isBlank()) {
            return showAllCommands();
        }
        else if (!args.contains(" ")) {
            if (!commandProcessor.getCommands().containsKey(args)) {
                throw new CommandDoesntExistsException(args);
            }

            return showInfoAboutCommand(commandProcessor.getCommands().get(args));
        }
        else {
            throw new IllegalCommandSyntaxException("Help command expects only one or none arguments.", this);
        }
    }

    public String showInfoAboutCommand(Command command) {
        StringBuilder builder = new StringBuilder(command.getDescription());

        if (!(command instanceof OverloadedCommand)) {
            return builder.toString();
        }

        builder.append("\n\n    Syntax: ").append(command.getArgumentsSyntax()).append("\n");

        CommandArgument[] arguments = ((OverloadedCommand) command).getArguments();

        for (CommandArgument argument : arguments) {
            builder.append("\n")
                    .append(" ".repeat(4))
                    .append(argument.getName())
                    .append(argument.isOptional()? " (optional)" : "")
                    .append(" - ")
                    .append(argument.getDescription());
        }

        return builder.toString();
    }

    public String showAllCommands() {

        StringBuilder builder = new StringBuilder();

        for(Map.Entry<String, Command> entry: commandProcessor.getCommands().entrySet()) {
            builder.append(entry.getKey());

            if (entry.getValue() instanceof OverloadedCommand) {
                CommandArgument[] arguments = ((OverloadedCommand) entry.getValue()).getArguments();

                for (CommandArgument commandArgument : arguments) {
                    builder.append(" ").append(commandArgument);
                }
            }

            builder.append("\n");
        }

        return builder.toString().trim();
    }

    @Override
    public String getDescription() {
        return "Shows all commands if no arguments provided. Displays info about command you provided.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("command_name", "Name of the command you want to know more about.", true)
        };
    }
}
