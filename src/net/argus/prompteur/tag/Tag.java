package net.argus.prompteur.tag;

import java.awt.Color;
import java.awt.Font;

public class Tag {
	
	public static final int WORD = 0;
	public static final int ENDL = 1;
	public static final int COLOR = 2;
	public static final int FONT = 3;
	public static final int TAB = 4;
	
	
	private Object data;
	
	private int tag;
	
	public Tag(int tag, Object data) {
		this.tag = tag;
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public String getString() {
		return (String) data;
	}
	
	public Font getFont() {
		return (Font) data;
	}
	
	public Color getColor() {
		return (Color) data;
	}
	
	public int getInt() {
		return (Integer) data;
	}
	
	public float getFloat() {
		return (Float) data;
	}
	
	public boolean getBoolean() {
		return (Boolean) data;
	}
	
	public int getTag() {
		return tag;
	}
	
	@Override
	public String toString() {
		return "tag@[" + tag + ", " + data + "]";
	}

}
