package com.lmag.gtd.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Utils {
	public static Image getImageFromPath(String path) {
		try {
			return new Image("res/"+path);
		} catch (SlickException e) {
			try {
				return new Image("res/error.png");
			} 
			catch (SlickException e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}
}
