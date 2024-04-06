package database;

import dataStructs.FormOfEducation;
import dataStructs.exceptions.IllegalValueException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.Consumer;

/**
 * interface for general-purpose database. Should be created with decorator.
 *
 * @param <T>
 */
public interface Database<T> {
    /**
     * @param id
     * @return the number of deleted elements.
     */
    int removeGreaterOrLowerThanId(long id, boolean greater);

    long sumOfAverageMark();

    String getElementsDescending();

    Optional<Integer> CountLessThanFormOfEducation(FormOfEducation education);

    String getInfo();
    /**
     * Adds a new element and updates the last id.
     *
     * @param element
     */
    void addElement(T element);
    T createElementFromInput(Consumer<String> standardOut, Consumer<String> errorOut);
    T createElementFromString(String input, Consumer<String> standardOut, Consumer<String> errorOut);
    String getAllElements();
    T updateElementById(long id, T new_element) throws IllegalValueException;
    boolean removeElementById(long id) throws IllegalValueException;
    /**
     * Clears the collection and resets the last id counter.
     */
    void clearCollection();
    boolean removeFirstElement() throws IllegalValueException;
    String getConstructorSignature();
    boolean addIfMin(T element);

    void serialize() throws Exception;
    void deserialize() throws Exception;

    void pushToUndoStack(UndoLog<T> log);

    boolean undo();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Data
    class UndoLog<T> {
        List<ElementChange<T>> changesList;

        public static <T> UndoLog<T> addedElements(T ...elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(true, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> deletedElements(T ...elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(false, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> deletedElements(Iterable<T> elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(false, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> changedElement(T newElement, T oldElement) {
            List<ElementChange<T>> changes = new ArrayList<>();

            changes.add(new ElementChange<>(true, newElement));
            changes.add(new ElementChange<>(false, oldElement));

            return new UndoLog<>(changes);
        }


            public boolean undo(Collection<T> collection) {
            for (ElementChange<T> change : changesList) {
                boolean res;

                if (change.isAdded) {
                    res = collection.remove(change.element);
                }
                else {
                    res = collection.add(change.element);
                }

                if (!res)
                    return false;
            }


            return true;
        }
    }

    @Data
    @AllArgsConstructor
    class ElementChange<T> {
        boolean isAdded;
        T element;
    }
}
