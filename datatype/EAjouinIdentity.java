package datatype;

public enum EAjouinIdentity {
    PROFESSOR,
    STUDENT;

    @Override
    public String toString() {
        return this == PROFESSOR ? "교수" : "학생";
    }
}
