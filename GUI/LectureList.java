package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import Calcul.Calcul;
import database.AjouinDataBase;
import database.DataBase;
import database.EnrolmentLectureDataBase;
import database.EnrolmentProfessorDataBase;
import database.EnrolmentStudentDataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Ajouin;
import datatype.EAjouinIdentity;
import datatype.Lecture;
import datatype.Professor;
import datatype.Student;
import datatype.Subject;
import enrolment.EnrolmentManager;
import java.util.ArrayList;







public class LectureList extends JFrame {
    
    public LectureList(){
    
        EnrolmentManager em = new EnrolmentManager();
        DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        DataBase<Subject> DB_subject = SubjectDataBase.getDB();
        DataBase<Lecture> DB_lecture = LectureDataBase.getDB();
        
        setTitle("수강신청 목록");
        setSize(1500, 1000);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JButton btn1 = new JButton("수강 등록");
        JButton btn2 = new JButton("수강 취소");
        JButton btn3 = new JButton("강의 등록");
        JButton btn4 = new JButton("강의 삭제");
        JButton btn5 = new JButton("강의 수정");
        JButton btn6 = new JButton("학점 계산기");
        
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        panel.add(btn5);
        panel.add(btn6);
        this.add(panel);

       btn1.addActionListener( new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
            	
                        
            }
            
        });
       btn2.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {

                    
           }
           
       });
       btn3.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {

                
           }
           
       });
       btn4.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {

                    
           }
           
       });
       btn5.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {

                   
           }
           
       });
       btn6.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {
        	   new Calcul();
                   
           }
           
       });



        setVisible(true);
    } // 메인 프레임

    

    
 

    
     
}




