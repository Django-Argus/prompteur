package net.argus.prompteur.net;

import java.io.IOException;

import javax.swing.JOptionPane;

import net.argus.prompteur.gui.PromptPanel;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class NetworkSystem {
	
	public static final int PROMPTEUR_DEFAULT_PORT = 54678;
	
	public static final int UNKNOWN_TYPE = -1;
	
	public static final int SERVER_TYPE = 0;
	public static final int CLIENT_TYPE = 1;
	
	private int type = UNKNOWN_TYPE;
	
	private PrompteurServer server = null;
	private PrompteurClient client = null;
	
	private PromptPanel promptPanel;
	
	public NetworkSystem() {}
	
	public NetworkSystem(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		if(this.type == UNKNOWN_TYPE) {
			this.type = type;
			create();
		}
		
	}
	
	private void create() {
		try {
			switch(type) {
				case SERVER_TYPE:
					server = new PrompteurServer(PROMPTEUR_DEFAULT_PORT, this);
					break;
					
				case CLIENT_TYPE:
					String host = JOptionPane.showInputDialog(null, "Prompteur host", "Network System", JOptionPane.QUESTION_MESSAGE);
					int port = PROMPTEUR_DEFAULT_PORT;
					if(host.indexOf(':') > -1) {
						String[] str = host.split(":");
						
						host = str[0];
						port = Integer.valueOf(str[1]);
					}
					
					client = new PrompteurClient(host, port);
					break;
			}
		}catch(IOException e) {
			Debug.log("Network creation error", Info.ERROR);
			type = UNKNOWN_TYPE;
		}
	}
	
	public void clear() {
		switch(type) {
			case SERVER_TYPE:
				server = null;
				break;
			case CLIENT_TYPE:
				client = null;
				break;
		}
		type = UNKNOWN_TYPE;
	}
	
	public PrompteurClient getClient() {
		return client;
	}
	
	public PrompteurServer getServer() {
		return server;
	}
	
	public boolean isNetworkTypeSelected() {
		return type!=UNKNOWN_TYPE;
	}
	
	public void setPromptPanel(PromptPanel promptPanel) {
		this.promptPanel = promptPanel;
	}
	
	public PromptPanel getPromptPanel() {
		return promptPanel;
	}
	
}
