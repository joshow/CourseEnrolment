import database.AjouinDataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Ajouin;
import datatype.Lecture;
import datatype.Subject;

public class TestProgram {
    public static void main(String args[]) {
        // DataBase Example
        AjouinDataBase DB_ajouin = AjouinDataBase.getInstance();
        Ajouin ajouin = DB_ajouin.selectOrNull("201520659");
        System.out.println(ajouin.getName() + ", " + ajouin.getMajor());

        LectureDataBase lecDb = LectureDataBase.getInstance();
        Lecture lecture = lecDb.selectOrNull("20ZZZZ");
        if (lecture == null) {
            System.out.println("database has not key '20ZZZZ'");
            if (!lecDb.hasKey("20ZZZZ")) {
                System.out.println("must be false");
            }
        }

        lecture = lecDb.selectOrNull("20NFNG");
        if (lecture != null) {
            System.out.println("database has key '20NFNG'");
            if (lecDb.hasKey("20ZZZZ")) {
                System.out.println("must be true");
            }

            String professorid = lecture.getProfessorId();

            if (DB_ajouin.hasKey(professorid)) {
                System.out.println("must be true too");
            }

            System.out.println("Lecture 20NFNG's professor is " + DB_ajouin.selectOrNull(professorid).getName());
        }

        // How to login
        if (DB_ajouin.isValidLoginInfo("20142348", "invalid PW") == false)
            System.out.println("Login Fail");
        if (DB_ajouin.isValidLoginInfo("invalid ID", "woonglife") == false)
            System.out.println("Login Fail");
        if (DB_ajouin.isValidLoginInfo("20142348", "woonglife") == true)
            System.out.println("Login Success!");

        // Subject DB Test
        SubjectDataBase DB_subject = SubjectDataBase.getInstance();
        Subject subj = DB_subject.selectOrNull("F071");
        System.out.println(subj.getName() + " " + subj.getCode() + " " + subj.getClassification() + " " + subj.getMajorDepartments().get(0));

        // Delete Test
        DB_ajouin.delete("201520659");
        lecDb.delete("20NFNG");
        DB_subject.delete("F071");

        System.out.println();
    }
}
