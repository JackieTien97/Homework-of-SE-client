package ui;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import rmi.RemoteHelper;


public class MainFrame extends JFrame implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1023866620193311338L;
	public JTextArea textArea;
	public JTextArea inputArea;
	public JTextArea outputArea;
	public LoginFrame loginFrame;
	public JMenuItem loginMenuItem;
	public JMenuItem logoutMenuItem;
	private JPopupMenu popMenu;
	private JSplitPane jsp;
	private JSplitPane jsp1;
	public JFrame frame;
	JMenu versionMenu;
	String filename = "";
	int save = 0;
	
	
	String filetemp = "";
	int flag = 0;
	long time = System.currentTimeMillis();
	LinkedList<String> undoList = new LinkedList<>();
	LinkedList<String> redoList = new LinkedList<>();


	public MainFrame() {
		// 鍒涘缓绐椾綋
		frame = new JFrame();
		frame.setTitle("BF Client");
		
		

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		fileMenu.add(newMenuItem);
		fileMenu.addSeparator();
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		fileMenu.add(saveMenuItem);
		JMenu runMenu = new JMenu("Run");
		menuBar.add(runMenu);
		JMenuItem executeMenuItem = new JMenuItem("Execute");
		executeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
		runMenu.add(executeMenuItem);
		versionMenu = new JMenu("Version");
		menuBar.add(versionMenu);
		JMenu preferenceMenu = new JMenu("Preference");
		menuBar.add(preferenceMenu);
		JMenuItem colorOfFontMenuItem = new JMenuItem("Font Color");
		colorOfFontMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(MainFrame.this, "Choose the color of your font", textArea.getForeground());
				textArea.setForeground(c);
			}
		});
		preferenceMenu.add(colorOfFontMenuItem);
		preferenceMenu.addSeparator();
		JMenuItem colorOfBackgroundMenuItem = new JMenuItem("Background Color");
		preferenceMenu.add(colorOfBackgroundMenuItem);
		colorOfBackgroundMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(MainFrame.this, "Choose the color of your font", textArea.getForeground());
				textArea.setBackground(c);
			}
		});
//		preferenceMenu.addSeparator();
//		JMenuItem fontSizeMenuItem = new JMenuItem("Font Size");
//		preferenceMenu.add(fontSizeMenuItem);
		JMenu homeMenu = new JMenu("Home");
		menuBar.add(homeMenu);
		loginMenuItem = new JMenuItem("Login");
		homeMenu.add(loginMenuItem);
		homeMenu.addSeparator();
		logoutMenuItem = new JMenuItem("Logout");
		homeMenu.add(logoutMenuItem);
		
		frame.setJMenuBar(menuBar);
		
		
		

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new SaveActionListener());
		executeMenuItem.addActionListener(new MenuItemActionListener());
		loginMenuItem.addActionListener(new HomeActionListener());
		logoutMenuItem.addActionListener(new HomeActionListener());
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.addKeyListener(this);
		JScrollPane jscp = new JScrollPane(textArea);

		inputArea = new JTextArea();
		inputArea.setEditable(false);
		inputArea.setLineWrap(true);
		JScrollPane jscp1 = new JScrollPane(inputArea);

		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);
		JScrollPane jscp2 = new JScrollPane(outputArea);

		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jscp1, jscp2);
		jsp.setBorder(new LineBorder(Color.BLACK));
		jsp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jscp, jsp);
		jsp1.setBorder(new LineBorder(Color.BLACK));
		
		frame.add(jsp1);
		
		popMenu = new JPopupMenu();
		popMenu.setVisible(false);
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.addActionListener(new UndoActionListener());
		popMenu.add(undoMenuItem);
		popMenu.addSeparator();
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.addActionListener(new RedoActionListener());
		popMenu.add(redoMenuItem);
		popMenu.addSeparator();
		JMenuItem openMenuItem1 = new JMenuItem("Open");
		openMenuItem1.addActionListener(new MenuItemActionListener());
		popMenu.add(openMenuItem1);
		popMenu.addSeparator();
		JMenuItem saveMenuItem1 = new JMenuItem("Save");
		saveMenuItem1.addActionListener(new SaveActionListener());
		popMenu.add(saveMenuItem1);
		popMenu.addSeparator();
		JMenuItem executeMenuItem1 = new JMenuItem("Execute");
		executeMenuItem1.addActionListener(new MenuItemActionListener());
		popMenu.add(executeMenuItem1);
		textArea.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					popMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			
		});
		
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		frame.setLocation(120, 20);
		frame.setVisible(true);
		jsp1.setDividerLocation(0.8);
		jsp1.setDividerSize(2);
		jsp.setDividerLocation(0.5);
		jsp.setDividerSize(2);
		
		jsp1.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				
				jsp.setDividerLocation(0.5);
				jsp1.setDividerLocation(0.8);
			}
			
		});
		
	}
	
	public void setLoginFrame(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
	}

	class MenuItemActionListener implements ActionListener {
		/**
		 * 瀛愯彍鍗曞搷搴斾簨浠�
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("Open")) {
				try{
					String s = RemoteHelper.getInstance().getIOService().readFileList(loginFrame.user);
					String[] fileName1 = s.split(",");
					int length = 0;
					for(String i : fileName1){
						if(i.endsWith("Version0")){
							length++;
						}
					}
					String[] fileName = new String[length];
					int temp = 0;
					for(String i : fileName1){
						if(i.endsWith("Version0")){
							fileName[temp] = i.split("_")[1];
							temp++;
						}
					}
					fileOpenDialog fileop = new fileOpenDialog(fileName,fileName1,loginFrame.user, MainFrame.this);
					fileop.setVisible(true);
				}catch(RemoteException e1){
					e1.printStackTrace();
				}
				
			} 
			
			else if (cmd.equals("New")) {
				String filenameTemp = JOptionPane.showInputDialog("Please write your filename:");
				if(!(filenameTemp == null)){
					filename = filenameTemp;
					try{
						while(!RemoteHelper.getInstance().getIOService().creatFile(loginFrame.user, filename + "_Version0")){
							filename = JOptionPane.showInputDialog(MainFrame.this, "Your filename has existed! Please rename it!");
						}
						MainFrame.this.versionMenu.removeAll();
						filetemp = "";
						flag = 0;
						undoList.clear();
						redoList.clear();
						textArea.setText("");
						inputArea.setText("");
						outputArea.setText("");
						save = 0;
						MainFrame.this.frame.setTitle("BF Client" + "/" + loginFrame.user + "/" + filename + "_Version" + save);
					}catch(RemoteException e1){
						e1.printStackTrace(); 
					}
				}
				
			} 
			
			else if (cmd.equals("Execute")) {
				String code = textArea.getText();
				String param = inputArea.getText();
				String result;
				try {
					result = RemoteHelper.getInstance().getExecuteService().execute(code, param);
				} catch (RemoteException e1) {
					result = "Error!";
					e1.printStackTrace();
				}
				outputArea.setText("");
				outputArea.setText(result);
			}
		}
	}
	
	class HomeActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.equals("Login")){
				loginFrame.setVisible(true);
			}
			else if(cmd.equals("Logout")){
				loginFrame.setVisible(true);
				loginFrame.logout.setEnabled(true);
			}
		}
	}
	
	class UndoActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!MainFrame.this.undoList.isEmpty()){
				MainFrame.this.filetemp = MainFrame.this.textArea.getText();
				String temp = MainFrame.this.undoList.removeLast();
				MainFrame.this.textArea.setText(temp);
				MainFrame.this.redoList.add(MainFrame.this.filetemp);
				MainFrame.this.filetemp = temp;
			}
		}
		
	}
	
	class RedoActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!MainFrame.this.redoList.isEmpty()){
				MainFrame.this.filetemp = MainFrame.this.textArea.getText();
				String temp = MainFrame.this.redoList.removeLast();
				MainFrame.this.textArea.setText(temp);
				MainFrame.this.undoList.add(MainFrame.this.filetemp);
				MainFrame.this.filetemp = temp;
			}
			
		}
		
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!filename.equals("")){
				if(save == 0){
					String code = textArea.getText();
					try {
						RemoteHelper.getInstance().getIOService().writeFile(code, loginFrame.user, filename + "_Version" + save);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					JMenuItem version = new JMenuItem("Version" + save);
					MainFrame.this.versionMenu.add(version);
					MainFrame.this.versionMenu.addSeparator();
					version.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							String cmd = e.getActionCommand();
							String file = filename + "_" + cmd;
							try{
								String fileTemp = RemoteHelper.getInstance().getIOService().readFile(loginFrame.user, file);
								MainFrame.this.filetemp = filetemp;
								MainFrame.this.flag = 0;
								MainFrame.this.undoList.clear();
								MainFrame.this.redoList.clear();
								textArea.setText(fileTemp);
							}catch(RemoteException e1){
								e1.printStackTrace();
							}
							MainFrame.this.frame.setTitle("BF Client" + "/" + loginFrame.user + "/" + filename + "_" +cmd);
						}
					});
					MainFrame.this.frame.setTitle("BF Client" + "/" + loginFrame.user + "/" + filename + "_Version" + save);
					save++;
				}
				else{
					String code = textArea.getText();
					int former = save - 1;
					String codeOfFormer = "";
					boolean flag = false;
					while(former >= 0){
						try{
							codeOfFormer = RemoteHelper.getInstance().getIOService().readFile(loginFrame.user, filename + "_Version" + former);
						}catch(RemoteException e1){
							e1.printStackTrace();
						}
						if(code.equals(codeOfFormer)){
							flag = true;
							break;
						}
						former--;
					}
					if(!flag){
						try {
							RemoteHelper.getInstance().getIOService().writeFile(code, loginFrame.user, filename + "_Version" + save);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						JMenuItem version = new JMenuItem("Version" + save);
						MainFrame.this.versionMenu.add(version);
						MainFrame.this.versionMenu.addSeparator();
						version.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								String cmd = e.getActionCommand();
								String file = filename + "_" + cmd;
								try{
									String fileTemp = RemoteHelper.getInstance().getIOService().readFile(loginFrame.user, file);
									MainFrame.this.filetemp = filetemp;
									MainFrame.this.flag = 0;
									MainFrame.this.undoList.clear();
									MainFrame.this.redoList.clear();
									textArea.setText(fileTemp);
								}catch(RemoteException e1){
									e1.printStackTrace();
								}
								MainFrame.this.frame.setTitle("BF Client" + "/" + loginFrame.user + "/" + filename + "_" +cmd);
							}
						});
						MainFrame.this.frame.setTitle("BF Client" + "/" + loginFrame.user + "/" + filename + "_Version" + save);
						save++;
					}
				}	
			}
			else{
				JOptionPane.showMessageDialog(MainFrame.this, "Please create a new file before saving!");
			}
			
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		long temp = System.currentTimeMillis();
		
		if(!e.isControlDown()){
			if(flag == 0){
				undoList.add(filetemp);
				flag = 1;
				time = System.currentTimeMillis();
			}
			else{
				if((temp - time) > 2000){
					flag = 0;
					filetemp = textArea.getText();
					time = System.currentTimeMillis();
				}
				else{
					time = System.currentTimeMillis();
				}
			} 
		}
		
//		if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE && !e.isControlDown()){
//			if(flag == 0){
//				undoList.add(filetemp);
//				flag = 1;
//				time = System.currentTimeMillis();
//			}
//			else{
//				if((temp - time) > 2000){
//					flag = 0;
//					filetemp = textArea.getText();
//					time = System.currentTimeMillis();
//				}
//				else{
//					time = System.currentTimeMillis();
//				}
//			} 
//		}
		
//		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
//			undoList.add(filetemp);
//			flag = 0;
//			filetemp = textArea.getText();
//			time = System.currentTimeMillis();
//			System.out.println(filetemp);
//		}

//		else{
//			if(flag == 0){
//				undoList.add(filetemp);
//				flag = 1;
//				time = System.currentTimeMillis();
//			}
//			else{
//				if((temp - time) > 2000){
//					flag = 0;
//					filetemp = textArea.getText();
//					time = System.currentTimeMillis();
//				}
//				else{
//					time = System.currentTimeMillis();
//				}
//			}
//		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V){
			undoList.add(filetemp);
			flag = 0;
			filetemp = textArea.getText();
			System.out.println(filetemp);
			time = System.currentTimeMillis();
		}
//		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
//			undoList.add(filetemp);
//			flag = 0;
//			filetemp = textArea.getText();
//			time = System.currentTimeMillis();
//		}
	}

	
}
