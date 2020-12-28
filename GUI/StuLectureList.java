package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import Calcul.Calcul;
import database.AjouinDataBase;
import database.DataBase;
import database.LectureDataBase;
import database.SubjectDataBase;
import datatype.Ajouin;
import datatype.ClassTime;
import datatype.Lecture;
import datatype.Student;
import datatype.Subject;
import enrolment.EEnrolmentState;
import enrolment.EnrolmentManager;


public class StuLectureList extends JFrame {
    private static final int LECTURE_TABLE_COLUMN_COUNT = 8;

    private Student student;
    private JTable lectureTable;
    private String[] lectureCodes;

    public StuLectureList(Student student){
        super();
        assert student != null;

        this.student = student;

        DataBase<Ajouin> DB_AJOUIN = AjouinDataBase.getDB();
        DataBase<Subject> DB_SUBJECT = SubjectDataBase.getDB();
        DataBase<Lecture> DB_LECTURE = LectureDataBase.getDB();
        
        setTitle("수강신청");
        setSize(1070, 560);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();


        String[] col = { "강의명", "과목코드", "담당교수", "학점", "여석","총인원", "강의실","강의시간" };   //필드명(열제목) 지정

        assert col.length == LECTURE_TABLE_COLUMN_COUNT;

        Collection<Lecture> lectures = DB_LECTURE.getValues();
        Object[][] lectureTableRows = new Object[lectures.size()][LECTURE_TABLE_COLUMN_COUNT];
        this.lectureCodes = new String[lectures.size()];

        int i = 0;
        for (Lecture lecture : lectures) {
            this.lectureCodes[i] = lecture.getLectureId();
            lectureTableRows[i++] = createLectureTableRow(lecture);
        }

        DefaultTableCellRenderer cellAlignCenter = new DefaultTableCellRenderer();
        cellAlignCenter.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tableTitle = new JLabel(" < 강의 목록 > ");
        lectureTable = new JTable(lectureTableRows, col) {    // 강의 목록 테이블
            public boolean isCellEditable(int row, int column) {
                return false;    // 셀 편집은 못하면서도 행을 선택할 수 있도록
            }
        };

        lectureTable.getColumn("강의명").setPreferredWidth(70);
        lectureTable.getColumn("과목코드").setPreferredWidth(5);
        lectureTable.getColumn("과목코드").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("담당교수").setPreferredWidth(5);
        lectureTable.getColumn("담당교수").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("학점").setPreferredWidth(1);
        lectureTable.getColumn("학점").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("여석").setPreferredWidth(1);
        lectureTable.getColumn("여석").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("총인원").setPreferredWidth(1);
        lectureTable.getColumn("총인원").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("강의실").setPreferredWidth(5);
        lectureTable.getColumn("강의실").setCellRenderer(cellAlignCenter);
        lectureTable.getColumn("강의시간").setPreferredWidth(200);

        lectureTable.setRowHeight(18);

        JScrollPane scrollTablePane = new JScrollPane(lectureTable);
        panel.add(tableTitle);
        panel.add(scrollTablePane);

        tableTitle.setBounds(50, 15, 150, 25);
        tableTitle.setFont(new Font(null, Font.BOLD, 15));
        scrollTablePane.setLocation(50, 40);
        scrollTablePane.setSize(750, 450);

        AjouinProfile profile = new AjouinProfile(student);
        profile.setLocation(835, 65);
        panel.add(profile);
        
        JButton enrolButton = new JButton("수강 등록");
        JButton cancelButton = new JButton("수강 취소");
        JButton calculatorButton = new JButton("학점 계산기");
        JButton btn4 = new JButton("신청내역 확인");
        panel.setLayout(null);    // 버튼 고정 레이아웃 해제

        enrolButton.setBounds(835, 342, 90, 50);
        cancelButton.setBounds(945, 342, 90, 50);
        calculatorButton.setBounds(835, 412, 90, 50);
        btn4.setBounds(945, 412, 90, 50);

        panel.add(enrolButton);
        panel.add(cancelButton);
        panel.add(calculatorButton);
        panel.add(btn4);

        this.add(panel);

        setTitle("아주대학교 정통대 수강신청");




        

        // 버튼 액션 리스너 추가
        enrolButton.addActionListener( new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = lectureTable.getSelectedRow();
                String lectureCode = (String) lectureTable.getValueAt(selectedRowIndex, 1);
                EnrolmentManager em = new EnrolmentManager();

                EEnrolmentState result = em.enrolLectureFromStudent(student.getId(), lectureCodes[selectedRowIndex]);

                if (result == EEnrolmentState.SUCCESS) {
                    JDialog message = new JDialog();
                    String lectureName = (String) lectureTable.getValueAt(selectedRowIndex, 0);
                    String professorName = (String) lectureTable.getValueAt(selectedRowIndex, 2);
                    JOptionPane.showMessageDialog(message, professorName + "교수님의 " + lectureName + " 강의 등록에 성공하였습니다!");
                    return;
                } else {
                    JDialog message = new JDialog();
                    JOptionPane.showMessageDialog(message, "실패!\n" + student.getId() + "\n" + lectureCode);
                }

                // TODO: 실패시 안내 메시지 띄우기

                assert result != EEnrolmentState.FAIL_INVALID_ID;
            }
        });

        cancelButton.addActionListener( new ActionListener(){
           
           public void actionPerformed(ActionEvent e) {

                    
           }
        });

           
    
        calculatorButton.addActionListener( new ActionListener(){
           
            public void actionPerformed(ActionEvent e) {
                new Calcul();
                    
            }
           
        });

        btn4.addActionListener( new ActionListener(){
           
            public void actionPerformed(ActionEvent e) {
        	   
                    
            }
           
        });


           
   



        setVisible(true);
        

    
    }

    private Object[] createLectureTableRow(Lecture lecture) {
        Object[] tableRow = new Object[LECTURE_TABLE_COLUMN_COUNT];
        tableRow[0] = lecture.getSubject().getName();
        tableRow[1] = lecture.getSubject().getCode();
        tableRow[2] = lecture.getProfessorName();
        tableRow[3] = lecture.getCredit();
        tableRow[4] = lecture.getRemainSeat();
        tableRow[5] = lecture.getSeatsLimit();
        tableRow[6] = lecture.getClassroom();
        // 강의 시간 텍스트화
        StringBuilder sb = new StringBuilder(45);
        for (ClassTime classTime : lecture.getClassTimes()) {
            sb.append(classTime.toString());
            sb.append(" / ");
        }
        sb.delete(sb.length() - 3, sb.length());
        tableRow[7] = sb.toString();
        return tableRow;
    }
}

