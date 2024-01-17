package net.argus.prompteur.net;

import java.io.IOException;

import net.argus.beta.com.CardinalSocket;
import net.argus.cjson.CJSON;
import net.argus.cjson.CJSONParser;
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
		
		while(!socket.isClosed()) {
			try {
				String nLine = socket.nextString();
				execut(CJSONParser.getCJSON(nLine));
			}catch(IOException | InterruptedException e) {Debug.log("Error in client process", Info.ERROR);}
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
				
			case EventNetworkSystem.SEND_OFF_Y:
				promptFrame.getPromptPanel().caughtUpOffY(cjson.getInt("offy"));
				break;

		}
	}
	
	public PromptFrame getPromptFrame() {
		return promptFrame;
	}
	
}
