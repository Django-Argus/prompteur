package net.argus.prompteur.net.event;

import net.argus.util.Listener;

public interface NetworkSystemListener extends Listener {
	
	public void changeDirection(NetworkSystemEvent e);
	public void startStop(NetworkSystemEvent e);
	public void reset(NetworkSystemEvent e);
	public void resetSpeed(NetworkSystemEvent e);
	
	public void changeSpeed(NetworkSystemEvent e);


}
