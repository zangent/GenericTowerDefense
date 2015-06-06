package com.lmag.gtd.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public abstract class Tower extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	public boolean firing = false;
	
	public boolean constructing = false;
	

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
	
	
	public Tower(String sprite, Vector2f position) {
		super(sprite, position);
		this.addAsInputListener();
	}
	
	
	public float getDamage() {
		
		return (baseDamage * damagePercMod) + damageMod;
	}
	
	
	public int getRange() {

		return (int) ((baseRange * rangePercMod) + rangeMod);
	}
	
	
	public int getFireRate() {
		
		return (int) ((baseFireRate * fireRatePercMod) + fireRateMod);
	}
}
