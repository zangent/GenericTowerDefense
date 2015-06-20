package com.lmag.gtd.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class TowerMachineGun extends Tower {
	
	
	public TowerMachineGun() {
		this(new Vector2f(0,0));
	}
	
	public TowerMachineGun(Vector2f pos) {
		this("tower_machinegun.png", pos);
	}
	
	protected TowerMachineGun(String spr, Vector2f pos) {
		super(spr, pos);
		
		turret = Utils.getImageFromPath("canun.png");
		
		baseRange = 500;
		baseFireRate = 35;
		baseDamage = 0.25f;
	}
	
	public int t;
	
	@Override
	public void tick(int dt) {
		super.tick(dt);
		
		
		//Deprecated 
		/*if(EMPTimer != 0) {
			target = null;
		}*/
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		t += delta;
		
		if(t > getFireRate() && target != null) {
			
			t = 0;
			
			MainGame.instance.root.addChild(new BulletNorm(getCenterPos(), this, Utils.getAngle(this.getCenterPos(), target.getCenterPos()), getWidth()/2));
		}

		//debug
		//System.out.println(target==null);
		if (target != null) {

			target.isTarget--;
		}
		
		target = getTarget();

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

class BulletNorm extends Entity {
	
	Vector2f step;
	
	public static final int lifeTime = 5000;
	private int life = 0;
	public static final float speed = 20;
	
	public Tower owner;
	
	public BulletNorm(Vector2f position, Tower Owner, float anglo_saxon, float barrelLength) {
		super("needasentryhere.png", position);
		
		owner = Owner;
		
		setPos(position.add(new Vector2f(-barrelLength/2+barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		sprite.setRotation(anglo_saxon);
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
	}
	
	
	@Override
	public void update(int dt) {
		
		setPos(getPos().add(step));
		
		life += dt;
		
		if(life>=lifeTime) {
			
			parent.removeChild(this);
		}
		
		EntityLiving e = (EntityLiving)Utils.getNearestEntity(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), 50);
		
		if(e != null) {
			
			e.damage(owner.getDamage());
			parent.removeChild(this);
		}
	}
}
