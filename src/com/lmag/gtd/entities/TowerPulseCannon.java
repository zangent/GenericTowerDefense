package com.lmag.gtd.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class TowerPulseCannon extends Tower {
	
	public int burstFireRate = 300, currentVolley = 0, volleys = 3;
	
	
	public TowerPulseCannon() {
		this(new Vector2f(0,0));
	}
	
	public TowerPulseCannon(Vector2f pos) {
		
		this("tower_pulsecannon.png", pos);
	}
	
	protected TowerPulseCannon(String spr, Vector2f pos) {
		super(spr, pos);
		
		turret = Utils.getImageFromPath("canun2.png");

		baseDamage = 11;
		baseFireRate = 1500;
		baseRange = 500;
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
		super.update(delta);
		
		t += delta;
		
		if((firing && t > burstFireRate || t > getFireRate()) && target != null) {
			
			if (!firing) {
				
				firing = true;
			}
			
			t = 0;
			
			currentVolley++;
			
			if (currentVolley >= volleys) {
				
				currentVolley = 0;
				firing = false;
			}
			
			MainGame.instance.root.addChild(new BulletPulse(getCenterPos(), this, Utils.getAngle(this.getCenterPos(), target.getCenterPos()), getWidth()/2));
		}

		if (target != null) {

			target.isTarget--;
		}
		
		target = (EntityLiving) Utils.getNearestEntity(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), getRange());

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

class BulletPulse extends Entity {
	
	Vector2f step;
	
	Tower owner;
	
	public static final int lifeTime = 10000;
	
	private int life = 0;
	
	public static final float speed = 10f;
	
	
	public BulletPulse(Vector2f position, Tower Owner, float anglo_saxon, float barrelLength) {
		super("pulsey.png", position);
		
		setPos(position.add(new Vector2f(barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		
		sprite.setRotation(anglo_saxon);
		
		owner = Owner;
		
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
	}
	
	@Override
	public void update(int dt) {

		//TODO: Debug
		/*
		if (statEffects.contains(StatEffect.EMP)) {
			
			return;
		}
		*/
		
		//debug
		if (parent == null) {
			
			System.out.println("Somehow parent is null! WTF");
		}
		if (owner == null) {
			
			System.out.println("Somehow owner is null! WTF");
		}
		
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
