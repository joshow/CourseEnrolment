package datatype;

import java.util.ArrayList;

public class Lecture implements Cloneable {
    private final String lectureId;
    private final String professorId;
    private final String professorName;
    private final Subject subject;
    private int credit;             // 학점
    private int maxEnrolCount;
    private String classroom;
    private ArrayList<ClassTime> classTimes;
    private final ArrayList<Ajouin> students = new ArrayList<>();  // TODO: DataBase에서 등록 학생 정보 가져오기

    public Lecture(String lectureId, String professorId, String professorName, Subject subject,
                   int credit, int maxEnrolCount, String classroom, ArrayList<ClassTime> classTimes) {
        assert credit > 0 : "datatype.Lecture credit must be higher than 0";
        assert maxEnrolCount > 0 : "datatype.Lecture credit must be higher than 0";

        this.lectureId = lectureId;
        this.professorId = professorId;
        this.professorName = professorName;
        this.subject = subject;
        this.credit = credit;
        this.maxEnrolCount = maxEnrolCount;
        this.classroom = classroom;
        this.classTimes = classTimes;
    }

    public void setMaxEnrolCount(int maxEnrolCount) {
        assert maxEnrolCount > 0 : "datatype.Lecture credit must be higher than 0";
        this.maxEnrolCount = maxEnrolCount;
    }

    public boolean addClassTime(ClassTime classTime) {
        for (ClassTime ct : classTimes) {
            if (ct.isOverlapTime(classTime)) {
                return false;
            }
        }

        classTimes.add(classTime);
        return true;
    }

    public boolean removeClassTime(ClassTime classTime) {
        for (int i = 0; i < classTimes.size(); ++i) {
            if (classTimes.get(i) == classTime) {
                classTimes.remove(i);
                return true;
            }
        }

        return false;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getLectureId() {
        return lectureId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public ArrayList<ClassTime> getClassTimes() {
        return classTimes;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getCredit() {
        return credit;
    }

    public int getMaxEnrolCount() {
        return maxEnrolCount;
    }

    public String getClassroom() {
        return classroom;
    }

    public ArrayList<Ajouin> getStudents() {
        return students;
    }

    @Override
    public Object clone(){
        Lecture lectureClone = null;

        try {
            lectureClone = (Lecture) super.clone();
        } catch (CloneNotSupportedException e) {
            assert false: "Fail lecture.clone()";
        }

        lectureClone.classTimes = (ArrayList<ClassTime>) this.classTimes.clone();

        return lectureClone;
    }
}
