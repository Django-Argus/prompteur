package net.argus.prompteur.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.JOptionPane;

import net.argus.beta.com.CardinalSocket;
import net.argus.beta.com.client.Client;

public class PrompteurClient extends Client {

	public PrompteurClient(String host, int port) throws UnknownHostException {
		super(host, port);
	}
	
	@Override
	public CardinalSocket open() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		CardinalSocket sock = super.open();
		JOptionPane.showMessageDialog(null, "The client was connected", "NetworkSystem", JOptionPane.INFORMATION_MESSAGE);
		return sock;
	}

}
