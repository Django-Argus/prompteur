package net.argus.prompteur;

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
import net.argus.prompteur.net.NetworkSystem;

public class Main {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Loader loader = new Loader();
			loader.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
			OptionPane.showErrorDialog(null, "Prompteur", e);
			System.exit(1);
		}
	}
	
	public static void start(List<File> files, boolean slave, NetworkSystem netSys) {
		List<Page> pages = new ArrayList<Page>();
		for(File file : files) {
			String txt = readText(file);
			pages.add(new Page(Loader.getPageName(file), txt));
		}
		
		Properties prop = new Properties(new File("config.properties"));
		if(!prop.exists()) {
			OptionPane.showErrorDialog(null, "Prompteur", new FileNotFoundException("config.properties not found"));
			System.exit(1);
			return;
		}

		PromptFrame fen = new PromptFrame(pages, netSys, slave, 0, prop);
		fen.setVisible(true);
	}
	
	public static void start0(List<Page> pages, boolean slave, int offY, NetworkSystem netSys) {
		Properties prop = new Properties(new File("config.properties"));
		if(!prop.exists()) {
			OptionPane.showErrorDialog(null, "Prompteur", new FileNotFoundException("config.properties not found"));
			System.exit(1);
			return;
		}

		PromptFrame fen = new PromptFrame(pages, netSys, slave, offY, prop);
		fen.setVisible(true);
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
