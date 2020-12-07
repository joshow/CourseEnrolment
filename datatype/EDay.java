package datatype;

public enum EDay {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    private final String signature;

    EDay(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return signature;
    }

    public static EDay getEnumFrom(char day_ko) {

        if (day_ko == '월') {
            return EDay.MON;
        } else if (day_ko == '화') {
            return EDay.TUE;
        } else if (day_ko == '수') {
            return EDay.WED;
        } else if (day_ko == '목') {
            return EDay.THU;
        } else if (day_ko == '금') {
            return EDay.FRI;
        } else if (day_ko == '토') {
            return EDay.SAT;
        } else if (day_ko == '일') {
            return EDay.SUN;
        }

        assert false;
        return EDay.SUN;   // avoid compile error
    }
}
