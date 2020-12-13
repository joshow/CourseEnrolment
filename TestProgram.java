import database.AjouinDataBase;
import database.DataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Ajouin;
import datatype.Lecture;
import datatype.Subject;

public class TestProgram {
    public static void main(String[] args) {
        // DataBase Example
        DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        Ajouin ajouin = DB_ajouin.selectOrNull("201520659");
        System.out.println(ajouin.getName() + ", " + ajouin.getMajor());

        DataBase<Lecture> DB_lecture = LectureDataBase.getDB();
        Lecture lecture = DB_lecture.selectOrNull("20ZZZZ");
        if (lecture == null) {
            System.out.println("database has not key '20ZZZZ'");
            if (!DB_lecture.hasKey("20ZZZZ")) {
                System.out.println("must be false");
            }
        }

        lecture = DB_lecture.selectOrNull("20NFNG");
        if (lecture != null) {
            System.out.println("database has key '20NFNG'");
            if (!DB_lecture.hasKey("20ZZZZ")) {
                System.out.println("must be false");
            }

            String professorid = lecture.getProfessorId();

            if (DB_ajouin.hasKey(professorid)) {
                System.out.println("must be true");
            }

            System.out.println("Lecture 20NFNG's professor is " + DB_ajouin.selectOrNull(professorid).getName());
        }

        // How to login
        if (AjouinDataBase.isValidLoginInfo("20142348", "invalid PW") == false)
            System.out.println("Login Fail");
        if (AjouinDataBase.isValidLoginInfo("invalid ID", "woonglife") == false)
            System.out.println("Login Fail");
        if (AjouinDataBase.isValidLoginInfo("20142348", "woonglife") == true)
            System.out.println("Login Success!");

        // Subject DB Test
        DataBase<Subject> DB_subject = SubjectDataBase.getDB();
        Subject subj = DB_subject.selectOrNull("F071");
        System.out.println(subj.getName() + " " + subj.getCode() + " " + subj.getClassification() + " " + subj.getMajorDepartments().get(0));

        // Delete Test
        DB_ajouin.delete("201520659");
        DB_lecture.delete("20NFNG");
        DB_subject.delete("F071");

        System.out.println();
    }
}
