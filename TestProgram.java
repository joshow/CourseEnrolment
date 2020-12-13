import database.AjouinDataBase;
import database.DataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Ajouin;
import datatype.ClassTime;
import datatype.EDay;
import datatype.Lecture;
import datatype.Subject;
import enrolment.EEnrolmentState;
import enrolment.EnrolmentManager;

import java.util.ArrayList;

public class TestProgram {
    public static void main(String[] args) {
        //TestDataBase();
        TestEnrolmentManager();
    }

    public static void TestDataBase() {
        // DataBase Example
        DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        Ajouin ajouin = DB_ajouin.selectOrNull("201520659");
        System.out.println(ajouin.getName() + ", " + ajouin.getMajor());

        DataBase<Lecture> DB_lecture = LectureDataBase.getDB();
        Lecture lecture = DB_lecture.selectOrNull("20ZZZZ");
        if (lecture == null) {
            System.out.println("database has not key '20ZZZZ'");
            if (!DB_lecture.hasKey("20ZZZZ")) {
                System.out.println("must be false");
            }
        }

        lecture = DB_lecture.selectOrNull("20NFNG");
        if (lecture != null) {
            System.out.println("database has key '20NFNG'");
            if (!DB_lecture.hasKey("20ZZZZ")) {
                System.out.println("must be false");
            }

            String professorid = lecture.getProfessorId();

            if (DB_ajouin.hasKey(professorid)) {
                System.out.println("must be true");
            }

            System.out.println("Lecture 20NFNG's professor is " + DB_ajouin.selectOrNull(professorid).getName());
        }

        // How to login
        if (AjouinDataBase.isValidLoginInfo("20142348", "invalid PW") == false)
            System.out.println("Login Fail");
        if (AjouinDataBase.isValidLoginInfo("invalid ID", "woonglife") == false)
            System.out.println("Login Fail");
        if (AjouinDataBase.isValidLoginInfo("20142348", "woonglife") == true)
            System.out.println("Login Success!");

        // Subject DB Test
        DataBase<Subject> DB_subject = SubjectDataBase.getDB();
        Subject subj = DB_subject.selectOrNull("F071");
        System.out.println(subj.getName() + " " + subj.getCode() + " " + subj.getClassification() + " " + subj.getMajorDepartments().get(0));

        // Delete Test
        DB_ajouin.delete("201520659");
        DB_lecture.delete("20NFNG");
        DB_subject.delete("F071");
    }

    private static void TestEnrolmentManager() {
        EnrolmentManager em = new EnrolmentManager();
        DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        DataBase<Subject> DB_subject = SubjectDataBase.getDB();
        DataBase<Lecture> DB_lecture = LectureDataBase.getDB();

        Lecture lec = DB_lecture.selectOrNull("20FGHE");
        assert em.enrolLectureFromProfessor("123456778", lec) == EEnrolmentState.FAIL_ENROLLED_LECTURE;

        ArrayList<ClassTime> cts = new ArrayList<>();
        cts.add(new ClassTime(EDay.MON, 9, 11));
        cts.add(new ClassTime(EDay.WED, 9, 11));

        lec = new Lecture("201422949", DB_ajouin.selectOrNull("201422949").getName(), DB_subject.selectOrNull("F071"), 3, 30, "원999", cts);
        assert em.enrolLectureFromProfessor("201422949", lec) == EEnrolmentState.FAIL_WRONG_IDENTITY_ID;

        lec = new Lecture("20081588", DB_ajouin.selectOrNull("20081588").getName(), DB_subject.selectOrNull("C025"), 3, 30, "원999", cts);
        assert em.enrolLectureFromProfessor("123456778", lec) == EEnrolmentState.FAIL_INVALID_ID;

        assert em.enrolLectureFromProfessor("20081588", lec) == EEnrolmentState.SUCCESS;

        // TODO: 강의 등록 중 FAIL_OVERLAP_CLASSROOM 테스트
        // TODO: 강의 취소 및 수강신청/취소 테스트
    }


}
