package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
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
import GUI.AjouinProfile;







public class ProLectureList extends JFrame {
    
    public ProLectureList(){
    
        EnrolmentManager em = new EnrolmentManager();
        DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        DataBase<Subject> DB_subject = SubjectDataBase.getDB();
        DataBase<Lecture> DB_lecture = LectureDataBase.getDB();
        
        setTitle("수강신청 목록");
        setSize(1500, 1000);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel title = new JLabel("아주대학교 정보통신대학 수강신청 프로그램");
        JButton btn1 = new JButton("강의 등록");
        JButton btn2 = new JButton("강의 삭제");
        JButton btn3 = new JButton("강의 수정");
        panel.setLayout(null); // 버튼 고정 레이아웃 해제
        
        String col[] = { "강의명","과목코드", "학점", "여석", "강의실","강의시간" };   //필드명(열제목) 지정
        
        Object values[][] = { { "수학", "123", "3", "4","팔달","3:00~9:00" }, //레코드값! 7개의 레코드 생성
                {  "수학", "123", "3", "4","팔달","3:00~9:00" },
                {  "수학", "123", "3", "4","팔달","3:00~9:00" },
                {  "수학", "123", "3", "4","팔달","3:00~9:00" },
                {  "수학", "123", "3", "4","팔달","3:00~9:00" },
                {  "수학", "123", "3", "4","팔달","3:00~9:00" },
                {  "수학", "123", "3", "4","팔달","3:00~9:00" } };
        
        JTable table = new JTable(values, col);//수강신청 테이블
        
        JScrollPane pane = new JScrollPane(table); 
        panel.add(pane); 
        pane.setLocation(100,100);
        pane.setSize(800,750);
        
        btn1.setBounds(150, 900, 122, 30);
        btn2.setBounds(350, 900, 122, 30);
        btn3.setBounds(550, 900, 122, 30);
       
        
        

        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
      
        this.add(panel);
        panel.add(title);
        

     


           
   
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


      

        setVisible(true);
       
    
    
    
    }
    // 메인 프레임
   


    
	
    public static void main(String[] args)
    
    {
    	new ProLectureList();
    	
    }

    
    

 
	
    
}