package com.lmag.gtd.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class DebugEnemy extends Entity {

	public DebugEnemy(Vector2f position) {
		super("enemytst.png", position);
	}
	@Override
	public void render(Graphics g) {
		
		g.drawOval(getX()-500, getY()-500, 1000, 1000);
		
		super.render(g);
		
	}
}
