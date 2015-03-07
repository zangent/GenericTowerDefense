package com.lmag.gtd.entities;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class BulletNorm extends Entity{
	
	Vector2f step;
	
	public static final int lifeTime = 5000;
	private int life = 0;
	
	
	public BulletNorm(Vector2f position, float anglo_saxon, float speed, float barrelLength) {
		super("needasentryhere.png", position);
		setPos(position.add(new Vector2f(barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		sprite.setRotation(anglo_saxon);
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
	}
	
	public int getDamage() {
		return 1;
	}
	
	@Override
	public void update(int dt) {
		
		setPos(getPos().add(step));
		
		life += dt;
		
		if(life>=lifeTime) {
			
			parent.removeChild(this);
		}
		
		EntityLiving e = (EntityLiving)Utils.getNearestEntity(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), 50);
		
		if(e!=null) {
			e.damage(getDamage());
			parent.removeChild(this);
		}
	}
}
