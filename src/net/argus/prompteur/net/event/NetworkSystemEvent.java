package net.argus.prompteur.net.event;

import java.awt.Dimension;

public class NetworkSystemEvent {
	
	private Object parent;
	private Dimension frameSize;
	private int pageIndex, direction, speed, offY;
	private boolean play;
	
	public NetworkSystemEvent(Object parent, Dimension frameSize, int pageIndex, int direction, int speed, int offY, boolean play) {
		this.parent = parent;
		this.frameSize = frameSize;
		this.pageIndex = pageIndex;
		this.direction = direction;
		this.speed = speed;
		this.offY = offY;
		this.play = play;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public Dimension getFrameSize() {
		return frameSize;
	}
	
	public int getPageIndex() {
		return pageIndex;
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
