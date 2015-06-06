package com.lmag.gtd.entities;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class TowerLaser extends Tower {
	
	//public static final Upgrade[] elligibleUpgrades = {Upgrade.range, Upgrade.damage};
	
	public int timeSinceLastUpdate = 0;
	
	public int ticks = 0;
	
	public int intensity = 0, warmup = 600;
	
	public float multiplier = 0.65f;
	
	
	
	public TowerLaser() {
		this(new Vector2f(0,0));
	}
	
	public TowerLaser(Vector2f pos) {
		
		this("tower_laser.png", pos);
	}
	
	protected TowerLaser(String spr, Vector2f pos) {
		super(spr, pos);
		
		turret = Utils.getImageFromPath("canun2.png");
		
		baseFireRate = 175;
		baseDamage = 3;
		baseRange = 500;
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
		
		float dmg = getDamage();
		
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

			ticks += delta;
			
			if(ticks > getFireRate()) {
				
				target.damage((float)dmg / ((float)warmup / (float)intensity)*multiplier);
				ticks = 0;
			}
		}

		if (target != null) {

			target.isTarget--;
		}
		//Entity lt = target;
		target = (EntityLiving) Utils.getNearestEntity(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), getRange());
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

		g.drawImage(turret, getX(), getY());
		
		
		if(target != null && this.isUpdating()) {
			
			turret.setRotation(Utils.getAngle(this.getPos(), target.getPos()));

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
	public void onMouseClicked(int btn, int x, int y, int clickCount)  {

		
		if (this.isUpdating()) {
		
			if(this.contains(new Vector2f(x,y)) ||
					(!MainGame.instance.lc.buyMenuRight.open &&
							MainGame.instance.lc.entityMenuRight.
							contains(new Vector2f(x,y)))) {

			
				if(parent.updateChildren) {
			
					MainGame.instance.lc.setEntitySidebar(this);
				}
			}
			
			else if(MainGame.instance.lc.selectedEntity == this) {
		
				MainGame.instance.lc.setBuySidebar();
			}
		}
	}
}
