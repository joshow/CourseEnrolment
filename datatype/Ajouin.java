package datatype;

public abstract class Ajouin implements Cloneable, ICloneable<Ajouin> {
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

    public EAjouinIdentity getIdentity() {
        return identity;
    }

    @Override    // ICloneable<Ajouin>
    public Ajouin clone(){
        Ajouin ajouinClone = null;

        try {
            ajouinClone = (Ajouin) super.clone();   // Object clone() => Cloneable
        } catch (CloneNotSupportedException e) {
            assert false: "Fail ajouin.clone()";
        }
        return ajouinClone;
    }
}
