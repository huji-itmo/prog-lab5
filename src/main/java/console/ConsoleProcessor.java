package console;

import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import lombok.Data;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

@Data
public class ConsoleProcessor {

    private final AbstractCommandProcessor commandProcessor;

    public ConsoleProcessor(AbstractCommandProcessor commandProcessor) {

        this.commandProcessor = commandProcessor;

    }

    /**
     * Starts a while loop until user writes "exit" command.
     */
    public void startCommandLoop() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type \"help\" to see available commands.");
        try {
            while (true) {
                commandProcessor.executeCommand(
                        scanner.nextLine(),
                        System.out::println,
                        System.err::println);
            }
        } catch (NoSuchElementException e) {
            System.err.println("something went wrong, no string to read...");
            scanner.close();
        }
    }
}
