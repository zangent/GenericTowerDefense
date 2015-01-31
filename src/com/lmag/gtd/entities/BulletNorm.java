package com.lmag.gtd.entities;

import org.newdawn.slick.geom.Vector2f;

public class BulletNorm extends Entity{
	
	Vector2f step;
	
	public BulletNorm(Vector2f position, float anglo_saxon, float speed, float barrelLength) {
		super("needasentryhere.png", position);
		System.out.println(anglo_saxon);
		setPos(position.add(new Vector2f(barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		sprite.setRotation(anglo_saxon);
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
		System.out.println(step);
	}
	@Override
	public void update(int dt) {
		setPos(getPos().add(step));
	}
}
