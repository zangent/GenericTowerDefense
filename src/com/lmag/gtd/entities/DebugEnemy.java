package com.lmag.gtd.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.util.Utils;

public class DebugEnemy extends EntityLiving {

	int t = 0;
	int currentSegment = 0;

	float endTime = 0;
	float speedMod = 3f;
	
	Vector2f[] path;
	
	
	public DebugEnemy(Vector2f position) {
		super("enemytst.png", position);
		// 800x600
		path = new Vector2f[]{
				new Vector2f(-50,300),
				new Vector2f(400,300),
				new Vector2f(0,550),
				new Vector2f(400,550),
		};
		
		endTime = Utils.getDist(path[0], path[1]) / speedMod * 10f;
	}
	
	public DebugEnemy setPath(Vector2f[] p) {
		path = p;
		endTime = Utils.getDist(path[0], path[1]) / speedMod * 10f;
		currentSegment = 0;
		t = 0;
		return this;
	}
	
	@Override
	public void render(Graphics g) {
		
		super.render(g);
	}
	
	
	@Override
	public void update(int dt) {
		
		t += dt;
		
		if (t > endTime) {
			
			currentSegment++;
			t = 0;
			
			if (currentSegment >= path.length - 1) {
				
				currentSegment = 0;
			}
			
			endTime = Utils.getDist(path[currentSegment], path[currentSegment + 1]) / speedMod * 10;
		}
		
		Vector2f a = path[currentSegment];
		Vector2f b = path[currentSegment + 1];
		
		float stp = t/endTime;

		Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		);
		
		setPos(np);
		
		/*
		
		if ((int)Math.floor(t/percentJump)>=path.length-1) {
			
			t=1;
		}
		
		int slot = (int)Math.floor(t / percentJump);
		
		Vector2f a = path[slot];
		Vector2f b = path[slot + 1];
		
		float stp = t - (slot) * percentJump;
		
		stp *= (100 / percentJump) * 0.01f;
		
		Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		);
		
		setPos(np);
		
		*/
	}
}
