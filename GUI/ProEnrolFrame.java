package GUI;

import database.DataBase;
import database.SubjectDataBase;
import datatype.ClassTime;
import datatype.EClassification;
import datatype.EDay;
import datatype.EDepartment;
import datatype.Lecture;
import datatype.Professor;
import datatype.Subject;
import enrolment.EEnrolmentState;
import enrolment.EnrolmentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;

public class ProEnrolFrame extends JFrame {
    private static final int FRAME_WIDTH = 470;
    private static final int FRAME_HEIGHT = 300;

    private Professor professor;

    private JComboBox<Subject> subjectCombo;

    private JTextField creditTextField;
    private JTextField seatLimitTextField;
    private JTextField classroomTextField;

    private ClassTimePanel classTimePanel;

    private EnrollSuccessCallBackListener enrollSuccessCallBackListener;


    public ProEnrolFrame(Professor professor, EnrollSuccessCallBackListener enrollSuccessCallBackListener) {
        super();
        assert professor != null;
        assert enrollSuccessCallBackListener != null;

        this.professor = professor;
        this.enrollSuccessCallBackListener = enrollSuccessCallBackListener;

        setTitle("강의 등록");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);            // Frame 크기 고정
        setLocationRelativeTo(null);    // Frame을 화면 정중앙에 위치시킴

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding
        setContentPane(contentPanel);

        setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        subjectCombo = new JComboBox<>();

        DataBase<Subject> DB_SUBJECT = SubjectDataBase.getDB();
        Collection<Subject> items = DB_SUBJECT.getValues();
        subjectCombo.addItem(new Subject("과목 -", "", EClassification.ELECTIVE));
        for (Subject item : items) {
            if (item.getClassification() == EClassification.CORE) {    // 본인 학과의 전공 과목만을 콤보박스에 표시한다.
                ArrayList<EDepartment> majors = item.getMajorDepartments();
                for (EDepartment major : majors) {
                    if (professor.getMajor() == major) {
                        subjectCombo.addItem(item);
                        break;
                    }
                }
            } else {    // 교양과목의 경우 어떤 학과의 교수나 등록 가능하다.
                subjectCombo.addItem(item);
            }
        }
        add(subjectCombo);

        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // credit 학점 입력을 받기 위한 라벨, 텍스트 필드
        JPanel creditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel creditLabel = new JLabel("   학점");
        creditTextField = new JTextField(6);
        creditPanel.add(creditLabel);
        creditPanel.add(creditTextField);

        // 여석 입력을 받기 위한 라벨, 텍스트 필드
        JPanel seatLimitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seatLimitLabel = new JLabel("   여석");
        seatLimitTextField = new JTextField(6);
        seatLimitPanel.add(seatLimitLabel);
        seatLimitPanel.add(seatLimitTextField);

        // classroom 라벨, 텍스트 필드 => 강의실 DB가 따로 없기 때문에 별도 유효성 검사를 하지 않음
        JPanel classroomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel classroomLabel = new JLabel("강의실");
        classroomTextField = new JTextField(6);
        classroomPanel.add(classroomLabel);
        classroomPanel.add(classroomTextField);

        bottomLeftPanel.add(creditPanel);
        bottomLeftPanel.add(seatLimitPanel);
        bottomLeftPanel.add(classroomPanel);

        add(Box.createHorizontalStrut(2));

        // 하단부에 creditPanel과 classroomPanel을 적당한 비율로 추가되도록 GridBagLayout을 사용하였음
        GridBagLayout bottomLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        // 좌측 - 학점, 여석, 강의실
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        bottomLeftPanel.setPreferredSize(new Dimension(130, 60));
        bottomLayout.setConstraints(bottomLeftPanel, constraints);

        // 우측 - 강의 시간
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        classTimePanel = new ClassTimePanel();
        bottomLayout.setConstraints(classTimePanel, constraints);

        // 최하단 버튼
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 0.1;
        JPanel confirmButtonPanel = new JPanel();
        JButton okButton = new JButton("등록");
        okButton.addActionListener(this::okButtonClickEvent);
        JButton cancelButton = new JButton("취소");
        confirmButtonPanel.add(okButton);
        okButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        confirmButtonPanel.add(cancelButton);
        cancelButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        cancelButton.addActionListener((event) -> deActivate());    // cancelButtonClickEvent()
        bottomLayout.setConstraints(confirmButtonPanel, constraints);

        JPanel bottomPanel = new JPanel(bottomLayout);
        bottomPanel.add(bottomLeftPanel);
        bottomPanel.add(classTimePanel);
        bottomPanel.add(confirmButtonPanel);

        add(bottomPanel);
    }

    // 창을 활성화 시키는 메소드. 모든 상태를 초기화한다.
    public void activate() {
        subjectCombo.setSelectedIndex(0);

        creditTextField.setText("");
        seatLimitTextField.setText("");
        classroomTextField.setText("");

        classTimePanel.reset();

        setVisible(true);
    }

    public void deActivate() {
        setVisible(false);
    }

    // 등록 버튼 클릭시 유효성 검사 후
    private void okButtonClickEvent(ActionEvent event) {
        // 과목 선택 여부 확인
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        if (selectedSubject.getCode().length() == 0) {
            JDialog message = new JDialog();
            JOptionPane.showMessageDialog(message, "등록하실 과목을 골라주세요.");
            return;
        }

        // 텍스트 필드에 입력된 값이 유효한지 확인
        String creditText = creditTextField.getText();
        String seatLimitText = seatLimitTextField.getText();
        String classroomText = classroomTextField.getText();
        if (creditText.length() == 0 || seatLimitText.length() == 0 || classroomText.length() == 0) {
            JDialog message = new JDialog();
            JOptionPane.showMessageDialog(message, "등록에 필요한 값을 입력해주세요.");
            return;
        }

        int credit = -1;
        int seatLimit = -1;
        try {
            credit = Integer.parseInt(creditText);
            seatLimit = Integer.parseInt(seatLimitText);
        } catch (NumberFormatException exception) {
            credit = -1;
        }

        if (credit <= 0) {
            JDialog message = new JDialog();
            JOptionPane.showMessageDialog(message, "학점 & 여석은 1 이상의 정수만 입력하실 수 있습니다.");
            return;
        }

        // 강의 시간을 추가하였는지 확인
        ArrayList<ClassTime> classTimes = classTimePanel.getClassTimes();
        if (classTimes.isEmpty()) {
            JDialog message = new JDialog();
            JOptionPane.showMessageDialog(message, "강의 시간을 추가해주세요.");
            return;
        }

        classTimes = new ArrayList<>(classTimes);    // 새 ArrayList로 복사
        EnrolmentManager em = new EnrolmentManager();
        Lecture lecture = new Lecture(professor.getId(), professor.getName(), selectedSubject, credit, seatLimit,
                                        classroomText, classTimes);
        EEnrolmentState result = em.enrolLectureFromProfessor(professor.getId(), lecture);    // 강의 등록 시도

        if (result == EEnrolmentState.SUCCESS) {
            JDialog message = new JDialog();
            JOptionPane.showMessageDialog(message, "강의 등록에 성공하였습니다!");
            EventQueue.invokeLater(() -> {
                enrollSuccessCallBackListener.callback(lecture);
            });
            deActivate();
            return;
        }

        String dialogMessage = "";
        if (result == EEnrolmentState.FAIL_OVERLAP_CLASSROOM) {
            dialogMessage = "실패. 동일한 장소에서 진행되는 강의가 있습니다.";
        } else if (result == EEnrolmentState.FAIL_OVERLAP_CLASS_TIME) {
            dialogMessage = "실패. 이미 동일한 시간에 등록하신 강의가 있습니다.";
        } else {
            assert false;   // Impossible
        }

        JDialog messageDialog = new JDialog();
        JOptionPane.showMessageDialog(messageDialog, dialogMessage);
    }

    // 강의 시간을 추가/제거 할 수 있는 Panel 클래스
    private static class ClassTimePanel extends JPanel {
        public static final int CLASS_TIME_LIMIT = 3;

        private JPanel adderPanel;
        private JComboBox<EDay> adderDayCombo;
        private JComboBox<Integer> adderBeginCombo;
        private JComboBox<Integer> adderEndCombo;
        private Box adderBox;

        private ArrayList<ClassTime> classTimes = new ArrayList<>();
        private ArrayList<JPanel> timePanels = new ArrayList<>();

        public ClassTimePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(100, 60));
            setBorder(BorderFactory.createEmptyBorder(12, 5, 0, 0));

            Box titleBox = Box.createVerticalBox();
            JLabel classTimeLabel = new JLabel("   강의시간");
            classTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            titleBox.add(classTimeLabel);

            add(titleBox);

            adderBox = Box.createVerticalBox();
            initAdderPanel();
            adderBox.add(adderPanel);
            add(adderBox);
        }

        private void initAdderPanel() {
            adderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            adderPanel.setPreferredSize(new Dimension(100, 60));
            adderDayCombo = new JComboBox<>();
            for (EDay day : EDay.values()) {
                adderDayCombo.addItem(day);
            }

            adderBeginCombo = new JComboBox<>();
            for (int i = ClassTime.min; i < ClassTime.max; ++i) {
                adderBeginCombo.addItem(i);
            }
            adderBeginCombo.addItemListener((event) -> {    // 시작 시간이 바뀔시 끝나는 시간대의 범위를 조정한다.
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    adjustAdderCombo();
                }
            });

            adderEndCombo = new JComboBox<>();
            for (int i = ClassTime.min + 1; i <= ClassTime.max; ++i) {
                adderEndCombo.addItem(i);
            }
            adderEndCombo.setSelectedIndex(adderEndCombo.getItemCount() - 1);

            JButton addButton = new JButton("+");
            addButton.setPreferredSize(new Dimension(25, 25));
            addButton.addActionListener((event) -> {    // 강의 시간 추가 버튼 클릭시 액션
                // 최대 갯수 이상 추가 할 수 없도록한다.
                if (classTimes.size() >= CLASS_TIME_LIMIT) {
                    JDialog message = new JDialog();
                    JOptionPane.showMessageDialog(message, "더이상 시간을 추가할 수 없습니다.");
                    return;
                }

                ClassTime classTime = new ClassTime((EDay) adderDayCombo.getSelectedItem(),
                                                    (int) adderBeginCombo.getSelectedItem(),
                                                    (int) adderEndCombo.getSelectedItem());

                for (ClassTime existClassTime : classTimes) {
                    if (existClassTime.isOverlapTime(classTime)) {
                        JDialog message = new JDialog();
                        JOptionPane.showMessageDialog(message, "추가한 시간대와 겹치는 시간 입니다.");
                        return;
                    }
                }

                classTimes.add(classTime);

                JPanel timePanel = new JPanel();
                JLabel label = new JLabel(classTime.toString());
                JButton removeButton = new JButton("-");
                removeButton.setPreferredSize(new Dimension(25, 25));
                removeButton.addActionListener((_event) -> {    // 강의 시간 제거 액션
                    timePanels.remove(timePanel);
                    remove(timePanel);
                    classTimes.remove(classTime);
                    revalidate();
                });

                timePanel.add(label);
                timePanel.add(removeButton);
                timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                timePanels.add(timePanel);
                add(timePanel);

                revalidate();
            });


            adderPanel.add(adderDayCombo);
            adderPanel.add(adderBeginCombo);
            adderPanel.add(adderEndCombo);
            adderPanel.add(addButton);
            adderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        // begin combo box의 상태에 따라 end combo box의 범위를 조정한다.
        private void adjustAdderCombo() {
            int beginIndex = adderBeginCombo.getSelectedIndex();
            int endItem = (Integer) adderEndCombo.getSelectedItem();
            adderEndCombo.removeAllItems();
            for (int i = beginIndex + 2; i <= ClassTime.max; ++i) {
                adderEndCombo.addItem(i);
                if (i == endItem) {
                    adderEndCombo.setSelectedIndex(adderEndCombo.getItemCount() - 1);
                }
            }
        }

        public void reset() {
            for (JPanel timePanel : timePanels) {
                remove(timePanel);
            }
            timePanels.clear();
            classTimes.clear();

            adderDayCombo.setSelectedIndex(0);
            adderBeginCombo.setSelectedIndex(0);  // with adjustAdderCombo()
            adderEndCombo.setSelectedIndex(adderEndCombo.getItemCount() - 1);
        }

        public ArrayList<ClassTime> getClassTimes() {
            return classTimes;
        }
    }


    interface EnrollSuccessCallBackListener {
        void callback(Lecture enrolledLecture);
    }
}
