package database;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dataStructs.*;
import dataStructs.exceptions.IllegalValueException;
import dataStructs.exceptions.KeyNotFoundException;
import dataStructs.exceptions.NumberOutOfBoundsException;
import dataStructs.exceptions.ValueIsNullException;
import database.exceptions.PrimaryKeyIsNotUnique;
import lombok.Getter;
import parser.FileParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class StudyGroupDatabase extends CollectionDatabase<StudyGroup> {

    private long lastId = 0;

    private final FileParser<StudyGroupList> parser;

    /**
     * Clears the collection and resets the last id counter.
     */
    @Override
    public void clearCollection() {
        super.clearCollection();
        lastId = 0;
    }

    /**
     *
     * @param id
     * @return the number of deleted elements.
     */
    @Override
    public int removeGreater(long id) {
        int prevSize = getCollection().size();

        getCollection().removeIf(group -> group.getId() > id);

        int newSize = getCollection().size();

        return prevSize - newSize;
    }

    @Override
    public int removeLower(long id) {
        int prevSize = getCollection().size();

        getCollection().removeIf(group -> group.getId() < id);

        int newSize = getCollection().size();

        return prevSize - newSize;
    }

    @Override
    public long sumOfAverageMark() {
        long sum = 0;

        for (StudyGroup group : getCollection()) {
            sum += group.getAverageMark();
        }

        return sum;
    }

    @Override
    public String getElementsDescending() {
        return getCollection().stream()
                .sorted(((o1, o2) -> (int) (o2.getId() - o1.getId())))
                //by default its first minus second, so to reverse order we should switch their places
                .map(StudyGroup::toString)
                .map(group -> group + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public Optional<Integer> CountLessThanFormOfEducation(FormOfEducation formOfEducation) {
        if (getCollection().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((int)getCollection().stream()
                .filter(group -> group.getFormOfEducation().ordinal() < formOfEducation.ordinal())
                .count());
    }

    @Override
    public String getInfo() {

        StringBuilder builder = new StringBuilder(super.getInfo());

        Path pathToFile = parser.getFilePath();

        if (!Files.exists(pathToFile)) {
            builder.append("\nFile problem: file doesn't exists.");

            return builder.toString();
        }

        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(pathToFile, BasicFileAttributes.class);
        } catch (IOException e) {
            builder.append("\nFile problem: ").append(e.getMessage());

            return builder.toString();
        }

        builder.append("Initialization date: ").append(Date.from(attributes.creationTime().toInstant()))
                .append("\nLast update date: ").append(Date.from(attributes.lastModifiedTime().toInstant()));

        return builder.toString();
    }
    public StudyGroupDatabase(EntityBuilder<StudyGroup> builder, FileParser<StudyGroupList> parser) {

        super(new ArrayDeque<>(), builder);
        this.parser = parser;

        try {
            deserialize();
            getCollection().forEach(group -> {
                try {
                    group.checkValues();
                }catch (RuntimeException e) {
                    System.err.println("In element with id: " + group.getId());
                    throw e;
                }
            });

            lastId = checkAllUnique() + 1;

        } catch (IOException | RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    public ArrayDeque<StudyGroup> getArrayDequeCollection() {
        return (ArrayDeque<StudyGroup>)getCollection();
    }

    public StudyGroup updateElementById( long id, StudyGroup new_element) throws IllegalValueException {
        if (!removeElementById(id)) {
            throw new IllegalValueException("Can't find an element with this id!");
        }

        new_element.setId(id);
        getCollection().add(new_element);

        return new_element;
    }

    @Override
    public boolean removeElementById(long id) throws IllegalValueException {
        return getCollection().removeIf(group -> group.getId() == id);
    }

    @Override
    public void removeFirstElement() throws IllegalValueException {
        getArrayDequeCollection().removeFirst();
    }
    @Override
    public boolean addIfMin(StudyGroup group) {
        Optional<StudyGroup> res =  getCollection()
                .stream()
                .min(Comparator.comparingInt(StudyGroup::getStudentsCount));

        if (res.isEmpty() || res.get().getStudentsCount() > group.getStudentsCount()) {
            addElement(group);
            return true;
        }
        else {
            return false;
        }
    }

    public String getConstructorSignature() {
        return "(name: String, " +
                "coordinates.x :double, " +
                "coordinates.y: double, " +
                "studentsCount (null): int, " +
                "averageMark: long, " +
                "formOfEducation: FormOfEducation, " +
                "semesterEnum: FormOfEducation, " +
                "groupAdmin (null): " +
                "groupAdmin.name: String, " +
                "groupAdmin.birthday: String, " +
                "groupAdmin.nationality: Country, " +
                "groupAdmin.location.x: int, " +
                "groupAdmin.location.y: float, " +
                "groupAdmin.location.name: String)";
    }
    public void serialize() throws IOException {
        parser.serializeIntoFile(new StudyGroupList(getCollection()));
    }

    public void deserialize() throws IOException, RuntimeException {
        StudyGroupList list = parser.deserializeFromFile();
        if (list == null) {
            System.err.println("File is empty!");
            System.out.println("Created empty database... Type \"exit\" to quit without changes");
            return;
        }

        getCollection().addAll(list.getStudyGroupList());

    }

    /**
     * Goes through all elements in collection and checks if they are unique
     *
     * @return max id in collection
     * @throws PrimaryKeyIsNotUnique exception if there is two same id's
     */
    public long checkAllUnique() throws PrimaryKeyIsNotUnique {
        long maxId = 0;

        HashSet<Long> setOfUsedIds = new HashSet<>();

        for (StudyGroup group : getCollection()) {
            if (!setOfUsedIds.add(group.getId())) {
                throw new PrimaryKeyIsNotUnique("There is not unique id (" + group.getId() + ") in database file");
            }

            if (group.getId() > maxId) {
                maxId = group.getId();
            }
        }

        return maxId;
    }

    /**
     * Adds a new element and updates the last id.
     *
     * @param group
     */
    @Override
    public void addElement(StudyGroup group) {
        super.addElement(group);
        group.setId(lastId++);
    }
}
