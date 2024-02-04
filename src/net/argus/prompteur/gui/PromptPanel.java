package net.argus.prompteur.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.argus.file.Properties;
import net.argus.prompteur.Page;
import net.argus.prompteur.Timer;
import net.argus.prompteur.net.event.EventNetworkSystem;
import net.argus.prompteur.tag.Tag;

public class PromptPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2057763926368093928L;
	
	private PromptFrame fen;
	private Timer timer;
	
	private int offY;
	
	private int triangleWidth;
	private int triangleHeight;
	private int txtPadding = 10;
	private final int border = 16;
	
	private float perCentPos;
	
	public static final int FORWARD = 1;
	public static final int BACKWARD = -1;
	private int direction = FORWARD;
	
	private boolean bothTriangle = true;
	private Color triangleColor;
	
	private Font defaultFont;
	
	private Color background;
	private Color defaultTextColor;
	
	private int nbLine = 0;
	private int lastLineIndex = 0;
	
	private Dimension lastDim;
	
	private boolean forcePageRepaint = false;
	
	private List<Page> pages = new ArrayList<Page>();
	private int selectedPage = -1;
	
	private int tabSpace = 4;
	
	private boolean mirror = false;
	
	private boolean slave;
	
	public static final int NONE_TAG = -1;
	
	
	@SuppressWarnings("deprecation")
	public PromptPanel(PromptFrame fen, List<Page> pages, boolean slave, int offY, int speed, int direction, boolean mirror, boolean playing, Properties prop) {
		if(pages.size() == 0)
			throw new IllegalArgumentException("No page");
		
		this.slave = slave;
		
		this.fen = fen;
		this.pages = pages;
		
		this.offY = offY;
		this.direction = direction;
		
		this.mirror = mirror;
		
		this.defaultFont = prop.getFont("prompteur.font");
		this.background = prop.getColor("prompteur.background");
		setBackground(background);
		this.defaultTextColor = prop.getColor("prompteur.defaulttextcolor");
		
		this.triangleWidth = prop.getInt("prompteur.triangle");
		this.triangleHeight = 2 * triangleWidth;
		
		this.perCentPos = (float) ((float) prop.getInt("prompteur.triangle.position") / 100f);
		
		this.triangleColor = prop.getColor("prompteur.triangle.color");
		this.bothTriangle = prop.getBoolean("prompteur.tirangle.both");
		
		this.tabSpace = prop.getInt("prompteur.tabspace");
		
		fen.getNetworkSystem().setPromptPanel(this);
		
		this.timer = new Timer(this, slave, speed, playing, prop);
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2d);
		
		if(mirror) {
			AffineTransform at = AffineTransform.getTranslateInstance(0, getHeight());
			at.scale(1, -1);
			g2d.setTransform(at);
		}
		drawTriangle(g2d);
		
		g2d.translate(triangleWidth + txtPadding, fen.getHeight() * perCentPos);
		g2d.setFont(defaultFont);
		drawText(g2d, getCurrentPage().getText());
	}
	
	private void drawTriangle(Graphics2D g) {
		int[] x = new int[] {0, 0, triangleWidth};
		int[] y = new int[] {(int) (fen.getHeight() * perCentPos - triangleHeight / 2),
				(int) (fen.getHeight() * perCentPos + triangleHeight / 2),
				(int) (fen.getHeight() * perCentPos)};

		g.setColor(triangleColor);
		g.fillPolygon(x, y, x.length);
		
		if(bothTriangle) {
			int[] x2 = new int[] {fen.getWidth() - border, fen.getWidth() - border, fen.getWidth() - triangleWidth - border};
			int[] y2 = new int[] {(int) (fen.getHeight() * perCentPos - triangleHeight / 2),
					(int) (fen.getHeight() * perCentPos + triangleHeight / 2),
					(int) (fen.getHeight() * perCentPos)};
			
			g.fillPolygon(x2, y2, x2.length);
		}
	}
	
	private void drawText(Graphics2D g, String text) {
		if(getCurrentPage().equals(Page.EMPTY))
			return;
		
        int maxWidth = fen.getWidth() - (bothTriangle?2:1) * triangleWidth - 2 * txtPadding - 2 * border;
        int x = 10;
        int y = g.getFontMetrics().getHeight() / 4;
        int lineHeight = getNextLineMaxHeight(g, getCurrentPage().getTags(), 0);

        offY += timer.getSpeed() * direction;
        g.setColor(defaultTextColor);
        g.setFont(defaultFont);
        g.translate(0, -offY);

        if(getCurrentPage().isEmpty() || forcePageRepaint || lastDim == null || lastDim.height != fen.getSize().height || lastDim.width != fen.getSize().width) {
        	getCurrentPage().setTags(splitTextToFitWidth(text, g, maxWidth));
        	lastDim = fen.getSize();
        	forcePageRepaint = false;
        }
        
        g.setFont(defaultFont);
        g.setColor(defaultTextColor);
        
        int countLine = 0;
        int lastLineIndex = offY>0?nbLine + 1:1;
        for(int i = 0; i < getCurrentPage().getTags().size(); i++) {
        	Tag tag = getCurrentPage().getTags().get(i);
        	
        	if(tag.getTag() == Tag.ENDL)
        		countLine++;
        	
        	if(lastLineIndex != countLine && offY - y + fen.getHeight() * perCentPos> fen.getHeight() * perCentPos - lineHeight &&
        			offY - y + fen.getHeight() * perCentPos < fen.getHeight() * perCentPos + lineHeight)
        		lastLineIndex = countLine;
        	
        	 if((y + fen.getHeight() * perCentPos) - offY - lineHeight > fen.getHeight()) {
        		 break;
             }
        	 
        	 
        	switch(tag.getTag()) {
        		case Tag.WORD:
        			if((y + fen.getHeight() * perCentPos) - offY + lineHeight < 0)
        				continue;

        			g.drawString(tag.getString(), x, y);
        			x += g.getFontMetrics().stringWidth(tag.getString());
        			continue;
        			
        		case Tag.ENDL:
        			x = 10;
        			lineHeight = getNextLineMaxHeight(g, getCurrentPage().getTags(), i+1);
        			y += lineHeight;
        			continue;
        			
        		case Tag.COLOR:
        			g.setColor(tag.getColor());
        			continue;
        			
        		case Tag.FONT:
        			g.setFont(tag.getFont());
        			
        			continue;		
        	}
        }
        this.lastLineIndex = lastLineIndex;
    }
	
	private int getNextLineMaxHeight(Graphics2D g, List<Tag> tags, int i) {
		if(tags.size() <= i)
			return -1;
		int maxHeight = g.getFontMetrics().getHeight();
		Font backup = g.getFont();
		
		Tag tag = tags.get(i);

		for(; tag.getTag() != Tag.ENDL; i++) {
			tag = tags.get(i);
			
			if(tag.getTag() == Tag.WORD) {
				if(g.getFontMetrics().getHeight() > maxHeight)
					maxHeight = g.getFontMetrics().getHeight();			
			}
			
			if(tag.getTag() != Tag.FONT) 
				continue;
			
			g.setFont(tag.getFont());	
		}
		g.setFont(backup);
		return maxHeight;
	}

    private List<Tag> splitTextToFitWidth(String text, Graphics2D g, int maxWidth) {
        String[] words = text.split("\\s+");
        List<Tag> Ltags = new ArrayList<Tag>();
        
        int currentWidth = 0;
        int nbLine = 0;

  main: for(String word : words) {

        	if(word.startsWith("#") && word.endsWith("#")) {
        		String color = word.substring(1, word.length()-1);

        		if(color.equalsIgnoreCase("default")) {
        			Ltags.add(new Tag(Tag.COLOR, defaultTextColor));
        			continue;
        		}
        		Ltags.add(new Tag(Tag.COLOR, Color.decode("#" + color)));
        		continue;
        	}
        	
        	if(word.startsWith("%") && word.endsWith("%")) {
        		if(word.length() == 2) {
        			Ltags.add(new Tag(Tag.ENDL, 0));
        			nbLine++;
        			currentWidth = 0;
        			continue;
        		}
        		
        		char c = word.charAt(1);
        		for(int i = 1; i < word.length() - 2; i++)
        			if(word.charAt(i) != c) {
        				switch(getTag(word)) {
	            			case FontTag.FAMILY:
	            				String family = word.substring(8, word.length()-1);
	            				if(family.equalsIgnoreCase("default"))
	            					family = defaultFont.getFamily();
	            				
	            				Font nFont = new Font(family, g.getFont().getStyle(), g.getFont().getSize());
	            				Ltags.add(new Tag(Tag.FONT, nFont));
	                            g.setFont(nFont);
	                            
	            				continue main;
	            				
	            			case FontTag.STYLE:
	            				String style = word.substring(7, word.length()-1);
	            				if(Integer.valueOf(style) < 0)
	            					style = Integer.toString(defaultFont.getSize());
	            				
	            				nFont = g.getFont().deriveFont(Integer.valueOf(style));
	            				Ltags.add(new Tag(Tag.FONT, nFont));
	            				g.setFont(nFont);
	            				
	            				continue main;
	            				
	            			case FontTag.SIZE:
	            				String size = word.substring(6, word.length()-1);
	
	            				if(Integer.valueOf(size) <= 0)
	            					size = Integer.toString(defaultFont.getSize());
	            				
	            				nFont = g.getFont().deriveFont(Float.valueOf(size));
	            				Ltags.add(new Tag(Tag.FONT, nFont));
	            				g.setFont(nFont);
	            				
	            				continue main;
	            				
	            			case NONE_TAG:
	    					default:
	    						continue main;
        				}
        			}
        		
        		if(c == '%') {
        			for(int i = 0; i < word.length() - 1; i++)
        				Ltags.add(new Tag(Tag.ENDL, 0));
        			nbLine += word.length() - 1;
        			currentWidth = 0;
        			continue;
        		}else {
        			word = "";
            		Ltags.add(new Tag(Tag.ENDL, 0));

        			while(g.getFontMetrics().stringWidth(word + c) <= maxWidth)
        				word += c;
        			
            		Ltags.add(new Tag(Tag.WORD, word));
            		Ltags.add(new Tag(Tag.ENDL, 0));
            		
            		nbLine += 2;
            		currentWidth = 0;
            		continue;
        		}
        		
        	}else if(word.startsWith("@") && word.endsWith("@")) {
        		String spc = "";
        		for(int i = 0; i < tabSpace * word.length() - 1; i++)
        			spc += " ";
        		
        		if(currentWidth + g.getFontMetrics().stringWidth(spc) <= maxWidth) {
        			Ltags.add(new Tag(Tag.WORD, spc));
        			currentWidth += g.getFontMetrics().stringWidth(spc);
        		}else {
        			Ltags.add(new Tag(Tag.ENDL, 0));
        			nbLine++;
        			Ltags.add(new Tag(Tag.WORD, spc));
        			currentWidth = g.getFontMetrics().stringWidth(spc);
        		}
        		continue;
        	}
        	
            if(currentWidth + g.getFontMetrics().stringWidth(word + " ") <= maxWidth) {
            	Ltags.add(new Tag(Tag.WORD, word + " "));
            	currentWidth += g.getFontMetrics().stringWidth(word + " ");
            }else {
            	Ltags.add(new Tag(Tag.ENDL, 0));
            	nbLine++;
            	if(!word.equals(""))
            		word += " ";
            	Ltags.add(new Tag(Tag.WORD, word));
            	currentWidth = g.getFontMetrics().stringWidth(word);
            	continue;
            	
            }
        } 
        
        this.nbLine = nbLine;
        Ltags.add(new Tag(Tag.ENDL, 0));
        return Ltags;
    }
    
    private int getTag(String word) {
    	if(!word.endsWith("%"))
    		return NONE_TAG;
    	
    	if(word.startsWith("%family=")) {
    		return FontTag.FAMILY;
    	}
    	else if(word.startsWith("%style=")) {
    		return FontTag.STYLE;
    	}
    	else if(word.startsWith("%size="))
    		return FontTag.SIZE;
    	
    	return NONE_TAG;
    }
    
    public void changeDirection() {
    	direction = -direction;
    }
    
    public void setDirection(int direction) {
		this.direction = direction;
	}
    
    public int getDirection() {
		return direction;
	}
    
    public Timer getTimer() {
		return timer;
	}
    
    public int getCountLine() {
		return nbLine;
	}
    
    public int getLastLineIndex() {
		return lastLineIndex;
	}
    
    public void reset() throws InterruptedException {
    	if(timer == null)
    		return;
    	
    	if(!timer.isWait())
    		timer.waitStart();
    	offY = 0;
    	direction = FORWARD;
    	timer.setSpeed(0);
    	
    	repaint();
    }
    
    private Page getCurrentPage() {
    	if(selectedPage < 0)
    		return Page.EMPTY;
    	return pages.get(selectedPage);
    }
    
    public void setSelectedPage(int selectedPage) {
    	if(selectedPage == this.selectedPage)
    		return;
    	
    	forcePageRepaint();
    	
    	try {reset();}
    	catch(InterruptedException e) {e.printStackTrace();}
    	
    	this.selectedPage = selectedPage;
    	if(!slave) {
	    	getPromptFrame().startNetworkEvent(EventNetworkSystem.CHANGE_PAGE);
	    	getPromptFrame().startNetworkEvent(EventNetworkSystem.RESET);
    	}
	}
    
    public boolean isMirror() {
		return mirror;
	}
    
    public void setMirror(boolean mirror) {
    	this.mirror = mirror;
    }
    
    public List<Page> getPages() {
		return pages;
	}
    
    public void forcePageRepaint() {
    	this.forcePageRepaint = true;
    }
    
    public int getOffY() {
    	return offY;
    }
    
    public void caughtUpOffY(int offY) {
    	this.offY = offY;
    }
    
    public int getSelectedPageIndex() {
		return this.selectedPage;
	}
    
    public PromptFrame getPromptFrame() {
    	return fen;
    }

}
