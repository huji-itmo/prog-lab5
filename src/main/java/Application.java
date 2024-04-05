import commandDatabaseBridge.CommandDatabaseBridge;
import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.exceptions.CommandException;
import console.ConsoleProcessor;
import dataStructs.StudyGroup;
import dataStructs.StudyGroupList;
import database.EntityBuilder;
import database.StudyGroupDatabase;
import database.StudyGroupEntityBuilder;
import parser.JsonFileParser;

import java.io.StringReader;
import java.nio.file.Path;
import java.util.*;

public class Application {
    public static void run(String[] args) {

        if (args == null || args.length == 0 || args[0].isBlank()) {
            System.err.println("Please pass the path to the file!");
            return;
        }

        AbstractCommandProcessor processor = new CommandProcessor();
        ConsoleProcessor consoleProcessor = new ConsoleProcessor(processor);

        StudyGroupDatabase database = new StudyGroupDatabase(
                new StudyGroupEntityBuilder(),
                new JsonFileParser<>(Path.of(args[0]), StudyGroupList.class));

        TreeMap

        new CommandDatabaseBridge<>(processor, database);

        consoleProcessor.startCommandLoop();
    }
}


