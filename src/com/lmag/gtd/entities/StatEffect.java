package com.lmag.gtd.entities;

import org.newdawn.slick.Graphics;


public class StatEffect {
	
	public final String internalName = "YouDunGoofed";
	public final String displayName = "YouDunGoofed";
	
	public int  updateRate = Entity.UPDATE_MEDIUM;
	private int updateTime = 0;

	/**
	 * How long, in milliseconds, the effect should last. -1 is infinite.
	 */
	public int duration = -1;
	
	/**
	 * The entity this effect is applied to.
	 */
	protected EntityLiving parent = null;
	
	/**
	 * Value added directly to base stat.
	 */
	public int healthMod = 0;

	/**
	 * Value added directly to base stat.
	 */
	public int rangeMod = 0;
	

	/**
	 * Base stat is multiplied by this value.
	 */
	public float fireRateMod = 1;

	/**
	 * Base stat is multiplied by this value.
	 */
	public float updateRateMod = 1;
	

	/**
	 * Health regenerated every update
	 */
	public int regen = 0;
	
	/**
	 * Percent of health regenerated every update
	 */
	public float regenPercMod = 0;
	
	
	public StatEffect(EntityLiving parent, int duration) {
		
		this.duration = duration;
		
		this.parent = parent;
	}
	
	public StatEffect(EntityLiving Parent) {
		
		parent = Parent;
	}
	
	
	public void onAdded() {
		
		
	}
	
	public void onRemoved() {

	}

    public void onUpdate(int dt) {

    }
	
	public void update(int dt) {

        onUpdate(dt);

        if (duration != -1) {
            duration -= dt;

            if(duration == -1)
                duration = -2;
        }
		
		if (duration != -1 && duration <= 0) {
			
			parent.removeStatEffect(this);
		}

	}

	public void replaces(StatEffect old) {
		
		
	}
}