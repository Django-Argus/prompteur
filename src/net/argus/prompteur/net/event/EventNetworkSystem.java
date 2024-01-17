package net.argus.prompteur.net.event;

import net.argus.event.Event;

public class EventNetworkSystem extends Event<NetworkSystemListener> {
	
	public static final int CHANGE_DIRECTION = 0;
	public static final int START_STOP = 1;
	public static final int RESET = 2;
	public static final int RESET_SPEED = 3;
	
	public static final int CHANGE_SPEED = 4;
	public static final int CHANGE_SIZE = 6;	
	public static final int CHANGE_PAGE = 7;
	
	
	@Deprecated
	public static final int SEND_OFF_Y = 5;

	@Override
	public void event(NetworkSystemListener listener, int event, Object... objs) {
		switch(event) {
			case CHANGE_DIRECTION:
				listener.changeDirection((NetworkSystemEvent) objs[0]);
				break;
				
			case START_STOP:
				listener.startStop((NetworkSystemEvent) objs[0]);
				break;
				
			case RESET:
				listener.reset((NetworkSystemEvent) objs[0]);
				break;
				
			case RESET_SPEED:
				listener.resetSpeed((NetworkSystemEvent) objs[0]);
				break;
				
			case CHANGE_SPEED:
				listener.changeSpeed((NetworkSystemEvent) objs[0]);
				break;
				
			case CHANGE_SIZE:
				listener.changeSize((NetworkSystemEvent) objs[0]);
				break;
				
			case CHANGE_PAGE:
				listener.changePage((NetworkSystemEvent) objs[0]);
				break;
		}
	}

}
