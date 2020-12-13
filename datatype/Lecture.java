package datatype;

import database.DataBase;
import database.EnrolmentLectureDataBase;

import java.util.ArrayList;

public class Lecture implements Cloneable, ICloneable<Lecture> {
    private final String lectureId;
    private final String professorId;
    private final String professorName;
    private final Subject subject;
    private int credit;             // 학점
    private int seatsLimit;
    private String classroom;
    private ArrayList<ClassTime> classTimes;

    public Lecture(String lectureId, String professorId, String professorName, Subject subject,
                   int credit, int seatsLimit, String classroom, ArrayList<ClassTime> classTimes) {
        assert credit > 0 : "datatype.Lecture credit must be higher than 0";
        assert seatsLimit > 0 : "datatype.Lecture credit must be higher than 0";

        this.lectureId = lectureId;
        this.professorId = professorId;
        this.professorName = professorName;
        this.subject = subject;
        this.credit = credit;
        this.seatsLimit = seatsLimit;
        this.classroom = classroom;
        this.classTimes = classTimes;
    }


    public void setSeatsLimit(int seatsLimit) {
        assert seatsLimit > 0 : "datatype.Lecture credit must be higher than 0";
        this.seatsLimit = seatsLimit;
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

    public int getSeatsLimit() {
        return seatsLimit;
    }

    public String getClassroom() {
        return classroom;
    }

    public int getRemainingSeats() {
        DataBase<ArrayList<String>> enrolDB = EnrolmentLectureDataBase.getDB();
        ArrayList<String> students = enrolDB.selectOrNull(this.lectureId);
        return this.seatsLimit - students.size();
    }

    @Override    // ICloneable<Lecture>
    public Lecture clone(){
        Lecture lectureClone = null;

        try {
            lectureClone = (Lecture) super.clone();   // Object clone() => Cloneable
        } catch (CloneNotSupportedException e) {
            assert false: "Fail lecture.clone()";
        }

        lectureClone.classTimes = (ArrayList<ClassTime>) this.classTimes.clone();

        return lectureClone;
    }
}
