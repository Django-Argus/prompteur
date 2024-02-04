package net.argus.prompteur.gui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JFrame;

import net.argus.file.Properties;
import net.argus.prompteur.Page;
import net.argus.prompteur.net.NetworkSystem;
import net.argus.prompteur.net.PackagePrefab;
import net.argus.prompteur.net.event.EventNetworkSystem;
import net.argus.prompteur.net.event.NetworkSystemEvent;
import net.argus.prompteur.net.event.NetworkSystemListener;

public class PromptFrame extends JFrame implements KeyListener, MouseWheelListener, NetworkSystemListener, ComponentListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2453487625181132844L;
	
	private PromptPanel pan;
	
	private NetworkSystem netSys;
	
	private boolean slave, fullScreen;

	public PromptFrame(List<Page> pages, NetworkSystem netSys, boolean slave, Dimension frameSize, int offY, int speed, int direction, boolean mirror, boolean playing, Properties prop) {
		this.netSys = netSys;
		this.slave = slave;
		
		setTitle();
		setDefaultCloseOperation(3);
		setAlwaysOnTop(prop.getBoolean("prompteur.frame.alwaysontop"));
		setSize(frameSize);
		if(!slave)
			setResizable(prop.getBoolean("prompteur.frame.resizable"));
		else
			setResizable(false);
		setLocationRelativeTo(null);
		pan = new PromptPanel(this, pages, slave, offY, speed, direction, mirror, playing, prop);
		
		addKeyListener(this);
		addMouseWheelListener(this);
		
		netSys.addNetworkSystemListener(this);
		addComponentListener(this);
		setContentPane(pan);
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(slave)
			return;
		
		changeSpeed(-e.getWheelRotation());
	}
	
    @Override
    public void keyTyped(KeyEvent e) {}
			
    @Override
    public void keyReleased(KeyEvent e) {}
			
    @Override
    public void keyPressed(KeyEvent e) {
    	try {
    		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_M) {
    			if(e.isAltDown())
    				pan.getTimer().getMonitorFrame().setVisible(true);
    			else {
    				pan.setMirror(!pan.isMirror());
    				pan.repaint();
    			}
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
    			setTitle();
    		}
    		
    		if(e.getKeyCode() == KeyEvent.VK_F11) {
    			enterFullScreenMod();
    		}
    		
    	}catch(InterruptedException e1) {
    		e1.printStackTrace();
    	}
    }
    
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	
	@Override
	public void componentResized(ComponentEvent e) {
		startNetworkEvent(EventNetworkSystem.CHANGE_SIZE);
	}


	@Override
	public void changeDirection(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getChangeDirectionPackage(e.getDirection()));
	}

	@Override
	public void startStop(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getStartStopPackage(e.isPlay()));
	}

	@Override
	public void reset(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getResetPackage());
	}

	@Override
	public void resetSpeed(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getResetPackage());
	}
	
	@Override
	public void changeSpeed(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getChangeSpeedPackage(e.getSpeed()));
	}
	
	@Override
	public void changeSize(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getChangeSizePackage(e.getFrameSize()));
	}
	
	@Override
	public void changePage(NetworkSystemEvent e) {
		netSys.getServer().sendToAll(PackagePrefab.getChangePagePackage(e.getPageIndex()));
	}


	
	public void changeDirection() {
		pan.changeDirection();
		startNetworkEvent(EventNetworkSystem.CHANGE_DIRECTION);
	}
	
	public void startStop() throws InterruptedException {
		pan.getTimer().waitStart();
		startNetworkEvent(EventNetworkSystem.START_STOP);

	}
	
	public void reset() throws InterruptedException {
		pan.reset();
		startNetworkEvent(EventNetworkSystem.RESET);
	}
	
	public void resetSpeed() {
		pan.getTimer().setSpeed(0);
		startNetworkEvent(EventNetworkSystem.RESET_SPEED);
	}
	
	public void changeSpeed(int addsp) {
		pan.getTimer().addSpeed(addsp);
		startNetworkEvent(EventNetworkSystem.CHANGE_SPEED);
	}
	
	public boolean isNetworkReady() {
		return netSys!=null;
	}
	
	public void enterFullScreenMod() {
		GraphicsDevice device = getGraphicsConfiguration().getDevice();
		device.setFullScreenWindow(fullScreen?null:this);
		
		startNetworkEvent(EventNetworkSystem.CHANGE_SIZE);
		
		fullScreen = !fullScreen;
	}
	
	protected void startNetworkEvent(int event) {
		if(pan == null)
			return;

		netSys.startEvent(event, new NetworkSystemEvent(this, this.getSize(), 
				pan.getSelectedPageIndex(), pan.getDirection(), pan.getTimer().getRealSpeed(), 
				pan.getOffY(), !pan.getTimer().isWait()));
	}
	
	private void setTitle() {
		setTitle("[Prompteur] " + (slave?("slave of " + netSys.getClient().getHost().getHostName()):(netSys.getType()==NetworkSystem.SERVER_TYPE?"master":"")));
	}
	
	public NetworkSystem getNetworkSystem() {
		return netSys;
	}
	
	public PromptPanel getPromptPanel() {
		return pan;
	}
	
	public boolean isFullScreen() {
		return fullScreen;
	}

}
