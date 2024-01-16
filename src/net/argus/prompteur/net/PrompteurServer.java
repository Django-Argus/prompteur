package net.argus.prompteur.net;

import java.io.IOException;

import javax.swing.JOptionPane;

import net.argus.beta.com.CardinalSocket;
import net.argus.beta.com.server.Server;
import net.argus.event.com.server.ServerEvent;
import net.argus.event.com.server.ServerListener;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class PrompteurServer extends Server implements ServerListener {
	
	private NetworkSystem netSys;

	public PrompteurServer(int port, NetworkSystem netSys) throws IOException {
		super(port);
		this.netSys = netSys;
		addServerListener(this);
	}
	
	@Override
	public void open() {
		super.open();
		
		JOptionPane.showMessageDialog(null, "The server was opened on port " + getPort(), "NetworkSystem", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void newClient(ServerEvent e) {
		try {
			sendDataToNexClient(e.getSocket());
		}catch(IOException e1) {
			Debug.log("Error with new client, connection close", Info.ERROR);
			
			try {
				e.getSocket().close();
			} catch (IOException e2) {
				Debug.log("Error when closing the connection");
			}
		}
	}
	
	private void sendDataToNexClient(CardinalSocket sock) throws IOException {
		String pack = PackagePrefab.getConnectionPackage(netSys.getPromptPanel().getPages(), netSys.getPromptPanel().getOffY());
		
		sock.send(pack);
	}

}
