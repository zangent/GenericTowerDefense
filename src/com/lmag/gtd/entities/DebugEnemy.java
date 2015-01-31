package com.lmag.gtd.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class DebugEnemy extends Entity {

	Vector2f[] path;
	int endTime = 4000;
	float percentJump;
	int t;
	
	public DebugEnemy(Vector2f position) {
		super("enemytst.png", position);
		// 800x600
		path = new Vector2f[]{
				new Vector2f(-50,300),
				new Vector2f(400,300),
				new Vector2f(0,550),
				new Vector2f(400,550),
		};
		percentJump = endTime / (path.length - 1);
	}
	@Override
	public void render(Graphics g) {
		
		super.render(g);
	}
	@Override
	public void update(int dt) {
		t+=dt;
		if((int)Math.floor(t/percentJump)>=path.length-1) t=1;
		int slot = (int)Math.floor(t/percentJump);
		Vector2f a = path[slot];
		Vector2f b = path[slot+1];
		float stp = t-(slot)*percentJump;
		stp*=(100/percentJump)*0.01f;
		Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		);
		setPos(np);
	}
}
