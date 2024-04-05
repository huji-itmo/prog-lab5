package database;

import dataStructs.exceptions.IllegalValueException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.function.Consumer;

@Getter
public abstract class CollectionDatabase<T> implements Database<T> {
    private final Collection<T> collection;
    private final EntityBuilder<T> builder;

    protected CollectionDatabase(Collection<T> collection, EntityBuilder<T> builder) {
        this.collection = collection;
        this.builder = builder;
    }

    @Override
    public String getInfo() {
        return "Type: " +
                collection.getClass().getSimpleName() +
                "\nNumber of entries: " +
                collection.size() +
                "\n";
    }

    public String getAllElements() {
        StringBuilder builder = new StringBuilder();

        if (collection.isEmpty()) {
            builder.append("The database is currently empty.");
            return builder.toString();
        }

        collection.forEach(instance -> {
            builder.append(instance).append("\n");
        });

        return builder.toString();
    }

    public void addElement(T element) {
        collection.add(element);
    }

    @Override
    public T createElementFromString(String input, Consumer<String> standardOut, Consumer<String> errorOut) {
        return builder.buildFromString(input, standardOut, errorOut);
    }

    @Override
    public T createElementFromInput(Consumer<String> standardOut, Consumer<String> errorOut) {
        return builder.buildFromInput(standardOut, errorOut);
    }


    public void clearCollection() {
        collection.clear();
    }
}
