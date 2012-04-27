package edu.upenn.cis350;

public class ListItem {
	private Object data;
	private Type type;
	
	public enum Type {
		HEADER, EVENT, MESSAGE, MESSAGEBOX, MESSAGEHEADER,
		INFO, GROUP, SYSTEM, NONE;
	}
	
	public ListItem(Object data, Type type) {
		this.data = data;
		this.type = type;
	}
	
	/**
	 * Will return the data stored with this item
	 * @return the stored data item
	 */
	public Object getData() {
		return this.data;
	}
	
	/**
	 * Will return the type of data stored with this item
	 * @return the type of data stored
	 */
	public Type getType(){
		return type;
	}
	
}

