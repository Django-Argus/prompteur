package net.argus.prompteur;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.argus.file.CardinalFile;
import net.argus.file.Filter;
import net.argus.gui.OptionPane;
import net.argus.prompteur.net.NetworkSystem;
import net.argus.util.FileChooser;

public class Loader extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863630966325733046L;

	public static final Filter PROMPT_FILTER = new Filter("prompt", "Prompteur file");
	public static final Filter PROMPTPATTERN_FILTER = new Filter("promptpattern", "Prompteur pattern file");

	private FileChooser fileChooser = new FileChooser(defaultPath.getAbsolutePath(), PROMPT_FILTER);
	private FileChooser patternFileChooser = new FileChooser(defaultPath.getAbsolutePath(), PROMPTPATTERN_FILTER);
	
	private static File defaultPath = new File(".");
	
	private List<File> openedList = new ArrayList<File>();
	
	private List<File> files = new ArrayList<File>();
    private DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	private JPanel mainPan;
	private JScrollPane pan;
	
	private JPanel eastPanel;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton upButton;
	private JButton downButton;
	
	private JPanel southPanel;
	
	private JButton loadButton;
	private JButton connectButton;
	private JButton startButton;
	
	private JList<String> list;
	
	private NetworkSystem netSys;
	
	public Loader() {
		setTitle("Loader");
		setDefaultCloseOperation(3);
		setSize(600, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		
		netSys = new NetworkSystem();
		
		mainPan = new JPanel();
		mainPan.setLayout(new BorderLayout());
		
		eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(4, 1));
		
		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2));
		
		
		addButton = new JButton("+");
		addButton.addActionListener(getAddActionListener());
		
		removeButton = new JButton("-");
		removeButton.addActionListener(getRemoveActionListener());
		
		upButton = new JButton("↑");
		upButton.addActionListener(getUpActionListener());
		downButton = new JButton("↓");
		downButton.addActionListener(getDownActionListener());
		
		
		loadButton = new JButton("Load");
		loadButton.addActionListener(getLoadActionListener());
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(getConnectActionListener());
		
		startButton = new JButton("Start");
		startButton.addActionListener(getStartActionListener());
		

		list = new JList<String>(listModel);
		
		pan = new JScrollPane(list);
		
		eastPanel.add(addButton);
		eastPanel.add(removeButton);
		eastPanel.add(upButton);
		eastPanel.add(downButton);
		
		southPanel.add(loadButton);
		southPanel.add(connectButton);
		southPanel.add(startButton);
		
		mainPan.add(BorderLayout.CENTER, pan);
		mainPan.add(BorderLayout.EAST, eastPanel);
		mainPan.add(BorderLayout.SOUTH, southPanel);
		
		setContentPane(mainPan);
	}
	
	private ActionListener getAddActionListener() {
		return (e) -> {
			addFile(showFileChooser());
		};
	}
	
	private ActionListener getRemoveActionListener() {
		return (e) -> {
			int index = list.getSelectedIndex();
			if(index == -1)
				return;
			
			files.remove(index);
			listModel.remove(index);
		};
	}
	
	private ActionListener getUpActionListener() {
		return (e) -> {
			int index = list.getSelectedIndex();
			if(index <= 0)
				return;
			
			File temp = files.get(index);
			files.set(index, files.get(index-1));
			files.set(index-1, temp);
			
			listModel.set(index, getPageName(files.get(index)));
			listModel.set(index-1, getPageName(files.get(index-1)));

			list.setSelectedIndex(index-1);
		};
	}
	
	private ActionListener getDownActionListener() {
		return (e) -> {
			int index = list.getSelectedIndex();
			if(index >= files.size() - 1 || index == -1)
				return;
			
			File temp = files.get(index);
			files.set(index, files.get(index+1));
			files.set(index+1, temp);
			
			listModel.set(index, getPageName(files.get(index)));
			listModel.set(index+1, getPageName(files.get(index+1)));

			list.setSelectedIndex(index+1);
		};
	}
	
	private ActionListener getStartActionListener() {
		return (e) -> {
			if(files.size() <= 0)
				return;
			
			int res = JOptionPane.NO_OPTION;
			if(modified())
				res = OptionPane.showDialog(this, "Do you want to save this configuration", "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		
			switch(res) {
				case JOptionPane.YES_OPTION:
					savePattern();
				case JOptionPane.NO_OPTION:
					setVisible(false);
					Main.start(files, netSys);
					break;
					
				case JOptionPane.CANCEL_OPTION:
				case JOptionPane.CLOSED_OPTION:
					break;
			}
		};
	}
	
	private ActionListener getLoadActionListener() {
		return (e) -> {
			File f = showLoadFileChooser();
			if(f == null)
				return;
			String txt = Main.readText(f);
			String[] spl = txt.split("\n");
			
			try {
				Path baseFolderPath = Paths.get(f.getParent()).toRealPath();
			
				for(String str : spl) {
					File file = baseFolderPath.resolve(str).toFile();
					openedList.add(file);
					addFile(file);
				}
				
			}catch(IOException e1) {e1.printStackTrace();}
		};
	}
	
	private ActionListener getConnectActionListener() {
		return (e) -> {
			netSys.setType(NetworkSystem.CLIENT_TYPE);
			
			try {
				netSys.getClient().open();
				// get filename and content
				//Main.start0(files, netSys);
			}catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e1) {
				e1.printStackTrace();
			}
		};
	}
	
	private boolean modified() {
		if(files.size() != openedList.size())
			return true;
		
		for(int i = 0; i < files.size(); i++)
			if(!files.get(i).equals(openedList.get(i)))
				return true;
		return false;
	}
	
	public static String getPageName(File file) {
		return file.getName().substring(0, file.getName().indexOf('.'));
	}
	
	private File showFileChooser() {
		return fileChooser.showOpenFile(this);
	}
	
	private void savePattern() {
		File f = showSaveFileChooser();
		
		if(!f.getName().endsWith(".promptpattestart0rn"))
			f = new File(f.getAbsoluteFile() + ".promptpattern");
		
		try {
		
			CardinalFile file = new CardinalFile(f);
			String[] strs = new String[files.size()];
			
			if(!file.exists())
				file.createFile();

			Path baseFolderPath = Paths.get(f.getParent()).toRealPath();
			
			for(int i = 0; i < files.size(); i++) {
				Path filePathObject = Paths.get(files.get(i).getAbsolutePath()).toRealPath();
				String path = "";
				if(filePathObject.startsWith(baseFolderPath)) 
					path = baseFolderPath.relativize(filePathObject).toString();
				else
					path = filePathObject.toString();
				
				strs[i] = path;
			}
			file.write(strs);
		
		}catch(IOException e) {e.printStackTrace(); OptionPane.showErrorDialog(this, "Prompteur loader", e);}
	}
	
	private File showSaveFileChooser() {
		return patternFileChooser.showSaveFile(this);
	}
	
	private File showLoadFileChooser() {
		return patternFileChooser.showOpenFile(this);
	}
	
	private void addFile(File file) {
		if(file == null)
			return;
		
		if(files.contains(file)) {
			list.setSelectedIndex(files.indexOf(file));
			return;
		}
		
		files.add(file);
		listModel.addElement(getPageName(file));	
	}
	
}
