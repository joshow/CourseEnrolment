package datatype;

public class Student extends Ajouin {
    public Student(String id, String name, EDepartment major) {
        super(id, name, major, EAjouinIdentity.STUDENT);
    }
}
