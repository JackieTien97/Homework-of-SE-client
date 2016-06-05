package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;


public class LoginFrame extends JFrame implements ActionListener{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1108977764709499706L;
	JPanel jp;
	JLabel usernameOfJl;
	JLabel passwordOfJl;
	JTextField username;
	JPasswordField password;
	JButton register;
	JButton login;
	JButton logout;
	String user = "";
	MainFrame mainFrame;
	
	public LoginFrame(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		jp = new JPanel();
		jp.setLayout(null);
		usernameOfJl = new JLabel("Username");
		usernameOfJl.setBounds(25, 25, 80, 25);
		jp.add(usernameOfJl);
		passwordOfJl = new JLabel("Password");
		passwordOfJl.setBounds(25, 75, 80, 25);
		jp.add(passwordOfJl);
		username = new JTextField();
		username.setBounds(105, 25, 125, 25);
		jp.add(username);
		password = new JPasswordField();
		password.setBounds(105, 75, 125, 25);
		jp.add(password);
		register = new JButton("Register");
		register.setBounds(5, 125, 85, 25);
		register.addActionListener(this);
		jp.add(register);
		login = new JButton("Login");
		login.setBounds(100, 125, 85, 25);
		login.addActionListener(this);
		jp.add(login);
		logout = new JButton("Logout");
		logout.setBounds(195, 125, 75, 25);
		logout.setEnabled(false);
		logout.addActionListener(this);
		jp.add(logout);
		this.add(jp);
		this.setLocation(500, 300);
		this.setSize(280, 200);
		this.setTitle("Login");
		this.setResizable(false);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				    LoginFrame.this.setVisible(false);
					return;
				}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == register){
			String usernameTemp = this.username.getText();
			String passwordTemp = new String(this.password.getPassword());
			try{
				if(!RemoteHelper.getInstance().getIOService().creatFile(usernameTemp, "")){
					JOptionPane.showMessageDialog(this, "Your username has been registered!");
				}
				else{
					RemoteHelper.getInstance().getIOService().writeFile(passwordTemp, usernameTemp, "");
					this.setVisible(false);
					this.user = usernameTemp;
					mainFrame.textArea.setEditable(true);
					mainFrame.inputArea.setEditable(true);
					mainFrame.loginMenuItem.setEnabled(false);
					mainFrame.logoutMenuItem.setEnabled(true);
					this.login.setEnabled(false);
					this.register.setEnabled(false);
					mainFrame.frame.setTitle("BF Client" + "/" + this.user);
				}
			}catch(RemoteException e1){
				e1.printStackTrace(); 
			}
			
		}
		
		else if(e.getSource() == login){
			String usernameTemp = this.username.getText();
			String passwordTemp = new String(this.password.getPassword());
			try {
				if(RemoteHelper.getInstance().getUserService().login(usernameTemp, passwordTemp.toString())){
					this.setVisible(false);
					this.user = usernameTemp;
					mainFrame.textArea.setEditable(true);
					mainFrame.inputArea.setEditable(true);
					mainFrame.loginMenuItem.setEnabled(false);
					mainFrame.logoutMenuItem.setEnabled(true);
					this.login.setEnabled(false);
					this.register.setEnabled(false);
					mainFrame.frame.setTitle("BF Client" + "/" + this.user);
				}
				else{
					JOptionPane.showMessageDialog(this, "Wrong Password!!", "Erroe!", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
		else if(e.getSource() == logout){
			this.mainFrame.versionMenu.removeAll();
			this.mainFrame.save = 0;
			this.user = "";
			this.username.setText("");
			this.password.setText("");
			this.login.setEnabled(true);
			this.register.setEnabled(true);
			mainFrame.textArea.setEditable(false);
			mainFrame.inputArea.setEditable(false);
			mainFrame.textArea.setText("");
			mainFrame.inputArea.setText("");
			mainFrame.loginMenuItem.setEnabled(true);
			mainFrame.logoutMenuItem.setEnabled(false);
			this.logout.setEnabled(false);
			mainFrame.frame.setTitle("BF Client");
		}
		
	}

}
