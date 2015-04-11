package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
import com.lmag.gtd.util.Utils;

public class TowerMachineGun extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	protected int fireRate = 50;
	
	public TowerMachineGun() {
		this(new Vector2f(0,0));
	}
	
	public TowerMachineGun(Vector2f pos) {
		super("goodie.png", pos);
		turret = Utils.getImageFromPath("canun.png");
		
		range = 500;
	}
	
	protected TowerMachineGun(String spr, Vector2f pos) {
		super(spr, pos);
	}
	
	public int t;
	
	@Override
	public void tick(int dt) {
		super.tick(dt);
		
		if(EMPTimer != 0) {
			target = null;
		}
	}
	
	@Override
	public void update(int delta) {
		
		
		t+=delta;
		if(t>fireRate && target!=null) {
			t=0;
			MainGAme.instance.root.addChild(new BulletNorm(getCenterPos(), Utils.getAngle(this.getCenterPos(), target.getCenterPos()), getWidth()/2));
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
		
		target = (EntityLiving) Utils.getNearestEntity(Utils.sortByType(MainGAme.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), range);

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

class BulletNorm extends Entity{
	
	Vector2f step;
	
	public static final int lifeTime = 5000;
	private int life = 0;
	public static final float speed = 15;
	
	public BulletNorm(Vector2f position, float anglo_saxon, float barrelLength) {
		super("needasentryhere.png", position);
		setPos(position.add(new Vector2f(-barrelLength/2+barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		sprite.setRotation(anglo_saxon);
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
	}
	
	public float getDamage() {
		return .25f;
	}
	
	@Override
	public void update(int dt) {
		
		setPos(getPos().add(step));
		
		life += dt;
		
		if(life>=lifeTime) {
			
			parent.removeChild(this);
		}
		
		EntityLiving e = (EntityLiving)Utils.getNearestEntity(Utils.sortByType(MainGAme.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), 50);
		
		if(e!=null) {
			e.damage(getDamage());
			parent.removeChild(this);
		}
	}
}
