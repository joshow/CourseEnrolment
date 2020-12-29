package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import Calcul.Calcul;
import database.DataBase;
import database.EnrolmentStudentDataBase;
import database.LectureDataBase;
import datatype.ClassTime;
import datatype.Lecture;
import datatype.Student;
import enrolment.EEnrolmentState;
import enrolment.EnrolmentManager;


public class StuLectureList extends JFrame {
    private static final int LECTURE_TABLE_COLUMN_COUNT = 8;

    Calcul calculFrame;

    private Student student;
    private JLabel creditLabel;
    private JTable lectureTable;
    private String[] lectureCodes;
    private ArrayList<String> enrolledLectureCodes;

    public StuLectureList(Student student) {
        super();
        assert student != null;

        this.student = student;

        DataBase<Lecture> DB_LECTURE = LectureDataBase.getDB();

        // 기본 프레임 세팅
        setTitle("수강신청");
        setSize(1070, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();    // 실질적으로 프레임 내부를 채우는 Panel

        // 강의 목록 테이블 준비
        String[] col = { "강의명", "과목코드", "담당교수", "학점", "여석","총인원", "강의실","강의시간" };   //필드명(열제목) 지정
        assert col.length == LECTURE_TABLE_COLUMN_COUNT;

        // 이미 신청한 과목 목록을 멤버변수에 저장해둔다.
        enrolledLectureCodes = new ArrayList<>();
        ArrayList<String> eli = EnrolmentStudentDataBase.getDB().selectOrNull(student.getId());
        if (eli != null) {
            enrolledLectureCodes.addAll(eli);
        }

        // 테이블을 강의 목록으로 초기화
        Collection<Lecture> lectures = DB_LECTURE.getValues();
        Object[][] lectureTableRows = new Object[lectures.size()][LECTURE_TABLE_COLUMN_COUNT];
        this.lectureCodes = new String[lectures.size()];

        int i = 0;
        for (Lecture lecture : lectures) {
            this.lectureCodes[i] = lecture.getLectureId();
            lectureTableRows[i++] = createLectureTableRow(lecture);
        }

        JLabel tableTitle = new JLabel(" < 강의 목록 > ");
        lectureTable = new JTable(lectureTableRows, col) {    // 강의 목록 테이블
            public boolean isCellEditable(int row, int column) {
                return false;    // 셀 편집은 못하면서도 행을 선택할 수 있도록
            }
        };

        // 신청된 강의의 색을 지정하기 위해 Renderer 클래스를 새로 지정 세팅한다.
        DefaultTableCellRenderer lectureTableRenderer = new LectureTableCellRenderer();
        lectureTableRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        lectureTable.setDefaultRenderer(Object.class, lectureTableRenderer);

        lectureTable.getColumn("강의명").setPreferredWidth(70);
        lectureTable.getColumn("과목코드").setPreferredWidth(5);
        lectureTable.getColumn("담당교수").setPreferredWidth(5);
        lectureTable.getColumn("학점").setPreferredWidth(1);
        lectureTable.getColumn("여석").setPreferredWidth(1);
        lectureTable.getColumn("총인원").setPreferredWidth(1);
        lectureTable.getColumn("강의실").setPreferredWidth(5);
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
        JButton scheduleButton = new JButton("시간표 확인");
        panel.setLayout(null);    // 버튼 고정 레이아웃 해제

        enrolButton.setBounds(835, 335, 90, 50);
        cancelButton.setBounds(945, 335, 90, 50);
        calculatorButton.setBounds(835, 405, 90, 50);
        scheduleButton.setBounds(945, 405, 90, 50);

        //JLabel creditTitle = new JLabel("학점: ");
        creditLabel = new JLabel();
        updateCreditLabel();
        creditLabel.setBounds(840, 460, 100, 20);

        panel.add(enrolButton);
        panel.add(cancelButton);
        panel.add(calculatorButton);
        panel.add(scheduleButton);
        panel.add(creditLabel);

        this.add(panel);

        setTitle("아주대학교 정통대 수강신청");



        // 버튼 액션 리스너 추가
        enrolButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (lectureTable.getSelectedRowCount() != 1) {
                    return;
                }

                int selectedRowIndex = lectureTable.getSelectedRow();
                EnrolmentManager em = new EnrolmentManager();

                EEnrolmentState result = em.enrolLectureFromStudent(student.getId(), lectureCodes[selectedRowIndex]);

                if (result == EEnrolmentState.SUCCESS) {
                    updateCreditLabel();
                    enrolledLectureCodes.add(lectureCodes[selectedRowIndex]);
                    lectureTable.setValueAt((Integer) lectureTable.getValueAt(selectedRowIndex, 4) - 1, selectedRowIndex,4);

                    JDialog message = new JDialog();
                    String lectureName = (String) lectureTable.getValueAt(selectedRowIndex, 0);
                    String professorName = (String) lectureTable.getValueAt(selectedRowIndex, 2);
                    JOptionPane.showMessageDialog(message, professorName + "교수님의 " + lectureName + " 강의 신청에 성공하였습니다!");
                    return;
                }

                // 수강신청 실패시 안내 메시지 출력
                String failMessage = "";
                if (result == EEnrolmentState.FAIL_NO_MORE_CREDIT) {
                    failMessage = "실패. 학점이 부족합니다.";
                } else if (result == EEnrolmentState.FAIL_NO_MORE_REMAIN_SEAT) {
                    failMessage = "실패. 여석이 부족합니다.";
                } else if (result == EEnrolmentState.FAIL_ENROLLED_LECTURE) {
                    failMessage = "이미 신청하신 과목 입니다.";
                } else if (result == EEnrolmentState.FAIL_OVERLAP_CLASS_TIME) {
                    failMessage = "실패. 이미 동일한 시간대에 신청하신 과목이 있습니다.";
                } else {
                    assert false;   // Impossible
                }

                JDialog messageDialog = new JDialog();
                JOptionPane.showMessageDialog(messageDialog, failMessage);
            }
        });

        cancelButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (lectureTable.getSelectedRowCount() != 1) {
                    return;
                }

                int selectedRowIndex = lectureTable.getSelectedRow();

                // 신청되지 않은 과목은 수강 취소할 수 없음
                if (!enrolledLectureCodes.contains(lectureCodes[selectedRowIndex])) {
                    JDialog message = new JDialog();
                    JOptionPane.showMessageDialog(message, "신청하지 않은 강의는 수강 취소 할 수 없습니다.");
                    return;
                }

                EnrolmentManager em = new EnrolmentManager();

                EEnrolmentState result = em.cancelLectureFromStudent(student.getId(), lectureCodes[selectedRowIndex]);

                if (result == EEnrolmentState.SUCCESS) {
                    updateCreditLabel();
                    enrolledLectureCodes.remove(lectureCodes[selectedRowIndex]);
                    lectureTable.setValueAt((Integer) lectureTable.getValueAt(selectedRowIndex, 4) + 1, selectedRowIndex,4);

                    JDialog message = new JDialog();
                    String lectureName = (String) lectureTable.getValueAt(selectedRowIndex, 0);
                    String professorName = (String) lectureTable.getValueAt(selectedRowIndex, 2);
                    JOptionPane.showMessageDialog(message, professorName + "교수님의 " + lectureName + " 강의 수강 취소에 성공하였습니다.");
                    return;
                }

                // 수강 취소 실패시 안내 메시지 출력
                String failMessage = "";
                if (result == EEnrolmentState.FAIL_NONE_ENROLLED_LECTURE) {
                    enrolledLectureCodes.remove(lectureCodes[selectedRowIndex]);
                    failMessage = "신청하지 않은 강의는 수강취소 할 수 없습니다.";
                } else {
                    assert false;   // Impossible
                }

                JDialog messageDialog = new JDialog();
                JOptionPane.showMessageDialog(messageDialog, failMessage);
            }
        });

           

        calculatorButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (calculFrame == null) {
                    calculFrame = new Calcul();
                }
                calculFrame.setVisible(!calculFrame.isVisible());    // 보이는 상태 토글
            }
        });

        scheduleButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JDialog message = new JDialog();
                JOptionPane.showMessageDialog(message, "아직 준비되지 않은 기능입니다.");
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

    private void updateCreditLabel() {
        assert this.creditLabel != null;
        this.creditLabel.setText("학점: " + this.student.getEnrolledCredit() + " / " + Student.MAX_CREDIT);
    }



    // 코드 출처: https://blaseed.tistory.com/15
    // getTableCellRendererComponent() 메소드를 오버라이드하여 셀의 배경색을 지정할 수 있다.
    private class LectureTableCellRenderer extends DefaultTableCellRenderer {
        private final Color BLACK_COLOR = new Color(0x0);
        private final Color WHITE_COLOR = new Color(0xFFFFFF);
        private final Color YELLOW_COLOR = new Color(252, 247, 135);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (enrolledLectureCodes.contains(lectureCodes[row])) {    // 수강 신청한 강의의 경우
                if (isSelected) {
                    cell.setForeground(BLACK_COLOR);
                } else {
                    cell.setBackground(YELLOW_COLOR);
                }
            } else {
                if (!isSelected) {
                    cell.setBackground(WHITE_COLOR);
                }
            }
            return cell;
        }
    }
}

