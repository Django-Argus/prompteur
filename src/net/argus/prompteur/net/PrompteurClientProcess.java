package net.argus.prompteur.net;

import java.io.IOException;

import javax.swing.JOptionPane;

import net.argus.beta.com.CardinalSocket;
import net.argus.cjson.CJSON;
import net.argus.cjson.CJSONParser;
import net.argus.gui.OptionPane;
import net.argus.prompteur.Main;
import net.argus.prompteur.gui.PromptFrame;
import net.argus.prompteur.net.event.EventNetworkSystem;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class PrompteurClientProcess extends Thread {
	
	private PromptFrame promptFrame;
	private CardinalSocket socket;
	
	public PrompteurClientProcess(PromptFrame promptFrame, CardinalSocket socket) {
		this.promptFrame = promptFrame;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("prompteur-client-process");
		
		try {
			while(!socket.isClosed()) {
					String nLine = socket.nextString();
					execut(CJSONParser.getCJSON(nLine));
			}
		}catch(IOException | InterruptedException e) {
			Debug.log("Error in client process exiting", Info.ERROR);
			OptionPane.showDialog(null, "The master prompter no longer responds", "Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			
			try {promptFrame.reset();}
			catch(InterruptedException e1) {e1.printStackTrace();}
			
			promptFrame.setVisible(false);
			promptFrame.getPromptPanel().getTimer().getMonitorFrame().setVisible(false);
			Main.getLoader().setVisible(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void execut(CJSON cjson) throws InterruptedException {
		switch (cjson.getInt("type")) {
			case EventNetworkSystem.CHANGE_DIRECTION:
				promptFrame.changeDirection();
				break;
				
			case EventNetworkSystem.CHANGE_SPEED:
				promptFrame.getPromptPanel().getTimer().setSpeed(cjson.getInt("speed"));
				break;
				
			case EventNetworkSystem.RESET:
				promptFrame.reset();
				break;
				
			case EventNetworkSystem.RESET_SPEED:
				promptFrame.resetSpeed();
				break;
				
			case EventNetworkSystem.START_STOP:
				promptFrame.startStop();
				break;
				
			case EventNetworkSystem.CHANGE_SIZE:
				promptFrame.setSize(cjson.getInt("width"), cjson.getInt("height"));
				break;
				
			case EventNetworkSystem.CHANGE_PAGE:
				promptFrame.getPromptPanel().getTimer().getMonitorFrame().getMonitorPanel().selectPage(cjson.getInt("page_index"));
				break;
				
			case EventNetworkSystem.SEND_OFF_Y:
				promptFrame.getPromptPanel().caughtUpOffY(cjson.getInt("offy"));
				break;

		}
	}
	
	public PromptFrame getPromptFrame() {
		return promptFrame;
	}
	
}
