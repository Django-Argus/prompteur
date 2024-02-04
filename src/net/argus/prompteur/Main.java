package net.argus.prompteur;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.argus.file.Properties;
import net.argus.gui.OptionPane;
import net.argus.prompteur.gui.PromptFrame;
import net.argus.prompteur.gui.PromptPanel;
import net.argus.prompteur.net.NetworkSystem;

public class Main {
	
	private static Loader loader;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			loader = new Loader();
			loader.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
			OptionPane.showErrorDialog(null, "Prompteur", e);
			System.exit(1);
		}
	}
	
	public static Loader getLoader() {
		return loader;
	}
	
	public static PromptFrame start(List<File> files, boolean slave, NetworkSystem netSys) {
		List<Page> pages = new ArrayList<Page>();
		for(File file : files) {
			String txt = readText(file);
			pages.add(new Page(Loader.getPageName(file), txt));
		}
		
		Properties prop = new Properties(new File("config.properties"));
		if(!prop.exists()) {
			OptionPane.showErrorDialog(null, "Prompteur", new FileNotFoundException("config.properties not found"));
			System.exit(1);
			return null;
		}

		PromptFrame fen = new PromptFrame(pages, netSys, slave, prop.getDimension("prompteur.frame.size"), 0, 0, PromptPanel.FORWARD, false, false, prop);
		fen.setVisible(true);
		return fen;
	}
	
	public static PromptFrame start0(List<Page> pages, boolean slave, Dimension frameSize, int offY, int speed, int direction, boolean mirror, boolean playing,  NetworkSystem netSys) {
		Properties prop = new Properties(new File("config.properties"));
		if(!prop.exists()) {
			OptionPane.showErrorDialog(null, "Prompteur", new FileNotFoundException("config.properties not found"));
			System.exit(1);
			return null;
		}

		PromptFrame fen = new PromptFrame(pages, netSys, slave, frameSize, offY, speed, direction, mirror, playing, prop);
		fen.setVisible(true);
		
		return fen;
	}
	
	@SuppressWarnings("resource")
	public static String readText(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = "";
			String line = "";
			while((line = reader.readLine()) != null)
				text += line + "\n";
			
			return new String(text.getBytes(), StandardCharsets.UTF_8);
		}catch(IOException e) {e.printStackTrace();}
		return "";
	}

}
