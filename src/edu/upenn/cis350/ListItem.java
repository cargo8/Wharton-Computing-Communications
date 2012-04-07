package edu.upenn.cis350;

public class ListItem {
	private Object data;
	private boolean isSection;
	
	public ListItem(Object data, boolean isSection) {
		this.data = data;
		this.isSection = isSection;
	}
	
	/**
	 * Will return the data stored with this item
	 * @return the stored data item
	 */
	public Object getData() {
		return this.data;
	}
	
	/**
	 * Will return true if this item is a section header
	 * @return true if section header, false if list item
	 */
	public boolean isSection() {
		return this.isSection;
	}
}
