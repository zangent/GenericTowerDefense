package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;

public abstract class EntityLiving extends Entity {

	/**
	 * Base health value; Shouldn't be modified directly.
	 */
	protected float baseHealth = 100;
	/**
	 * The current health of the unit
	 */
	protected float health = baseHealth;
	/**
	 * Value added directly to base health.
	 */
	public float healthMod = 0;
	/**
	 * Base health is multiplied by this percentage.
	 */
	public float healthPercMod = 1.0f;
	

	/**
	 * Base regen value; Shouldn't be modified.
	 */
	protected float baseRegen = 0;
	/**
	 * Value added directly to base stat.
	 */
	public float regenMod = 0;
	/**
	 * Regen is multiplied by this percentage.
	 */
	public float regenPercMod = 1.0f;
	

	/**
	 * Base damage value; Shouldn't be modified.
	 */
	protected float baseDamage = 0;
	/**
	 * Value added directly to base stat.
	 */
	public float damageMod = 0;
	/**
	 * Damage is multiplied by this percentage.
	 */
	public float damagePercMod = 1.0f;


	/**
	 * Base detection range; Shouldn't be modified.
	 */
	protected int baseRange = 0;
	/**
	 * Value added directly to base stat.
	 */
	public int rangeMod = 0;
	/**
	 * Range is multiplied by this percentage.
	 */
	public float rangePercMod = 1.0f;
	

	/**
	 * Base fire rate; Shouldn't be modified.
	 */
	protected int baseFireRate = 0;
	/**
	 * Value added directly to base stat.
	 */
	public int fireRateMod = 0;
	/**
	 * fireRate is multiplied by this percentage.
	 */
	public float fireRatePercMod = 1.0f;
	
	
	public short isTarget = 0;
	
	private ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
	public ArrayList<String> availableUpgrades = new ArrayList<String>();
	
	protected int range = 500;

	public EntityLiving(String sprite, Vector2f position) {
		super(sprite, position);
	}
	
	
	public float getMaxHealth() {
		
		return (baseHealth * healthPercMod) + healthMod;
	}
	
	public float getHealth() {
		
		return health;
	}
	
	
	@Override
	public void update(int dt) {
		super.update(dt);
		
		heal((baseRegen * regenPercMod) + regenMod);
	}
	
	
	@Override 
	public void tick (int dt) {
		super.tick(dt);
		
	}
	
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(health != getMaxHealth()) {
			
			float per = ((float)health)/((float)getMaxHealth());
			
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
		
		if (MainGame.instance.debug) {
			
			g.setColor(Color.green);
			
			g.drawOval(this.getX()-range, this.getY()-range, range*2, range*2);
		}
	}
	

	public void addUpgrade(Upgrade upg) {
		
		for (String upgName : (ArrayList<String>)availableUpgrades.clone()) {
			
			if (upg.internalName.matches(upgName)) {
				
				if (!upgrades.contains(upg)) {
					
					upgrades.add(upg);
					
					availableUpgrades.remove(upgName);
					
					upg.onAdded();
				}
				
				return;
			}
		}
	}
	
	public void removeUpgrade(Upgrade upg) {
		
		if (upgrades.contains(upg)) {
			
			upgrades.remove(upg);
			
			availableUpgrades.add(upg.internalName);
			
			upg.onRemoved();
		}
	}
	
	
	public void heal(float healAmt) {
		
		if (health <= 0) {
			
			this.kill();
		}
		
		health += healAmt;
		
		float maxHealth = getMaxHealth();
		
		if (health > maxHealth) {
			
			health = maxHealth;
		}
	}
	
	public ArrayList<Upgrade> getUpgrades() {
		
		return upgrades;
	}

	
	public void setHealth(float health) {
		
		this.health = health;
		
		if(health <= 0) {
			
			this.kill();
		}
	}


	public void damage(float amt) {
		
		setHealth(health - amt);
	}
}
