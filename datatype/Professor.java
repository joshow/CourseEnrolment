package datatype;

public class Professor extends Ajouin {

    public Professor(String id, String name, EDepartment major) {
        super(id, name, major, EAjouinIdentity.PROFESSOR);
    }
}
