package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
import com.lmag.gtd.entities.menu.EntityMenu;
import com.lmag.gtd.util.Utils;

public class TowerPulseCannon extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	public int fireRate = 1500;
	
	public boolean firing = false;
	
	public int burstFireRate = 300, currentVolley = 0, volleys = 3;
	
	
	public TowerPulseCannon() {
		this(new Vector2f(0,0));
	}
	
	public TowerPulseCannon(Vector2f pos) {
		super("tower_pulsecannon.png", pos);
		
		turret = Utils.getImageFromPath("canun2.png");
		
		range = 500;
		
		addAsInputListener();
	}
	
	protected TowerPulseCannon(String spr, Vector2f pos) {
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
		if((firing && t > burstFireRate || t > fireRate) && target!=null) {
			
			if (!firing) {
				
				firing = true;
			}
			
			t=0;
			
			currentVolley++;
			
			if (currentVolley >= volleys) {
				
				currentVolley = 0;
				firing = false;
			}
			
			MainGAme.instance.root.addChild(new BulletPulse(getCenterPos(), Utils.getAngle(this.getCenterPos(), target.getCenterPos()), getWidth()/2));
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


	@Override
	public void mouseClicked(int btn, int x, int y, int clickCount)  {

		if(this.contains(new Vector2f(x,y)) && MainGAme.instance.lc.selectedEntity==this
				&& parent.updateChildren)
			MainGAme.instance.lc.setEntitySidebar(this);
	}
}

class BulletPulse extends Entity{
	
	Vector2f step;
	
	public static final int lifeTime = 10000;
	private int life = 0;
	public static final float speed = 10f;
	
	public BulletPulse(Vector2f position, float anglo_saxon, float barrelLength) {
		super("pulsey.png", position);
		setPos(position.add(new Vector2f(barrelLength*(float)Math.cos(Math.toRadians(anglo_saxon)), barrelLength*(float)Math.sin(Math.toRadians(anglo_saxon)))));
		sprite.setRotation(anglo_saxon);
		step = new Vector2f(speed*(float)Math.cos(Math.toRadians(anglo_saxon)), speed*(float)Math.sin(Math.toRadians(anglo_saxon)));
	}
	
	public float getDamage() {
		return 11;
	}
	
	@Override
	public void update(int dt) {

		
		if (statEffects.contains(StatEffect.EMP)) {
			
			return;
		}
		
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
