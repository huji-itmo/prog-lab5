package commands.nativeCommands;

import commands.AbstractCommandProcessor;
import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.exceptions.RecursionIsNotSupportedException;
import dataStructs.StudyGroup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Stack;

public class ExecuteScriptCommand implements OverloadedCommand{

    private final AbstractCommandProcessor commandProcessor;

    private final Stack<Path> scriptPathCallStack = new Stack<>();

    public ExecuteScriptCommand(AbstractCommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public String execute(String args) throws CommandException {
        Path pathToNewScript = Path.of(args);

        try (BufferedReader reader = Files.newBufferedReader(pathToNewScript)){

            if (scriptPathCallStack.contains(pathToNewScript)) {
                scriptPathCallStack.add(pathToNewScript);
                throw new RecursionIsNotSupportedException("execute command script doesn't support recursion!", scriptPathCallStack);
            }

            scriptPathCallStack.add(pathToNewScript);
            reader.lines()
                    .filter(line -> !line.isBlank())
                    .filter(line -> !line.startsWith("#"))
                    .forEach(line -> commandProcessor.executeCommand(line, System.out::println, System.err::println));

        } catch (IOException e) {
            throw new CommandException(e.getMessage());
        }

        scriptPathCallStack.remove(pathToNewScript);
        return "";
    }

    @Override
    public String getDescription() {
        return "Executes script line by line.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("path", "Path to the script file.", false)
        };
    }
}
