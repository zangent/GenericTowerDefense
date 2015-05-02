package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
import com.lmag.gtd.entities.menu.EntityMenu;
import com.lmag.gtd.util.Utils;

public class TowerLaser extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	public int timeSinceLastUpdate = 0;
	
	public boolean firing = false;
	
	public int ticks = 0, fireRate = 175;
	
	public int intensity = 0, warmup = 600;
	
	public float multiplier = 0.65f;
	
	
	
	public TowerLaser() {
		this(new Vector2f(0,0));
	}
	
	public TowerLaser(Vector2f pos) {
		super("tower_laser.png", pos);
		turret = Utils.getImageFromPath("canun2.png");
		
		range = 500;
		
		addAsInputListener();
	}
	
	protected TowerLaser(String spr, Vector2f pos) {
		super(spr, pos);
	}
	
	@Override
	public void tick(int dt) {
		super.tick(dt);
		
		if(EMPTimer != 0) {
			target = null;
		}
	}
	
	@Override
	public void update(int delta) {
		
		int dmg = 3;
		
		timeSinceLastUpdate += delta;
		
		if(target != null) {
			if(EMPTimer != 0) {
				target = null;
			}
			
			if (!firing) {
				
				firing = true;
			}
			
			timeSinceLastUpdate = 0;
			
			if (intensity < warmup) {
				
				intensity += delta;
				
				if (intensity > warmup) {
					
					intensity = warmup;
				}
			}

			ticks+=delta;
			if(ticks > fireRate) {
				target.damage((float)dmg / ((float)warmup / (float)intensity)*multiplier);
				ticks = 0;
			}
				
			
			
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
		//Entity lt = target;
		target = (EntityLiving) Utils.getNearestEntity(Utils.sortByType(MainGAme.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), range);
		if(target == null) {
			intensity = 0;
		}
		if (target != null) {
			
			target.isTarget++;
		}
	}
	
	@Override public void render(Graphics g) {
		super.render(g);
		
		//debug
		g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		
		if(target != null) {
			turret.setRotation(Utils.getAngle(this.getPos(), target.getPos()));
		}
		
		g.drawImage(turret, getX(), getY());
		
		if(target != null && EMPTimer==0) {

			Vector2f from = this.getCenterPos();
			Vector2f t = target.getCenterPos();

			float anglo_saxon = Utils.getAngle(from, t);
			
			final float BARREL_LENGTH = 16;
			
			from.add(new Vector2f(BARREL_LENGTH*(float)Math.cos(Math.toRadians(anglo_saxon)), BARREL_LENGTH*(float)Math.sin(Math.toRadians(anglo_saxon))));

			g.setColor(new Color(0.9f, 0.1f, 0.1f, 0.75f));
			g.setLineWidth(0.5f+((float)intensity/(float)warmup)*2.5f);
			g.drawLine(from.x, from.y, t.getX(), t.getY());
		}
	}
	


	@Override
	public void mouseClicked(int btn, int x, int y, int clickCount)  {
		
		if (!this.isUpdating()) {
			
			return;
		}
		
		if(this.contains(new Vector2f(x,y))) {
			

			//debug
			System.out.println("Clicked on entity");
		
			if(parent.updateChildren) {

				//debug
				System.out.println("Selecting");
			
				MainGAme.instance.lc.setEntitySidebar(this);
			}
		}
			
		else if(MainGAme.instance.lc.selectedEntity==this) {
			
			//debug
			System.out.println("Deselecting");
		
			MainGAme.instance.lc.setBuySidebar();
		}
	}
}
