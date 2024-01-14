package net.argus.prompteur;

import net.argus.file.Properties;
import net.argus.prompteur.gui.MonitorFrame;
import net.argus.prompteur.gui.PromptPanel;
import net.argus.util.ThreadManager;

public class Timer extends Thread {
	
	private boolean wait = true;
	private int speed = 0;
	private PromptPanel panel;
	
	private int maxWaitFPS, minFPS, maxFPS, maxSpeed;
	
	private float m;
	
	private MonitorFrame monitorFrame;
		
	public Timer(PromptPanel panel, Properties prop) {
		this.panel = panel;
		this.maxWaitFPS = prop.getInt("prompteur.maxwaitfps");
		this.minFPS = prop.getInt("prompteur.minfps");
		this.maxFPS = prop.getInt("prompteur.maxfps");
		this.maxSpeed = prop.getInt("prompteur.maxspeed");
		
		this.m = (float) ((float) (maxFPS - minFPS) / (float) maxSpeed);

		this.monitorFrame = new MonitorFrame(this, prop);
		monitorFrame.setVisible(true);
	}
	
	@Override
	public void run() {
		while(true) {
			if(!wait && speed != 0)
				panel.repaint();
				
			monitorFrame.update();

			ThreadManager.sleep((int) (1000 / (wait?maxWaitFPS:(m * speed + minFPS))));
		}
	}
	
	public void addSpeed(int add) {
		if(speed + add < 0)
			speed = 0;
		else if(speed + add > maxSpeed)
			speed = maxSpeed;
		else
			speed += add;
	}
	
	public void setSpeed(int speed) {
		if(speed < 0 || speed > maxSpeed)
			throw new IllegalArgumentException("speed must be between [0;" + maxSpeed + "]");
		
		this.speed = speed;
	}
	
	public void waitStart() throws InterruptedException {
		wait = !wait;
	}
	
	public int getSpeed() {
		return wait?0:speed;
	}
	public int getRealSpeed() {
		return speed;
	}
	
	public boolean isWait() {
		return wait;
	}
	
	public int getDirection() {
		return panel.getDirection();
	}
	
	public PromptPanel getPromptPanel() {
		return panel;
	}
	
	public MonitorFrame getMonitorFrame() {
		return monitorFrame;
	}

}
