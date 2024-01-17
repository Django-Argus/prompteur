package net.argus.prompteur.net.event;

public class NetworkSystemEvent {
	
	private Object parent;
	private int direction, speed, offY;
	private boolean play;
	
	public NetworkSystemEvent(Object parent, int direction, int speed, int offY, boolean play) {
		this.parent = parent;
		this.direction = direction;
		this.speed = speed;
		this.offY = offY;
		this.play = play;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getOffY() {
		return offY;
	}
	
	public boolean isPlay() {
		return play;
	}

}
