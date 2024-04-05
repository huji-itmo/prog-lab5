package database;

import dataStructs.FormOfEducation;
import dataStructs.exceptions.IllegalValueException;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * interface for general-purpose database. Should be created with decorator.
 *
 * @param <T>
 */
public interface Database<T> {
    int removeGreater(long id);
    int removeLower(long id);

    long sumOfAverageMark();

    String getElementsDescending();

    Optional<Integer> CountLessThanFormOfEducation(FormOfEducation education);

    public enum Predicate {
        GREATER_THAN,
        LESS_THAN
    }
    public enum SortingOrder {
        ASCENDING,
        DESCENDING
    }

    String getInfo();
    void addElement(T element);
    T createElementFromInput(Consumer<String> standardOut, Consumer<String> errorOut);
    T createElementFromString(String input, Consumer<String> standardOut, Consumer<String> errorOut);
    String getAllElements();
    T updateElementById(long id, T new_element) throws IllegalValueException;
    boolean removeElementById(long id) throws IllegalValueException;
    void clearCollection();
    void removeFirstElement() throws IllegalValueException;
    String getConstructorSignature();
    boolean addIfMin(T element);

    void serialize() throws Exception;
    void deserialize() throws Exception;

}
