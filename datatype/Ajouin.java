package datatype;

public abstract class Ajouin implements Cloneable {
    private /*final*/ String id;
    private String name;
    private EDepartment major;
    private EAjouinIdentity identity;

    public Ajouin(String id, String name, EDepartment major, EAjouinIdentity identity) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.identity = identity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EDepartment getMajor() {
        return major;
    }

    @Override
    public Object clone(){
        Object ajouinClone = null;

        try {
            ajouinClone = super.clone();
        } catch (CloneNotSupportedException e) {
            assert false: "Fail ajouin.clone()";
        }
        return ajouinClone;
    }
}
