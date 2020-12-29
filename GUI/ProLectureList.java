package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.DataBase;
import database.EnrolmentProfessorDataBase;
import database.LectureDataBase;
import datatype.ClassTime;
import datatype.Lecture;
import datatype.Professor;
import enrolment.EEnrolmentState;
import enrolment.EnrolmentManager;

public class ProLectureList extends JFrame {
    private static final int LECTURE_TABLE_COLUMN_COUNT = 8;

    private Professor professor;
    private JTable lectureTable;
    private DefaultTableModel lectureTableModel;

    private ProEnrolFrame enrolFrame;

    public ProLectureList(Professor professor) {
        super();
        assert professor != null;

        this.professor = professor;

        DataBase<Lecture> DB_LECTURE = LectureDataBase.getDB();

        // 기본 프레임 세팅
        setTitle("수강신청");
        setSize(1070, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();    // 실질적으로 프레임 내부를 채우는 Panel

        // 강의 목록 테이블 준비
        String[] col = { "고유번호", "강의명", "과목코드", "학점", "여석", "총인원", "강의실","강의시간" };   //필드명(열제목) 지정
        assert col.length == LECTURE_TABLE_COLUMN_COUNT;

        // 테이블을 등록한 강의 목록으로 초기화
        ArrayList<String> enrolledLectureCodes = new ArrayList<>();
        ArrayList<String> eli = EnrolmentProfessorDataBase.getDB().selectOrNull(professor.getId());
        if (eli != null) {
            enrolledLectureCodes.addAll(eli);
        }

        lectureTableModel = new DefaultTableModel(0, LECTURE_TABLE_COLUMN_COUNT);
        lectureTableModel.setColumnIdentifiers(col);

        for (String lectureCode : enrolledLectureCodes) {
            lectureTableModel.addRow(createLectureTableRow(DB_LECTURE.selectOrNull(lectureCode)));
        }

        JLabel tableTitle = new JLabel(" < 등록하신 강의 목록 > ");
        lectureTable = new JTable(lectureTableModel) {    // 강의 목록 테이블
            public boolean isCellEditable(int row, int column) {
                return false;    // 셀 편집은 못하면서도 행을 선택할 수 있도록
            }
        };

        // 가운데 정렬을 위해 Renderer 클래스를 새로 지정 세팅한다.
        DefaultTableCellRenderer lectureTableRenderer = new DefaultTableCellRenderer();
        lectureTableRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        lectureTable.setDefaultRenderer(Object.class, lectureTableRenderer);

        lectureTable.getColumn("고유번호").setPreferredWidth(5);
        lectureTable.getColumn("강의명").setPreferredWidth(70);
        lectureTable.getColumn("과목코드").setPreferredWidth(5);
        lectureTable.getColumn("학점").setPreferredWidth(1);
        lectureTable.getColumn("여석").setPreferredWidth(1);
        lectureTable.getColumn("총인원").setPreferredWidth(1);
        lectureTable.getColumn("강의실").setPreferredWidth(5);
        lectureTable.getColumn("강의시간").setPreferredWidth(200);

        lectureTable.setRowHeight(18);

        JScrollPane scrollTablePane = new JScrollPane(lectureTable);
        panel.add(tableTitle);
        panel.add(scrollTablePane);

        tableTitle.setBounds(50, 15, 180, 25);
        tableTitle.setFont(new Font(null, Font.BOLD, 15));
        scrollTablePane.setLocation(50, 40);
        scrollTablePane.setSize(750, 450);

        // 프로필 추가
        AjouinProfile profile = new AjouinProfile(professor);
        profile.setLocation(835, 65);
        panel.add(profile);

        // 버튼 추가
        JButton enrolButton = new JButton("강의 등록");
        JButton cancelButton = new JButton("강의 삭제");
        JButton scheduleButton = new JButton("시간표 확인");
        panel.setLayout(null);    // 버튼 고정 레이아웃 해제

        enrolButton.setBounds(835, 342, 90, 50);
        cancelButton.setBounds(945, 342, 90, 50);
        scheduleButton.setBounds(835, 412, 90, 50);

        panel.add(enrolButton);
        panel.add(cancelButton);
        panel.add(scheduleButton);

        this.add(panel);

        setTitle("아주대학교 정통대 강의등록");

        // 버튼 액션 리스너 추가
        enrolButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (enrolFrame == null) {
                    enrolFrame = new ProEnrolFrame(professor, (lecture) -> enrollSuccessCallBack(lecture));
                }
                if (enrolFrame.isVisible()) {
                    enrolFrame.deActivate();
                } else {
                    enrolFrame.activate();
                }
            }
        });

        cancelButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (lectureTable.getSelectedRowCount() != 1) {
                    return;
                }

                // 강의 삭제 안내
                int dialogResult = JOptionPane.showConfirmDialog (null, "강의를 정말 삭제 하시겠습니까?", "삭제 안내", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.NO_OPTION){
                    return;
                }

                int selectedRowIndex = lectureTable.getSelectedRow();

                EnrolmentManager em = new EnrolmentManager();

                EEnrolmentState result = em.cancelLectureFromProfessor(professor.getId(), (String) lectureTable.getValueAt(selectedRowIndex, 0));

                if (result == EEnrolmentState.SUCCESS) {
                    String lectureName = (String) lectureTable.getValueAt(selectedRowIndex, 1);
                    lectureTableModel.removeRow(selectedRowIndex);
                    JDialog message = new JDialog();
                    JOptionPane.showMessageDialog(message, lectureName + " 강의를 삭제하셨습니다.");
                    revalidate();
                    return;
                }

                // 조건상 강의 삭제에 실패 해서는 안된다.
                assert false;
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
        tableRow[0] = lecture.getLectureId();
        tableRow[1] = lecture.getSubject().getName();
        tableRow[2] = lecture.getSubject().getCode();
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

    private void enrollSuccessCallBack(Lecture lecture) {
        Object[] rowElement = createLectureTableRow(lecture);
        lectureTableModel.addRow(rowElement);
        revalidate();
    }
}