import GUI.Login;
import database.AjouinDataBase;
import database.EnrolmentLectureDataBase;
import database.EnrolmentProfessorDataBase;
import database.EnrolmentStudentDataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Subject;

import javax.swing.*;
import java.awt.*;

public class MainProgram {
    public static void main(String[] args) {
        LoadDataBase();
        EventQueue.invokeLater(() -> {
            JFrame frame = new Login();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });


    }

    private static void LoadDataBase() {
        AjouinDataBase.getDB();
        SubjectDataBase.getDB();
        LectureDataBase.getDB();
        EnrolmentProfessorDataBase.getDB();
        EnrolmentStudentDataBase.getDB();
        EnrolmentLectureDataBase.getDB();
    }
}
