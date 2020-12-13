package database;

import java.util.ArrayList;

public class EnrolmentLectureDataBase extends EnrolmentDataBase {
    private static final int enrolmentIndex = 3;
    private EnrolmentLectureDataBase() {};
    public static DataBase<ArrayList<String>> getDB () {
        return getDB(enrolmentIndex);
    }
}
