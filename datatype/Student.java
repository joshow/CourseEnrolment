package datatype;

import database.DataBase;
import database.EnrolmentStudentDataBase;
import database.LectureDataBase;

import java.util.ArrayList;

public class Student extends Ajouin {
    public final static int MAX_CREDIT = 19;

    public Student(String id, String name, EDepartment major) {
        super(id, name, major, EAjouinIdentity.STUDENT);
    }

    public int getEnrolledCredit() {
        ArrayList<String> enrolledLectureId = EnrolmentStudentDataBase.getDB().selectOrNull(this.getId());

        if (enrolledLectureId == null) {
            return 0;
        }

        DataBase<Lecture> DB_LECTURE = LectureDataBase.getDB();
        int totalCredit = 0;
        for (String lecId : enrolledLectureId) {
            totalCredit += DB_LECTURE.selectOrNull(lecId).getCredit();
        }

        return totalCredit;
    }
}
