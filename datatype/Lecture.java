package datatype;

import database.LectureDataBase;

import java.util.ArrayList;

public class Lecture implements Cloneable, ICloneable<Lecture> {
    private String lectureId;
    private final String professorId;
    private final String professorName;
    private final Subject subject;
    private final int credit;             // 학점
    private int seatsLimit;
    private String classroom;
    private ArrayList<ClassTime> classTimes;

    /*
     lectureId가 초기화되지 않는 생성자로 Professor가 강의를 등록하고자 할 때 사용한다.
     lectureId가 초기화되지 않은 객체는 유효하지 않은 강의로 인식되며 강의가 정상적으로 등록되면 lectureId가 초기화된다.
     */
    public Lecture(String professorId, String professorName, Subject subject,
                   int credit, int seatsLimit, String classroom, ArrayList<ClassTime> classTimes) {
        assert credit > 0 : "datatype.Lecture credit must be higher than 0";
        assert seatsLimit > 0 : "datatype.Lecture credit must be higher than 0";
        assert classTimes != null : "datatype.Lecture classTimes must be create out of class";
        assert classTimes.size() != 0 : "datatype.Lecture classTimes size must be at least 1";

        this.professorId = professorId;
        this.professorName = professorName;
        this.subject = subject;
        this.credit = credit;
        this.seatsLimit = seatsLimit;
        this.classroom = classroom;
        this.classTimes = classTimes;
    }

    // lectureId를 초기화하여 유효한 강의 객체를 생성한다. DataBase에서 csv를 읽어 객체를 생성할 때 사용한다.
    public Lecture(String lectureId, String professorId, String professorName, Subject subject,
                   int credit, int seatsLimit, String classroom, ArrayList<ClassTime> classTimes) {
        assert credit > 0 : "datatype.Lecture credit must be higher than 0";
        assert seatsLimit > 0 : "datatype.Lecture credit must be higher than 0";
        assert classTimes != null : "datatype.Lecture classTimes must be create out of class";
        assert classTimes.size() != 0 : "datatype.Lecture classTimes size must be at least 1";

        this.lectureId = lectureId;
        this.professorId = professorId;
        this.professorName = professorName;
        this.subject = subject;
        this.credit = credit;
        this.seatsLimit = seatsLimit;
        this.classroom = classroom;
        this.classTimes = classTimes;
    }

    public boolean isValid() {
        if (this.lectureId == null) {
            return false;    // EnrolmentManager 클래스에게 강의 번호를 부여받지 못한 객체는 유효하지 않은 개체로 판단한다.
        }
        return LectureDataBase.getDB().selectOrNull(this.lectureId) != null;  // DB에 존재하지 않아도 유효하지 않은 객체이다.
    }

    public void setLectureId(String lectureId) {
        assert !isValid() : "datatype.Lecture fail set lecture id: is valid lecture";
        this.lectureId = lectureId;
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

    public boolean isOverlapClassTime(Lecture other) {
        for (ClassTime myTime : this.classTimes) {
            for (ClassTime otherTime: other.classTimes) {
                if (myTime.isOverlapTime(otherTime)) {
                    //System.out.println(myTime.getDay().toString() + myTime.getStart() + "" + myTime.getEnd());
                    //System.out.println(otherTime.getDay().toString() + otherTime.getStart() + "" + otherTime.getEnd());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOverlapClassroom(Lecture other) {
        if (this.classroom.equals(other.classroom)) {
            return isOverlapClassTime(other);
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
