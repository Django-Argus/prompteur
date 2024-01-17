package net.argus.prompteur.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import net.argus.file.Properties;
import net.argus.prompteur.Page;
import net.argus.prompteur.Timer;

public class MonitorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -555685532076364481L;
	
	private Timer timer;
	
	private JScrollPane scroll;
	private JList<String> pageList;
	
	private DefaultListModel<String> dataModel = new DefaultListModel<String>();
	
	private JLabel waitLabel = new JLabel();
	private JLabel speedLabel  = new JLabel();
	private JLabel directionLabel = new JLabel();
	private JProgressBar progressBar = new JProgressBar();
	
	private Color background;
	
	@SuppressWarnings("deprecation")
	public MonitorPanel(Timer timer, boolean slave, Properties prop) {
		this.timer = timer;
		setLayout(new BorderLayout());
		background = prop.getColor("monitor.background");
		setBackground(background);
		
		update();
		updateList();
		
		pageList = new JList<String>(dataModel);
		pageList.addListSelectionListener(getListSelectionListener());
		pageList.setSelectedIndex(0);
		
		if(slave)
			pageList.setEnabled(false);
		
		scroll = new JScrollPane(pageList);
		
		JPanel centerPan = new JPanel();
		centerPan.setBackground(background);
		
		waitLabel.setFont(prop.getFont("monitor.wait.font"));
		speedLabel.setFont(prop.getFont("monitor.speed.font"));
		directionLabel.setFont(prop.getFont("monitor.direction.font"));
		progressBar.setStringPainted(prop.getBoolean("monitor.progressbar.text"));
		
		centerPan.add(speedLabel);
		
		add(BorderLayout.NORTH, scroll);
		add(BorderLayout.WEST, waitLabel);
		add(BorderLayout.CENTER, centerPan);
		add(BorderLayout.EAST, directionLabel);
		add(BorderLayout.SOUTH, progressBar);
	}
	
	private ListSelectionListener getListSelectionListener() {
		return (e) -> {
			if(e.getValueIsAdjusting())
				return;
			
			int index = pageList.getSelectedIndex();
			timer.getPromptPanel().setSelectedPage(index);
			timer.getPromptPanel().repaint();
		};
	}
	
	public void update() {
		waitLabel.setText(timer.isWait()?"Waiting":"Playing");
		speedLabel.setText(Integer.toString(timer.getRealSpeed()));
		directionLabel.setText(timer.getDirection()==PromptPanel.FORWARD?"Forward":"Backward");
		
		int nbLine = timer.getPromptPanel().getCountLine();
		int val = timer.getPromptPanel().getLastLineIndex();
		progressBar.setMaximum(nbLine);
		progressBar.setValue(val);

		if(nbLine != 0)
			progressBar.setString((int) ((float) ((float) (val - 1) / (float) nbLine) * 100) + "%");
		else
			progressBar.setString("?");
	}
	
	public void updateList() {
		List<Page> pages = timer.getPromptPanel().getPages();
		for(Page p : pages)
			dataModel.addElement(p.getName());
	}
	
	public void selectPage(int pageIndex) {
		pageList.setSelectedIndex(pageIndex);
	}
	
	public int getPageIndex() {
		return pageList.getSelectedIndex();
	}

}
