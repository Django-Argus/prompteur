package net.argus.prompteur.gui;

import javax.swing.JFrame;

import net.argus.file.Properties;
import net.argus.prompteur.Timer;

public class MonitorFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4356467888533392980L;
	
	private MonitorPanel pan;
		
	public MonitorFrame(Timer timer, Properties prop) {
		setAlwaysOnTop(prop.getBoolean("monitor.frame.alwaysontop"));
		setSize(prop.getDimension("monitor.frame.size"));
		setResizable(prop.getBoolean("monitor.frame.resizable"));
		
		setTitle("Prompteur Monitor");
		
		pan = new MonitorPanel(timer, prop);
		
		setContentPane(pan);
	}
	
	public void update() {
		pan.update();
	}

}
