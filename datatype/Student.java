package datatype;

public class Student extends Ajouin {
    public final static int MAX_CREDIT = 19;

    public Student(String id, String name, EDepartment major) {
        super(id, name, major, EAjouinIdentity.STUDENT);
    }
}
