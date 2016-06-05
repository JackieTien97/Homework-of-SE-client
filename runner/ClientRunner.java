package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



import rmi.RemoteHelper;
import ui.LoginFrame;
import ui.MainFrame;

public class ClientRunner {
	private RemoteHelper remoteHelper;
	MainFrame mainFrame;
	LoginFrame loginFrame;
	
	public ClientRunner() {
		linkToServer();
		initGUI();
	}
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://localhost:8888/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		mainFrame = new MainFrame();
		loginFrame = new LoginFrame(mainFrame);
		mainFrame.setLoginFrame(loginFrame);
	}
	

	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		ClientRunner cr = new ClientRunner();
	}
}
