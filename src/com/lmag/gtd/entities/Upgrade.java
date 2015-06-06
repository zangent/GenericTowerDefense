package com.lmag.gtd.entities;

public class Upgrade {
	
	public String iconName = "default.png";
	
	public String name = "Unnamed";
	
	Tower parent;

	public Upgrade(Tower Parent) {
		
		parent = Parent;
	}
	
	public void onAdded() {}
	
	public void onRemoved() {}
}