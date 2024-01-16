package net.argus.prompteur.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelListener;
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
	
	private boolean slave;

	public PromptFrame(List<Page> pages, NetworkSystem netSys, boolean slave, int offY, Properties prop) {
		this.netSys = netSys;
		this.slave = slave;
		
		setTitle("Prompteur");
		setDefaultCloseOperation(3);
		setAlwaysOnTop(prop.getBoolean("prompteur.frame.alwaysontop"));
		setSize(prop.getDimension("prompteur.frame.size"));
		setResizable(prop.getBoolean("prompteur.frame.resizable"));
		setLocationRelativeTo(null);
		pan = new PromptPanel(this, pages, offY, prop);
		
		addKeyListener(getKeyListener());
		addMouseWheelListener(getMouseWheelListener());
		
		setContentPane(pan);
		
	}
	
    private MouseWheelListener getMouseWheelListener() {
    	return (e) -> {
    		if(slave)
    			return;
    		
    		pan.getTimer().addSpeed(-e.getWheelRotation());
    	};
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
					if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_M) {
						pan.getTimer().getMonitorFrame().setVisible(true);
					}
					
					if(slave)
						return;
					
					if(e.getKeyCode() == KeyEvent.VK_LESS) {
						changeDirection();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_SPACE) {
						if(e.isControlDown())
							resetSpeed();
						startStop();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						reset();
					}
					
					if(e.getKeyCode() == KeyEvent.VK_DELETE) {
						resetSpeed();
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
	
	public void changeDirection() {
		pan.changeDirection();
	}
	
	public void startStop() throws InterruptedException {
		pan.getTimer().waitStart();
	}
	
	public void reset() throws InterruptedException {
		pan.reset();
	}
	
	public void resetSpeed() {
		pan.getTimer().setSpeed(0);

	}
	
	public boolean isNetworkReady() {
		return netSys!=null;
	}
	
	public NetworkSystem getNetworkSystem() {
		return netSys;
	}

}
