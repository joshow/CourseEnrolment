package GUI;

import database.AjouinDataBase;
import database.DataBase;
import datatype.Ajouin;
import datatype.EAjouinIdentity;
import datatype.Student;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.imageio.*;

public class Login extends JFrame{
	public Login() {
		AjouinDataBase.getDB();    // 사용자 정보 DB 로딩

        JPanel panel = new JPanel();		
		JLabel label = new JLabel("학번 입력: ");
		JLabel pswrd = new JLabel("비밀번호: ");

		JTextField txtID = new JTextField(12);
		JPasswordField txtPass = new JPasswordField(12);

		JButton logBtn = new JButton("로그인");

		panel.setBorder(BorderFactory.createEmptyBorder(10, 8, 0, 8));
		panel.add(label);
		panel.add(txtID);
		panel.add(pswrd);
		panel.add(txtPass);
		panel.add(logBtn);
		
		logBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = txtID.getText() ;
				String password = String.valueOf(txtPass.getPassword());
				
				if(AjouinDataBase.isValidLoginInfo(id,password)) {
					JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.");
					Ajouin ajouin = AjouinDataBase.getDB().selectOrNull(id);
					if (ajouin.getIdentity() == EAjouinIdentity.PROFESSOR) {
						EventQueue.invokeLater(ProLectureList::new);
					} else {
						EventQueue.invokeLater(() -> new StuLectureList((Student) ajouin));
					}
					dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "등록되지 않은 아이디 또는 패스워드입니다. 다시입력하세요");
				}
			}
		});

		setTitle("수강신청 로그인");
		setSize(280,150);
		setLocationRelativeTo(null);    // Frame을 화면 정중앙에 위치시킴
		setResizable(false);            // Frame 크기 고정

		add(panel);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {
			new Login();
		});
	}
	*/
}