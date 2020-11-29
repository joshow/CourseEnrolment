public abstract class Ajouin {
    private /*final*/ String id;
    private String name;
    private Department major;
    // 복수전공 & 부전공 여부

    public Ajouin(String id) {
        // 반드시 생성자를 호출하기 이전에 유효한 id인지 검증해야 한다.
        // TODO: 데이터베이스에서 id를 통해 name 및 major 초기화
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getMajor() {
        return major;
    }
}
