package net.argus.prompteur.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JFrame;

import net.argus.file.Properties;
import net.argus.prompteur.Page;
import net.argus.prompteur.net.NetworkSystem;

public class PromptFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2453487625181132844L;
	
	private PromptPanel pan;
	
	private NetworkSystem netSys;

	public PromptFrame(List<Page> pages, NetworkSystem netSys, Properties prop) {
		setTitle("Prompteur");
		setDefaultCloseOperation(3);
		setAlwaysOnTop(prop.getBoolean("prompteur.frame.alwaysontop"));
		setSize(prop.getDimension("prompteur.frame.size"));
		setResizable(prop.getBoolean("prompteur.frame.resizable"));
		setLocationRelativeTo(null);
		pan = new PromptPanel(this, pages, prop);
		
		addKeyListener(getKeyListener());
		setContentPane(pan);
		
		this.netSys = netSys;
	}
	
	private KeyListener getKeyListener() {
    	return new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if(e.getKeyCode() == KeyEvent.VK_LESS) {
						pan.changeDirection();
					}
					
					if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_M) {
						pan.getTimer().getMonitorFrame().setVisible(true);
					}
					
					if(e.getKeyCode() == KeyEvent.VK_SPACE) {
						if(e.isControlDown())
							pan.getTimer().setSpeed(0);
							pan.getTimer().waitStart();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						pan.reset();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_DELETE) {
						pan.getTimer().setSpeed(0);
					}
					
					if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
						netSys.setType(NetworkSystem.SERVER_TYPE);
						
						netSys.getServer().open();
					}
					
				}catch(InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		};
    }
	
	public boolean isNetworkReady() {
		return netSys!=null;
	}
	
	public NetworkSystem getNetworkSystem() {
		return netSys;
	}

}
