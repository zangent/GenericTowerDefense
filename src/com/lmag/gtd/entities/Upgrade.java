package com.lmag.gtd.entities;

public class Upgrade {

	public String displayName = "Unnamed";

	public String internalName = "LOL YOU MESSED UP YOU SHOULD NEVER SEE THIS STRING OH GOD YOU'RE BEING DUMB FIX THIS BUG RIGHT NOW OR I'LL KILL YOU IN YOUR SLEEP";

	Tower parent;

	public Upgrade(Tower Parent) {

		parent = Parent;
	}

	public void onAdded() {
		
	}

	public void onRemoved() {
	}
}