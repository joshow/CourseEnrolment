package database;

import java.util.ArrayList;

public class EnrolmentStudentDataBase extends EnrolmentDataBase {
    private static final int enrolmentIndex = 0;
    private EnrolmentStudentDataBase() {};
    public static DataBase<ArrayList<String>> getDB () {
        return getDB(enrolmentIndex);
    }
}
