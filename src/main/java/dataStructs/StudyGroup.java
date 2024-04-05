package dataStructs;

import dataStructs.exceptions.NumberOutOfBoundsException;
import dataStructs.exceptions.StringIsEmptyException;
import dataStructs.exceptions.ValueIsNullException;
import database.DatabaseEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

@Data
public class StudyGroup implements DatabaseEntity{

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer studentsCount; //Значение поля должно быть больше 0, Поле может быть null

    private long averageMark; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле может быть null

    public void setName(String name) throws StringIsEmptyException {
        Fields.checkName(name);
        this.name = name;
    }
    public void setAverageMark(long value) throws NumberOutOfBoundsException {
        Fields.checkAverageMark(value);
        this.averageMark = value;
    }
    public void setStudentsCount(Integer value) throws NumberOutOfBoundsException {
        Fields.checkStudentsCount(value);
        studentsCount = value;
    }
    public StudyGroup(String name, Coordinates coordinates, Integer studentsCount, long averageMark, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) throws StringIsEmptyException, NumberOutOfBoundsException {
        setName(name);
        setCoordinates(coordinates);
        setStudentsCount(studentsCount);
        setAverageMark(averageMark);
        setFormOfEducation(formOfEducation);
        setSemesterEnum(semesterEnum);
        setGroupAdmin(groupAdmin);

        setCreationDate(LocalDate.now());
    }

    public static StudyGroup createFromInput(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
        return new StudyGroup(
                Fields.enterName(scanner, standardOut, errorOut),
                Coordinates.createFromInput(scanner, standardOut, errorOut),
                Fields.enterStudentsCount(scanner, standardOut, errorOut),
                Fields.enterAverageMark(scanner, standardOut, errorOut),
                Fields.enterFormOfEducation(scanner, standardOut, errorOut),
                Fields.enterSemesterEnum(scanner, standardOut, errorOut),
//                Fields.enterGroupAdmin(scanner));
                Person.createFromInput(scanner, standardOut, errorOut));
    }

    public String getValues(String separator) {
        String groupAdmin = "";

        if (getGroupAdmin() == null) {
            groupAdmin = null;
        }
        else {
            groupAdmin = getGroupAdmin().getValues(separator);
        }

        return getName() + separator +
                getCoordinates().getValues(separator) + separator +
                getStudentsCount() + separator +
                getAverageMark() + separator +
                getFormOfEducation() + separator +
                getSemesterEnum() + separator +
                groupAdmin;
    }

    @Override
    public void checkValues() throws RuntimeException {
        Fields.checkName(getName());
        Fields.checkAverageMark(getAverageMark());
        Fields.checkStudentsCount(getStudentsCount());
        Fields.checkCreationDate(getCreationDate());
        Optional.of(getCoordinates()).ifPresentOrElse(
                Coordinates::checkValues,
                () -> {throw new RuntimeException("coordinates is null");});

        Optional.of(getGroupAdmin()).ifPresent(Person::checkValues);
    }

    public static class Fields {
        private final static String NAME_IS_EMPTY_MESSAGE = "Name of the group can't be empty!";
        private final static String AVERAGE_MARK_OUT_OF_BOUNDS_MESSAGE = "Average mark should be greater than zero!";
        private final static String STUDENT_COUNT_OUT_OF_BOUNDS_MESSAGE = "Student count should be greater than zero!";
        public static void checkName(String name) throws StringIsEmptyException {
            if (name == null || name.isBlank()) {
                throw new StringIsEmptyException(NAME_IS_EMPTY_MESSAGE);
            }
        }
        public static void checkAverageMark(long value) throws NumberOutOfBoundsException{
            if (value <= 0) {
                throw new NumberOutOfBoundsException(AVERAGE_MARK_OUT_OF_BOUNDS_MESSAGE);
            }
        }
        public static void checkStudentsCount(Integer value) throws NumberOutOfBoundsException {
            if (value == null) {
                return;
            }

            if (value <= 0) {
                throw new NumberOutOfBoundsException(STUDENT_COUNT_OUT_OF_BOUNDS_MESSAGE);
            }
        }

        public static String enterName(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
            standardOut.accept("Enter name: (String)");

            try {
                String in = scanner.nextLine();
                checkName(in);

                return in;

            }
            catch (StringIsEmptyException e) {
                errorOut.accept(NAME_IS_EMPTY_MESSAGE);
            }

            return enterName(scanner, standardOut, errorOut);
        }

        public static Integer enterStudentsCount(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
            standardOut.accept("Enter students count (int or null): ");
            String line = scanner.nextLine();
            if (line.equals("null")) {
                return null;
            }

            try {
                int in = Integer.parseInt(scanner.nextLine()) ;
                checkStudentsCount(in);

                return in;
            }
            catch (NumberOutOfBoundsException e) {
                errorOut.accept(STUDENT_COUNT_OUT_OF_BOUNDS_MESSAGE);
            }
            catch (NumberFormatException e) {
                errorOut.accept(e.getMessage());
            }

            return enterStudentsCount(scanner, standardOut, errorOut);
        }
        public static long enterAverageMark(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
            standardOut.accept("Enter average mark (long): ");

            try {
                long in = Long.parseLong(scanner.nextLine());
                checkAverageMark(in);

                return in;
            }
            catch (NumberOutOfBoundsException e) {
                errorOut.accept(AVERAGE_MARK_OUT_OF_BOUNDS_MESSAGE);
            }
            catch (NumberFormatException e) {
                errorOut.accept(e.getMessage());
            }

            return enterAverageMark(scanner, standardOut, errorOut);
        }
        public static String getEnumOptions(Class<? extends Enum<?>> clazz) {
            Iterator<? extends Enum<?>> iterator = Arrays.stream(clazz.getEnumConstants()).iterator();

            StringBuilder builder = new StringBuilder();

            while (iterator.hasNext()) {
                builder.append(iterator.next().name());
                if (iterator.hasNext()) {
                    builder.append(" | ");
                }
            }

            return builder.toString();
        }
        public static FormOfEducation enterFormOfEducation(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
            standardOut.accept("Enter form of education: (FormOfEducation or null)");
            standardOut.accept("Choose one of following: " + getEnumOptions(FormOfEducation.class) + " | null");

            String line = scanner.nextLine();
            if (line.equals("null")) {
                return null;
            }

            try {
                String in = scanner.nextLine();
                return FormOfEducation.valueOf(in);
            }
            catch (IllegalArgumentException e) {
                errorOut.accept(e.getMessage());
                return enterFormOfEducation(scanner, standardOut, errorOut);
            }
        }
        public static Semester enterSemesterEnum(Scanner scanner, Consumer<String> standardOut, Consumer<String> errorOut) {
            standardOut.accept("Enter semester: (Semester or null)");
            standardOut.accept("Choose one of following: " + getEnumOptions(Semester.class) + " | null");

            String line = scanner.nextLine();
            if (line.equals("null")) {
                return null;
            }
            try {
                String in = scanner.nextLine();
                return Semester.valueOf(in);
            }
            catch (IllegalArgumentException e) {
                errorOut.accept(e.getMessage());
                return enterSemesterEnum(scanner, standardOut, errorOut);
            }
        }

        public static void checkCreationDate(LocalDate creationDate) {
            if (creationDate == null) {
                throw new ValueIsNullException(StudyGroup.class, "Creation date can't be null!");
            }
        }
    }
}