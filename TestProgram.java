import database.AjouinDataBase;
import datatype.Ajouin;

public class TestProgram {
    public static void main(String args[]) {
        AjouinDataBase DB_ajouin = AjouinDataBase.getInstance();

        Ajouin ajouin = DB_ajouin.selectOrNull("201520659");

        System.out.println(ajouin.getName() + ", " + ajouin.getMajor());

    }
}
