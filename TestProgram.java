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

/*
* 개발을 진행하며 구현한 기능들을 테스트하기 위해 사용한 클래스 입니다.
* 테스트를 진행하던 시기와 프로젝트 제출 시기의 데이터베이스 상태가 달라
* 메소드를 호출해도 원활히 실행되지 않을 가능성이 높습니다.
*/

public class TestProgram {
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

        // 강의 등록 테스트
        Lecture lec = DB_lecture.selectOrNull("20FGHE");
        assert em.enrolLectureFromProfessor("123456778", lec) == EEnrolmentState.FAIL_ENROLLED_LECTURE;

        ArrayList<ClassTime> cts = new ArrayList<>();
        cts.add(new ClassTime(EDay.TUE, 9, 11));
        cts.add(new ClassTime(EDay.THU, 9, 11));

        lec = new Lecture("201422949", DB_ajouin.selectOrNull("201422949").getName(), DB_subject.selectOrNull("F071"), 3, 30, "원999", cts);
        assert em.enrolLectureFromProfessor("201422949", lec) == EEnrolmentState.FAIL_WRONG_IDENTITY_ID;

        lec = new Lecture("20081588", DB_ajouin.selectOrNull("20081588").getName(), DB_subject.selectOrNull("C025"), 3, 30, "원999", cts);
        assert em.enrolLectureFromProfessor("123456778", lec) == EEnrolmentState.FAIL_INVALID_ID;

        assert em.enrolLectureFromProfessor("20081588", lec) == EEnrolmentState.SUCCESS;

        lec = new Lecture("20081588", DB_ajouin.selectOrNull("20081588").getName(), DB_subject.selectOrNull("C025"), 3, 30, "원999", cts);
        assert em.enrolLectureFromProfessor("20081588", lec) == EEnrolmentState.FAIL_OVERLAP_CLASS_TIME;

        // 20FKRE,20051423,C015,3,60,원432,월12,월14,수12,수14
        ArrayList<ClassTime> cts2 = new ArrayList<>();
        cts2.add(new ClassTime(EDay.MON, 12, 14));
        cts2.add(new ClassTime(EDay.WED, 12, 14));
        lec = new Lecture("20091548", DB_ajouin.selectOrNull("20091548").getName(), DB_subject.selectOrNull("C015"), 5, 40, "원432", cts2);
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.clear();
        cts2.add(new ClassTime(EDay.MON, 10, 12));
        cts2.add(new ClassTime(EDay.WED, 12, 14));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.clear();
        cts2.add(new ClassTime(EDay.MON, 11, 13));
        cts2.add(new ClassTime(EDay.WED, 10, 12));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.clear();
        cts2.add(new ClassTime(EDay.MON, 1, 2));
        cts2.add(new ClassTime(EDay.WED, 11, 13));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.clear();
        cts2.add(new ClassTime(EDay.MON, 1, 2));
        cts2.add(new ClassTime(EDay.WED, 11, 15));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.add(new ClassTime(EDay.MON, 1, 2));
        cts2.add(new ClassTime(EDay.WED, 14, 15));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.add(new ClassTime(EDay.MON, 13, 15));
        cts2.add(new ClassTime(EDay.WED, 11, 15));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        cts2.clear();
        cts2.add(new ClassTime(EDay.MON, 9, 11));
        cts2.add(new ClassTime(EDay.WED, 15, 16));
        assert em.enrolLectureFromProfessor("20091548", lec) == EEnrolmentState.SUCCESS;
        // 동일한 시간 다른 장소 => 성공
        cts2 = new ArrayList<>();
        cts2.add(new ClassTime(EDay.MON, 10, 12));
        cts2.add(new ClassTime(EDay.WED, 14, 15));
        lec = new Lecture("20138469", DB_ajouin.selectOrNull("20138469").getName(), DB_subject.selectOrNull("C015"), 5, 40, "팔888", cts2);
        assert em.enrolLectureFromProfessor("20138469", lec) == EEnrolmentState.SUCCESS;

        // 강의 삭제 테스트
        assert em.cancelLectureFromProfessor("20138469", "DKSIEJF") == EEnrolmentState.FAIL_INVALID_ID;
        assert em.cancelLectureFromProfessor("2013846", lec.getLectureId()) == EEnrolmentState.FAIL_INVALID_ID;
        assert em.cancelLectureFromProfessor("201520659", lec.getLectureId()) == EEnrolmentState.FAIL_WRONG_IDENTITY_ID;

        assert em.cancelLectureFromProfessor("20138469", "20DFBD") == EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;
        assert em.cancelLectureFromProfessor("20138469", lec.getLectureId()) == EEnrolmentState.SUCCESS;
        assert em.cancelLectureFromProfessor("20138469", lec.getLectureId()) == EEnrolmentState.FAIL_INVALID_ID;
        assert em.cancelLectureFromProfessor("20081588", "20APQZ") == EEnrolmentState.SUCCESS;


        // 수강 신청 & 취소 테스트
        assert em.enrolLectureFromStudent("20199900", "20DBFZ") == EEnrolmentState.FAIL_INVALID_ID;
        assert em.enrolLectureFromStudent("201520659", "18DBFZ") == EEnrolmentState.FAIL_INVALID_ID;
        assert em.enrolLectureFromStudent("20132148", "20DBFZ") == EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        assert em.enrolLectureFromStudent("201520659", "20DBFZ") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201520659", "20DBFZ") == EEnrolmentState.FAIL_ENROLLED_LECTURE;
        assert em.enrolLectureFromStudent("201520659", "20JNHJ") == EEnrolmentState.FAIL_OVERLAP_CLASS_TIME;
        assert em.enrolLectureFromStudent("201520659", "20FGNA") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201520659", "20NFTB") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201520659", "20BFBD") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201520659", "20SVDC") == EEnrolmentState.SUCCESS;

        assert em.enrolLectureFromStudent("201520659", "20FYZE") == EEnrolmentState.SUCCESS;

        assert em.cancelLectureFromStudent("20142294", "20FYZE") == EEnrolmentState.FAIL_INVALID_ID;
        assert em.cancelLectureFromStudent("201422949", "10ZZZZ") == EEnrolmentState.FAIL_INVALID_ID;
        assert em.cancelLectureFromStudent("20091548", "20FYZE") == EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        assert em.cancelLectureFromStudent("201520659", "20DZSW") == EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;
        assert em.cancelLectureFromStudent("201422949", "20FYZE") == EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;

        assert em.enrolLectureFromStudent("201422949", "20FYZE") == EEnrolmentState.FAIL_NO_MORE_REMAIN_SEAT;
        assert em.cancelLectureFromStudent("201520659", "20FYZE") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201422949", "20FYZE") == EEnrolmentState.SUCCESS;
        assert em.enrolLectureFromStudent("201520659", "20FYZE") == EEnrolmentState.FAIL_NO_MORE_REMAIN_SEAT;

        // total credit => 15  /  max credit => 19  /  20LHGG credit = > 5
        assert em.enrolLectureFromStudent("201520659", "20LHGG") == EEnrolmentState.FAIL_NO_MORE_CREDIT;
    }
}
