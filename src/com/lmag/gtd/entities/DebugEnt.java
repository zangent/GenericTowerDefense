package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class DebugEnt extends Entity {
	
	Image turret;
	
	Entity target;
	
	public DebugEnt() {
		super("goodie.png", new Vector2f(0,0));
		turret = Utils.getImageFromPath("canun.png");
	}

	@Override
	public void update(int delta) {
		//this.setOffset(MainGame.instance.getMousePos().sub(new Vector2f(MainGame.WIDTH/2, MainGame.HEIGHT/2)));
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			offset.add(new Vector2f(0,-10));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			offset.add(new Vector2f(-10,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			offset.add(new Vector2f(0,10));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			offset.add(new Vector2f(10,0));
		}

		//debug
		//System.out.println(target==null);
		
		target = Utils.getNearestEntity(Utils.sortByType(MainGame.instance.root.getCopyOfChildren(), "DebugEnemy"), this.getPos(), 500);
	}
	
	@Override public void render(Graphics g) {
		super.render(g);
		if(target != null) turret.setRotation(Utils.getAngle(this.getPos(), target.getPos()));
		
		g.drawImage(turret, getX(), getY());
	}
}
