package Calcul;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class Calcul extends JFrame implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private JPanel main = new JPanel();
	private JLabel left = new JLabel("학점");
	private JLabel right = new JLabel("성적");
	private JButton btnCal = new JButton("계산하기");
	private JComboBox[] grade = new JComboBox[10];
	private JComboBox[] num = new JComboBox[10];
	private JTextField avg = new JTextField(3);
	private JLabel by1 = new JLabel("학점계산기");
	private String[] strGrade = {"선택", "A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "F"};
	private String[] strNum = {"선택", "1", "2", "3", "4"};
	private String[] setGrade = new String[10];
	private int[] setNum = new int[10];
	private Process process = new Process();
	
	public Calcul() {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");			
		} catch(Exception e) {}
					

				
		for(int i=0; i<10; i++) {
			grade[i] = new JComboBox();
			num[i] = new JComboBox();
			grade[i].setMaximumRowCount(10);
			for(int j=0; j<strGrade.length; j++)
				grade[i].addItem(strGrade[j]);
			for(int k=0; k<strNum.length; k++)
				num[i].addItem(strNum[k]);
		}
				
		main.setLayout(new GridBagLayout());
		
		main.add(by1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
		main.add(left, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 2, 5, 5), 0, 0));
		main.add(right, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 2, 5, 5), 0, 0));
		for(int i=0; i<10; i++) {
			main.add(num[i], new GridBagConstraints(0, i+3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
			main.add(grade[i], new GridBagConstraints(1, i+3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
		}
		main.add(btnCal, new GridBagConstraints(0, 14, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 2, 5, 5), 0, 0));
		main.add(avg, new GridBagConstraints(0, 15, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));

		avg.setBackground(Color.white);
		avg.setEditable(false);
		
		for(int i=0; i<10; i++) {
			grade[i].addItemListener(this);
			num[i].addItemListener(this);
		}
		btnCal.addActionListener(this);
	
		
		add(main, "Center");
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();		
		setTitle("학점계산기");
		setBounds(screenSize.width/2-100, screenSize.height/2-335, 200, 670);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCal) {
			try {
				double score = process.getScore(setGrade, setNum);
				avg.setText(Double.toString(score));
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "입력값이 잘못되었습니다.");
			}			
		}
		

		

	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == grade[0]) 
			setGrade[0] = (String)grade[0].getSelectedItem();
		if(e.getSource() == grade[1]) 
			setGrade[1] = (String)grade[1].getSelectedItem();
		if(e.getSource() == grade[2]) 
			setGrade[2] = (String)grade[2].getSelectedItem();
		if(e.getSource() == grade[3]) 
			setGrade[3] = (String)grade[3].getSelectedItem();
		if(e.getSource() == grade[4]) 
			setGrade[4] = (String)grade[4].getSelectedItem();
		if(e.getSource() == grade[5]) 
			setGrade[5] = (String)grade[5].getSelectedItem();
		if(e.getSource() == grade[6]) 
			setGrade[6] = (String)grade[6].getSelectedItem();
		if(e.getSource() == grade[7]) 
			setGrade[7] = (String)grade[7].getSelectedItem();
		if(e.getSource() == grade[8]) 
			setGrade[8] = (String)grade[8].getSelectedItem();
		if(e.getSource() == grade[9]) 
			setGrade[9] = (String)grade[9].getSelectedItem();
		
		if(e.getSource() == num[0]) 
			setNum[0] = Integer.parseInt((String)num[0].getSelectedItem());
		if(e.getSource() == num[1]) 
			setNum[1] = Integer.parseInt((String)num[1].getSelectedItem());
		if(e.getSource() == num[2]) 
			setNum[2] = Integer.parseInt((String)num[2].getSelectedItem());
		if(e.getSource() == num[3]) 
			setNum[3] = Integer.parseInt((String)num[3].getSelectedItem());
		if(e.getSource() == num[4]) 
			setNum[4] = Integer.parseInt((String)num[4].getSelectedItem());
		if(e.getSource() == num[5]) 
			setNum[5] = Integer.parseInt((String)num[5].getSelectedItem());
		if(e.getSource() == num[6]) 
			setNum[6] = Integer.parseInt((String)num[6].getSelectedItem());
		if(e.getSource() == num[7]) 
			setNum[7] = Integer.parseInt((String)num[7].getSelectedItem());
		if(e.getSource() == num[8]) 
			setNum[8] = Integer.parseInt((String)num[8].getSelectedItem());
		if(e.getSource() == num[9]) 
			setNum[9] = Integer.parseInt((String)num[9].getSelectedItem());
	}





		
	

	
}
	


