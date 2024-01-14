package net.argus.prompteur;

import java.util.ArrayList;
import java.util.List;

import net.argus.prompteur.tag.Tag;

public class Page {
	
	public static final Page EMPTY = new Page("empty", "");
	
	private String name;
	private String text;
	private List<Tag> tags = new ArrayList<Tag>();
	
	public Page(String name, String text) {
		this.name = name;
		this.text = text;
	}
	
	public Page(List<Tag> tags) {
		this.tags = tags;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isEmpty() {
		return tags.isEmpty();
	}

}
