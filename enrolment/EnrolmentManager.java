package enrolment;

import database.AjouinDataBase;
import database.DataBase;
import database.EnrolmentLectureDataBase;
import database.EnrolmentProfessorDataBase;
import database.EnrolmentStudentDataBase;
import database.LectureDataBase;
import datatype.Ajouin;
import datatype.EAjouinIdentity;
import datatype.Lecture;
import datatype.Professor;
import datatype.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class EnrolmentManager {
    private final DataBase<Ajouin> DB_AJOUIN;
    private final DataBase<Lecture> DB_LECTURE;
    private final DataBase<ArrayList<String>> DB_ENROL_STUDENT;
    private final DataBase<ArrayList<String>> DB_ENROL_PROFESSOR;
    private final DataBase<ArrayList<String>> DB_ENROL_LECTURE;

    public EnrolmentManager() {
        DB_AJOUIN = AjouinDataBase.getDB();
        DB_LECTURE = LectureDataBase.getDB();
        DB_ENROL_STUDENT = EnrolmentStudentDataBase.getDB();
        DB_ENROL_PROFESSOR = EnrolmentProfessorDataBase.getDB();
        DB_ENROL_LECTURE = EnrolmentLectureDataBase.getDB();
    }

    // 수강 등록
    public EEnrolmentState enrolLectureFromStudent(String studentId, String lectureId) {
        Lecture lecture = DB_LECTURE.selectOrNull(lectureId);
        if (lecture == null) {    // 없는 과목 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        }

        Ajouin ajouin = DB_AJOUIN.selectOrNull(studentId);
        if (ajouin == null) {    // 없는 아주인 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        } else if (ajouin.getIdentity() != EAjouinIdentity.STUDENT) {  // 학생의 ID가 아닌 경우
            return EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        }

        Student student = (Student) ajouin;

        ArrayList<String> enrolledLectures = DB_ENROL_STUDENT.selectOrNull(studentId);
        if (enrolledLectures == null) {
            enrolledLectures = new ArrayList<>();
        } else {
            // 학점이 부족하지 않은지 확인 & 이미 신청한 강의인지 & 본인 시간표와 겹치는지 확인
            int totalCredit = 0;
            for (String enrolledLectureId : enrolledLectures) {
                if (lecture.getLectureId().equals(enrolledLectureId)) {
                    return EEnrolmentState.FAIL_ENROLLED_LECTURE;
                }

                Lecture enrolledLecture = DB_LECTURE.selectOrNull(enrolledLectureId);
                assert enrolledLecture != null;
                if (enrolledLecture.isOverlapClassTime(lecture)) {
                    return EEnrolmentState.FAIL_OVERLAP_CLASS_TIME;
                }
                totalCredit += enrolledLecture.getCredit();
            }

            if (totalCredit + lecture.getCredit() > Student.MAX_CREDIT) {
                return EEnrolmentState.FAIL_NO_MORE_CREDIT;
            }
        }

        // 여석이 남았는지 확인
        ArrayList<String> enrolledStudents = DB_ENROL_LECTURE.selectOrNull(lectureId);
        assert enrolledStudents != null;
        if (lecture.getSeatsLimit() <= enrolledStudents.size()) {
            return EEnrolmentState.FAIL_NO_MORE_REMAIN_SEAT;
        }

        // 학생이 수강신청한 강의 목록을 갱신
        enrolledLectures.add(lectureId);
        if (enrolledLectures.size() == 1) {     // 처음 등록하는 강의인 경우
            DB_ENROL_STUDENT.put(studentId, enrolledLectures);
        } else {
            DB_ENROL_STUDENT.updateCSV();
        }

        // 강의에 수강신청한 학생 목록을 갱신
        enrolledStudents.add(studentId);
        DB_ENROL_LECTURE.updateCSV();

        return EEnrolmentState.SUCCESS;
    }

    // 수강 취소
    public EEnrolmentState cancelLectureFromStudent(String studentId, String lectureId) {
        Lecture lecture = DB_LECTURE.selectOrNull(lectureId);
        if (lecture == null) {    // 없는 과목 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        }

        Ajouin ajouin = DB_AJOUIN.selectOrNull(studentId);
        if (ajouin == null) {    // 없는 아주인 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        } else if (ajouin.getIdentity() != EAjouinIdentity.STUDENT) {  // 학생의 ID가 아닌 경우
            return EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        }

        Student student = (Student) ajouin;

        ArrayList<String> enrolledLectures = DB_ENROL_STUDENT.selectOrNull(studentId);
        // 학생이 해당 강의를 신청하지 않은 상태인 경우
        if (enrolledLectures == null) {
            return EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;
        } else if (!enrolledLectures.contains(lectureId)) {
            return EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;
        }

        // 학생이 수강신청한 강의 목록을 갱신한다.
        enrolledLectures.remove(lectureId);
        DB_ENROL_STUDENT.updateCSV();

        // 강의에 수강신청한 학생 목록을 갱신
        ArrayList<String> enrolledStudents = DB_ENROL_LECTURE.selectOrNull(lectureId);
        assert enrolledStudents != null;
        enrolledStudents.remove(studentId);
        DB_ENROL_LECTURE.updateCSV();

        return EEnrolmentState.SUCCESS;
    }

    // 강의 등록
    public EEnrolmentState enrolLectureFromProfessor(String professorId, Lecture lecture) {
        if (lecture.isValid()) {    // 이미 유효한 강의라면 등록 되어있는 강의이다.
            return EEnrolmentState.FAIL_ENROLLED_LECTURE;
        }

        Ajouin ajouin = DB_AJOUIN.selectOrNull(professorId);
        if (ajouin == null) {    // 없는 아주인 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        } else if (ajouin.getIdentity() != EAjouinIdentity.PROFESSOR) {  // 교수의 ID가 아닌 경우
            return EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        }
        Professor professor = (Professor) ajouin;

        ArrayList<String> enrolledLectures = DB_ENROL_PROFESSOR.selectOrNull(professorId);
        if (enrolledLectures == null) {     // 처음 강의를 등록하는 경우
            enrolledLectures = new ArrayList<>();
        } else {
            // 교수의 기존 강의 시간과 겹치지 않는지 확인
            for (String enrolledLectureId : enrolledLectures) {
                if (DB_LECTURE.selectOrNull(enrolledLectureId).isOverlapClassTime(lecture)) {
                    return EEnrolmentState.FAIL_OVERLAP_CLASS_TIME;
                }
            }
        }

        if (!LectureDataBase.canAdd(lecture)) {
            return EEnrolmentState.FAIL_OVERLAP_CLASSROOM;
        }

        // 새로운 강의로 등록
        String id = generateLectureId();
        lecture.setLectureId(id);
        DB_LECTURE.put(id, lecture);

        // 교수가 등록한 강의 목록을 갱신
        enrolledLectures.add(id);
        if (enrolledLectures.size() == 1) {     // 처음 등록하는 강의인 경우
            DB_ENROL_PROFESSOR.put(id, enrolledLectures);
        } else {
            DB_ENROL_PROFESSOR.updateCSV();
        }

        // 강의에 수강신청한 학생 목록 테이블 생성
        DB_ENROL_LECTURE.put(id, new ArrayList<>());

        return EEnrolmentState.SUCCESS;
    }

    // 강의 삭제
    public EEnrolmentState cancelLectureFromProfessor(String professorId, String lectureId) {
        Lecture lecture = DB_LECTURE.selectOrNull(lectureId);
        if (lecture == null) {    // 없는 과목 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        }

        Ajouin ajouin = DB_AJOUIN.selectOrNull(professorId);
        if (ajouin == null) {    // 없는 아주인 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        } else if (ajouin.getIdentity() != EAjouinIdentity.PROFESSOR) {  // 교수의 ID가 아닌 경우
            return EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        }
        Professor professor = (Professor) ajouin;

        ArrayList<String> enrolledLectures = DB_ENROL_PROFESSOR.selectOrNull(professorId);
        if (enrolledLectures == null || !enrolledLectures.contains(lectureId)) {
            return EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;            // 교수가 등록하지 않은 강의의 경우
        }

        // 강의 DB에서 강의를 제거
        DB_LECTURE.delete(lectureId);

        // 교수가 등록한 강의 목록을 제거
        enrolledLectures.remove(lectureId);
        if (enrolledLectures.size() == 0) {
            DB_ENROL_PROFESSOR.delete(professorId);
        } else {
            DB_ENROL_PROFESSOR.put(professorId, enrolledLectures);
        }

        // 학생들의 신청한 강의 목록에서 제거
        ArrayList<String> enrolledStudents = DB_ENROL_LECTURE.selectOrNull(lectureId);
        assert enrolledStudents != null;
        for (String studentId : enrolledStudents) {
            ArrayList<String> studentEnrolledLectures = DB_ENROL_STUDENT.selectOrNull(studentId);
            studentEnrolledLectures.remove(lectureId);
            DB_ENROL_STUDENT.put(studentId, studentEnrolledLectures);
        }

        // 강의에 수강신청한 학생 목록 테이블을 제거
        DB_ENROL_LECTURE.delete(lectureId);

        return EEnrolmentState.SUCCESS;
    }

    // 강의 수정
    // 메소드 호출 전 DB에서 기존 Lecture 객체를 불러와 수정한 뒤 메소드의 매개변수로 사용한다.
    public EEnrolmentState editLectureFromProfessor(String professorId, Lecture editedLecture) {
        if (!editedLecture.isValid()) {
            return EEnrolmentState.FAIL_INVALID_LECTURE;
        }

        Ajouin ajouin = DB_AJOUIN.selectOrNull(professorId);
        if (ajouin == null) {    // 없는 아주인 ID인 경우
            return EEnrolmentState.FAIL_INVALID_ID;
        } else if (ajouin.getIdentity() != EAjouinIdentity.PROFESSOR) {  // 교수의 ID가 아닌 경우
            return EEnrolmentState.FAIL_WRONG_IDENTITY_ID;
        }
        Professor professor = (Professor) ajouin;

        if (!professorId.equals(editedLecture.getProfessorId())) {    // 해당 교수가 등록한 강의가 아닌 경우
            return EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE;
        }

        DB_LECTURE.put(professorId, editedLecture);

        return EEnrolmentState.SUCCESS;
    }

    private String generateLectureId() {
        String lectureId;

        do {
            StringBuilder sb = new StringBuilder(6);
            sb.append(LocalDate.now().getYear() % 100);

            Random random = new Random();
            for (int i = 0; i < 4; ++i) {
                sb.append(Character.toChars(random.nextInt(26) + 'A'));
            }

            lectureId = sb.toString();
        } while (DB_LECTURE.selectOrNull(lectureId) != null);

        return lectureId;
    }
}
