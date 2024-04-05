package commands;

import database.Database;

public class ElementCommandArgument extends CommandArgument{
    public ElementCommandArgument(Database<?> database) {
        super("element",
                "If this argument is empty, element will be constructed based on user's input.\n" +
                        "Or command tries to construct element with signature: \n" + database.getConstructorSignature(), true);
    }
}
