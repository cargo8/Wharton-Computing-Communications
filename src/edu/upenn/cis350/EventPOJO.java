package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;


// POJO to hold all event data, eventually to be put into a sqlite DB
public class EventPOJO {
	
	private String eventName;
	private String eventDesc;
	private String eventActions;
	private String startTime; //TODO timestamp?
	private String endTime;
	private List<String> affils;
	private List<String> systems;
	private int severity;
	private String contact1;
	private String contact2;
	private String type;
	//TODO List of messages as Strings or Message objects?
	
	public EventPOJO(){
		affils = new ArrayList<String>();
		systems = new ArrayList<String>();
	}
	
	///////// SETTER METHODS /////////////
	
	public void setEventName(String name){
		eventName = name;
	}
	
	public void setEventDesc(String desc){
		eventDesc = desc;
	}
	
	public void setEventActions(String actions){
		eventActions = actions;
	}
	
	public void setStartTime(String time){
		startTime = time;
	}

	public void setEndTime(String time){
		endTime = time;
	}
	
	public void addToAffils(String affil){
		affils.add(affil);
	}
	
	public void addToSystems(String system){
		systems.add(system);
	}
	
	public void setSeverity(int sev){
		severity = sev;
	}
	
	public void setContact1(String contact){
		contact1 = contact;
	}
	
	public void setContact2(String contact){
		contact2 = contact;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	//////////// GETTER METHODS /////////////
	
	public String getEventName(){
		return eventName;
	}
	
	public String getEventDesc(){
		return eventDesc;
	}
	
	public String getEventActions(){
		return eventActions;
	}
	
	public String getStartTime(){
		return startTime;
	}
	
	public String getEndTime(){
		return endTime;
	}
	
	public List<String> getAffils(){
		return affils;
	}
	
	public List<String> getSystems(){
		return systems;
	}
	
	public int getSeverity(){
		return severity;
	}
	
	public String getContact1(){
		return contact1;
	}
	
	public String getContact2(){
		return contact2;
	}
	
	public String getType(){
		return type;
	}
	
	
	
	
	
	
	
	
	
}
