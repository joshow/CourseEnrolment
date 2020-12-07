package datatype;

// 교과구분
public enum EClassification {
    CORE("CORE"),
    ELECTIVE("ELECTIVE");

    private final String signature;

    EClassification(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return signature;
    }

    public static EClassification getEnumFrom(String signature) {
        assert signature != null;

        if (signature.equals("CORE")) {
            return EClassification.CORE;
        }

        return EClassification.ELECTIVE;
    }
}
