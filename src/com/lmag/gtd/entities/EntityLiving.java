package com.lmag.gtd.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public abstract class EntityLiving extends Entity {
	
	protected int defaultHealth = 100;
	protected int health = defaultHealth;
	public int isTarget=0;

	public EntityLiving(String sprite, Vector2f position) {
		super(sprite, position);
	}
	
	public int getHealth() {
		return health;
	}
	
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(health != defaultHealth) {
			
			float per = ((float)health)/((float)defaultHealth);
			
			if(per < 0) per = 0;
			
			System.out.println(health + " " + defaultHealth);
			
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
	}

	
	public void setHealth(int health) {
		this.health = health;
		if(health <= 0) {
			onDeath();
			parent.removeChild(this);
		}
	}
	
	private void onDeath() {}


	public void damage(int amt) {
		setHealth(health - amt);
	}
}
