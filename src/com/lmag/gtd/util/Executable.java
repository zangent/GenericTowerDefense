package com.lmag.gtd.util;

import java.util.HashMap;

public abstract class Executable {
	
	/*
	 * Variable name, variable
	 */
	public HashMap<String, Object> params = new HashMap<String, Object>();
	
	
	public Executable() {}
	
	
	public abstract void run();
}
