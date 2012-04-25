package edu.upenn.cis350;

public class ListItem {
	private Object data;
	private boolean isSection;
	private ItemType type;
	
	public enum ItemType {
		HEADER, EVENT, MESSAGE, MESSAGEBOX, MESSAGEHEADER,
		INFO, GROUP;
	}
	
	public ListItem(Object data, boolean isSection) {
		this.data = data;
		this.isSection = isSection;
	}
	
	public ListItem(Object data, boolean isSection, ItemType type) {
		this.data = data;
		this.isSection = isSection;
		this.type = type;
	}
	
	/**
	 * Will return the data stored with this item
	 * @return the stored data item
	 */
	public Object getData() {
		return this.data;
	}
	
	public ItemType getType(){
		return type;
	}
	
	/**
	 * Will return true if this item is a section header
	 * @return true if section header, false if list item
	 */
	public boolean isSection() {
		return this.isSection;
	}
}

