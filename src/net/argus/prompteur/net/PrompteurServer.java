package net.argus.prompteur.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import net.argus.beta.com.CardinalSocket;
import net.argus.beta.com.server.Server;
import net.argus.event.com.server.ServerEvent;
import net.argus.event.com.server.ServerListener;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class PrompteurServer extends Server implements ServerListener {
	
	private NetworkSystem netSys;
	
	private List<CardinalSocket> clients = new ArrayList<CardinalSocket>();

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
			clients.add(e.getSocket());
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
		String pack = PackagePrefab.getConnectionPackage(netSys.getPromptPanel().getPages(),
				netSys.getPromptPanel().getOffY(), !netSys.getPromptPanel().getTimer().isWait(),
				netSys.getPromptPanel().getTimer().getSpeed(), netSys.getPromptPanel().getDirection());
		
		sock.send(pack);
	}
	
	public void sendToAll(String pack) {
		for(int i = 0; i < clients.size(); i++) {
			CardinalSocket sock = clients.get(i);
			try {
				sock.send(pack);
			}catch(IOException e) {Debug.log("Send error", Info.ERROR); clients.remove(sock);}
		}
	}
	
	public void sendOffY(int offY) {
		sendToAll(PackagePrefab.getOffYPackage(offY));
	}

}
