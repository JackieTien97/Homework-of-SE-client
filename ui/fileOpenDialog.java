package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import rmi.RemoteHelper;

public class fileOpenDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5166017045072843884L;
	JScrollPane filejsp;
	JList<String> filejList;
	
	public fileOpenDialog(String[] fileName, String[] fileName1, String user, MainFrame mainFrame){
		filejList = new JList<>(fileName);
		filejsp = new JScrollPane(filejList);
		this.add(filejsp);
		setSize(300,400);
		setLocation(600,200);
		setResizable(false);
	    filejList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(filejList.getSelectedIndex() != -1) {
					if(e.getClickCount() == 2){
						mainFrame.versionMenu.removeAll();
						String fileNameSelected = filejList.getSelectedValue();
						int temp = 0;
						for(String i : fileName1){
							if(i.startsWith(user + "_" + fileNameSelected + "_")){
								JMenuItem version = new JMenuItem("Version" + temp);
								mainFrame.versionMenu.add(version);
								mainFrame.versionMenu.addSeparator();
								version.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										mainFrame.outputArea.setText("");
										String cmd = e.getActionCommand();
										String file = mainFrame.filename + "_" + cmd;
										try{
											String fileTemp = RemoteHelper.getInstance().getIOService().readFile(mainFrame.loginFrame.user, file);
											mainFrame.filetemp = fileTemp;
											mainFrame.flag = 0;
											mainFrame.undoList.clear();
											mainFrame.redoList.clear();
											mainFrame.textArea.setText(fileTemp);
										}catch(RemoteException e1){
											e1.printStackTrace();
										}
										mainFrame.frame.setTitle("BF Client" + "/" + mainFrame.loginFrame.user + "/" + file);
									}
								});
								temp++;
							}
						}
						try {
							String file = RemoteHelper.getInstance().getIOService().readFile(user, fileNameSelected + "_Version" + (temp - 1));
							mainFrame.save = temp;
							mainFrame.textArea.setText(file);
							mainFrame.filetemp = file;
							mainFrame.flag = 0;
							mainFrame.undoList.clear();
							mainFrame.redoList.clear();
							mainFrame.outputArea.setText("");
							mainFrame.filename = fileNameSelected;
							fileOpenDialog.this.setVisible(false);
							mainFrame.frame.setTitle("BF Client" + "/" + mainFrame.loginFrame.user + fileNameSelected + "_Version" + (temp - 1));
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
		});
	}
	
	
}
