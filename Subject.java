import java.util.ArrayList;

public class Subject {
    private String name;
    private String code;
    private int classification; // TODO: 교과 구분 ENUM 클래스 만들기
    private ArrayList<Department> majorDepartments;

    public Subject(String name, String code, int classification) {
        // TODO: classification이 전공과목일 때 majorDepartments 가 초기화 되지 않는 경우를 배제하는 assert 추가.
        this.name = name;
        this.code = code;
        this.classification = classification;
        majorDepartments = new ArrayList<>();
    }

    public Subject(String name, String code, int classification, Department majorDepartment) {
        this.name = name;
        this.code = code;
        this.classification = classification;
        this.majorDepartments = new ArrayList<>();
        this.majorDepartments.add(majorDepartment);
    }

    public Subject(String name, String code, int classification, ArrayList<Department> majorDepartments) {
        this.name = name;
        this.code = code;
        this.classification = classification;
        this.majorDepartments = new ArrayList<>();
        this.majorDepartments.addAll(majorDepartments);
    }

    public void addMajorDepartment(Department department) {
        // TODO: 교과 구분상 전공 과목이 아닐시 early return
        this.majorDepartments.add(department);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getClassification() {
        return classification;
    }

    public ArrayList<Department> getMajorDepartments() {
        return majorDepartments;
    }
}
