import java.util.ArrayList;

public class Lecture {
    private final String professorId;
    private String professorName;
    private final Subject subject;
    private final ClassTime classTime;
    private int credit;             // 학점
    private int maxEnrolCount;
    private String classroom;
    private final ArrayList<Ajouin> students = new ArrayList<>();

    public Lecture(String professorId, Subject subject, ClassTime classTime, int credit, int maxEnrolCount, String classroom) {
        // TODO: professorId가 유효한지 검증 필요(assert 사용할 것)
        assert credit > 0 : "Lecture credit must be higher than 0";
        assert maxEnrolCount > 0 : "Lecture credit must be higher than 0";

        this.professorId = professorId;
        // TODO: professorId를 통해 this.professorName 초기화. 경우에 따라선 멤버 변수를 제거
        this.subject = subject;
        this.classTime = classTime;
        this.maxEnrolCount = maxEnrolCount;
        this.classroom = classroom;
    }

    public void setMaxEnrolCount(int maxEnrolCount) {
        assert maxEnrolCount > 0 : "Lecture credit must be higher than 0";
        this.maxEnrolCount = maxEnrolCount;
    }

    public void setClassTime(int start, int end) {
        this.classTime.updateTime(start, end);
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getProfessorId() {
        return professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public ClassTime getClassTime() {
        return classTime;
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
}
