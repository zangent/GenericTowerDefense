package com.lmag.gtd.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;

public abstract class EntityLiving extends Entity {
	
	protected float defaultHealth = 100;
	protected float health = defaultHealth;
	public short isTarget=0;
	
	protected int range = 500;

	public EntityLiving(String sprite, Vector2f position) {
		super(sprite, position);
	}
	
	public float getHealth() {
		return health;
	}
	
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(health != defaultHealth) {
			
			float per = ((float)health)/((float)defaultHealth);
			
			if(per < 0) per = 0;
			
			Vector2f p = this.getPos().add(new Vector2f(0,-15));
			int width = getWidth(), height = 10, padding = 2;
			
			
			g.setColor(Color.darkGray);
			
			g.fillRect(p.x, p.y, width, height);

			width -= 2*padding;
			height -= 2*padding;
			
			g.setColor(Color.green);
			if(per < .666f) {
				g.setColor(Color.yellow);
			}
			if(per < .333f) {
				g.setColor(Color.red);
			}
			
			g.fillRect(p.x+padding, p.y+padding, width*per, height);
		}
		
		if (MainGAme.instance.debug) {
			
			g.setColor(Color.green);
			
			g.drawOval(this.getX()-range, this.getY()-range, range*2, range*2);
		}
	}

	
	public void setHealth(float health) {
		this.health = health;
		if(health <= 0) {
			onDeath();
			parent.removeChild(this);
		}
	}
	
	protected void onDeath() {}


	public void damage(float amt) {
		setHealth(health - amt);
	}
}
