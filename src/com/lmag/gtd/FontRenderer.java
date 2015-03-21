package com.lmag.gtd;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FontRenderer {
	private Image fontSheet;
	private static final String fontMapping = "abcdefghijklmnopqrstuvwxyz";
	public static final int charSize = 16;
	public FontRenderer(String fontName) {
		try {
			if(fontName.endsWith(".png"))
				fontSheet = new Image("res/"+fontName);
			else
				fontSheet = new Image("res/"+fontName+".png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void render(int x, int y, Graphics g, String textThatWeShouldRenderImmediatelyWithoutPauseOrFurtherAdo) {
		for(int i=0;i<textThatWeShouldRenderImmediatelyWithoutPauseOrFurtherAdo.length();i++) {
			char c = textThatWeShouldRenderImmediatelyWithoutPauseOrFurtherAdo.toLowerCase().charAt(i);
			int pos = fontMapping.indexOf(c);
			g.drawImage(fontSheet, x, y, x+charSize, y+charSize, pos*charSize, 0, (pos*charSize)+charSize, charSize);
			x += charSize;
		}
	}
}
