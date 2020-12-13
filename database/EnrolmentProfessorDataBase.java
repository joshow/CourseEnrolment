package database;

import java.util.ArrayList;

public class EnrolmentProfessorDataBase extends EnrolmentDataBase {
    private static final int enrolmentIndex = 1;
    private EnrolmentProfessorDataBase() {};
    public static DataBase<ArrayList<String>> getDB () {
        return getDB(enrolmentIndex);
    }
}
