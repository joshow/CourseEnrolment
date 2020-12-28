package datatype;

import java.util.ArrayList;
import java.util.Arrays;

public class Subject implements Cloneable, ICloneable<Subject> {
    private String name;
    private String code;
    private EClassification classification;
    private ArrayList<EDepartment> majorDepartments;

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
        if (classification != EClassification.CORE) {
            return;
        }
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

    @Override    // ICloneable<Subject>
    public Subject clone(){
        Subject subjectClone = null;

        try {
            subjectClone = (Subject) super.clone();    // Object clone() => Cloneable
        } catch (CloneNotSupportedException e) {
            assert false: "Fail Subject.clone()";
        }
        return subjectClone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append(code);
        sb.append(" - ");
        sb.append(name);
        return sb.toString();
    }
}
