package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class DebugEnt extends Entity {
	
	Image turret;
	
	public EntityLiving target;
	
	public DebugEnt() {
		this(new Vector2f(0,0));
	}
	
	public DebugEnt(Vector2f pos) {
		super("goodie.png", pos);
		turret = Utils.getImageFromPath("canun.png");
	}
	public int t;
	@Override
	public void update(int delta) {
		
		t+=delta;
		if(t>1000&&target!=null) {
			t=0;
			MainGame.instance.root.addChild(new BulletNorm(getCenterPos(), Utils.getAngle(this.getCenterPos(), target.getCenterPos()), 3, getWidth()/2));
		}
		
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
		if (target != null) {

			target.isTarget--;
		}
		
		target = (EntityLiving) Utils.getNearestEntity(Utils.sortByType(MainGame.instance.root.getCopyOfChildren(), "DebugEnemy"), this.getCenterPos(), 500);

		if (target != null) {
			
			target.isTarget++;
		}
	}
	
	@Override public void render(Graphics g) {
		super.render(g);
		if(target != null) {
			turret.setRotation(Utils.getAngle(this.getPos(), target.getPos()));
			g.setColor(Color.green);
			//g.fillOval(target.getX()-10, target.getY()-10, target.getWidth()+20, target.getHeight()+20);
		}
		g.drawImage(turret, getX(), getY());
	}
}
