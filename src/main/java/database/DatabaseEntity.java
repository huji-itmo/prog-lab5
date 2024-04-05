package database;

public interface DatabaseEntity {
    String getValues(String separator);
    void checkValues() throws RuntimeException;
}
