package datatype;

import java.util.ArrayList;
import java.util.Arrays;

public class Subject implements Cloneable {
    private String name;
    private String code;
    private EClassification classification;
    private ArrayList<EDepartment> majorDepartments;

    // 임시
    public Subject() {

    }

    public Subject(String name, String code, EClassification classification) {
        assert classification != EClassification.CORE;

        this.name = name;
        this.code = code;
        this.classification = classification;
        this.majorDepartments = new ArrayList<>();
    }

    public Subject(String name, String code, EClassification classification, EDepartment majorDepartment) {
        this.name = name;
        this.code = code;
        this.classification = classification;
        this.majorDepartments = new ArrayList<>();
        this.majorDepartments.add(majorDepartment);
    }

    public Subject(String name, String code, EClassification classification, EDepartment[] majorDepartments) {
        assert majorDepartments != null && majorDepartments.length != 0;

        this.name = name;
        this.code = code;
        this.classification = classification;
        this.majorDepartments = new ArrayList<>(Arrays.asList(majorDepartments));
    }

    public void addMajorDepartment(EDepartment department) {
        // TODO: 교과 구분상 전공 과목이 아닐시 early return
        this.majorDepartments.add(department);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public EClassification getClassification() {
        return classification;
    }

    public ArrayList<EDepartment> getMajorDepartments() {
        return majorDepartments;
    }

    @Override
    public Object clone(){
        Object subjectClone = null;

        try {
            subjectClone = super.clone();
        } catch (CloneNotSupportedException e) {
            assert false: "Fail Subject.clone()";
        }
        return subjectClone;
    }
}
