package database;

public abstract class DataBase {
    public abstract boolean hasKey(String uniqueKey);

    public boolean updateElement(String uniqueKey, String elementCode, String element) {
        return false;
    }

    public abstract boolean delete(String uniqueKey);
}
