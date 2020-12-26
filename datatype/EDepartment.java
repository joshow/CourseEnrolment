package datatype;

public enum EDepartment {
    ELECTRICAL_COMPUTER_ENGINEERING("ECE"),
    SOFTWARE("SW"),
    CYBER_SECURITY("CS"),
    DIGITAL_MEDIA("DM"),
    MILITARY_DIGITAL_CONVERGENCE("MDC"),
    ETC("ETC");

    private final String code;

    EDepartment(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static EDepartment getEnumFrom(String code) {
        assert code != null;

        if (code.equals("ECE")) {
            return EDepartment.ELECTRICAL_COMPUTER_ENGINEERING;
        } else if (code.equals("SW")) {
            return EDepartment.SOFTWARE;
        } else if (code.equals("CS")) {
            return EDepartment.CYBER_SECURITY;
        } else if (code.equals("DM")) {
            return EDepartment.DIGITAL_MEDIA;
        } else if (code.equals("MDC")) {
            return EDepartment.MILITARY_DIGITAL_CONVERGENCE;
        }

        return EDepartment.ETC;
    }

    @Override
    public String toString() {
        if (code.equals("ECE")) {
            return "전자공학과";
        } else if (code.equals("SW")) {
            return "소프트웨어학과";
        } else if (code.equals("CS")) {
            return "사이버보안학과";
        } else if (code.equals("DM")) {
            return "미디어학과";
        } else if (code.equals("MDC")) {
            return "국방디지털융합학과";
        }

        return "기타";
    }
}
