package GUI;

import database.AjouinDataBase;
import database.DataBase;
import datatype.Ajouin;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.imageio.*;

public class Login extends JFrame{
	private JFrame frm;


	public Login() {
	
		DataBase<Ajouin> DB_ajouin = AjouinDataBase.getDB();
        JPanel panel = new JPanel();		
		JLabel label = new JLabel("학번 입력: ");
		JLabel pswrd = new JLabel("비밀번호: ");
		JLabel imgLbl = new JLabel();
		ImageIcon icon = new ImageIcon("C:/Users/Park/Downloads/ajoulogo.jpg"); 

		

		Image im = icon.getImage(); 

		Image im2 = im.getScaledInstance(250, 250, Image.SCALE_DEFAULT);

		

		ImageIcon icon2 = new ImageIcon(im2);

		JLabel img = new JLabel(icon2);
		

		
		JTextField txtID = new JTextField(12);
		JPasswordField txtPass = new JPasswordField(12);
		JButton logBtn = new JButton("로그인");
		
		panel.add(label);
		panel.add(txtID);
		panel.add(pswrd);
		panel.add(txtPass);
		panel.add(logBtn);
		
		logBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String id = txtID.getText() ;
				@SuppressWarnings("deprecation")
				String password = txtPass.getText() ;
				
				if(AjouinDataBase.isValidLoginInfo(id,password) == true)
				{
					
					
					JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.");
					new LectureList();
					dispose();
					
				}
				else
				{
					JOptionPane.showMessageDialog(null, "등록되지 않은 아이디 또는 패스워드입니다. 다시입력하세요");
					
				}
			}
		});
		add(panel);
		
		setVisible(true);
		setSize(400,200);
		setLocationRelativeTo(null);
		setResizable(true);
		img.setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	
	public static void main(String args[])
	

	{
		
		new Login();
		
	}
}