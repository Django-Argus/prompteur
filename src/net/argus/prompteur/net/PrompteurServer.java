package net.argus.prompteur.net;

import java.io.IOException;

import javax.swing.JOptionPane;

import net.argus.beta.com.server.Server;
import net.argus.event.com.server.ServerEvent;
import net.argus.event.com.server.ServerListener;

public class PrompteurServer extends Server implements ServerListener {

	public PrompteurServer(int port) throws IOException {
		super(port);
		addServerListener(this);
	}
	
	@Override
	public void open() {
		super.open();
		
		JOptionPane.showMessageDialog(null, "The server was opened on port " + getPort(), "NetworkSystem", JOptionPane.INFORMATION_MESSAGE);

	}

	@Override
	public void newClient(ServerEvent e) {
		System.out.println(e.getSocket());
	}

}
