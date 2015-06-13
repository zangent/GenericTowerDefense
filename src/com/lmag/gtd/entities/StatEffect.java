package com.lmag.gtd.entities;


public class StatEffect {
	
	public final String name = "YouDunGoofed";

	/**
	 * How long, in seconds, the effect should last. -1 is infinite.
	 */
	public int duration = -1;
	protected int sinceLastUpdate = 0;
	
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
	public float percRegen = 0;

	/**
	 * Health lost every update
	 */
	public int DoT = 0;
	
	/**
	 * Percent of health lost every update
	 */
	public float percDoT = 0;
	
	
	public StatEffect(EntityLiving Parent, int Duration) {
		
		duration = Duration;
		
		parent = Parent;
	}
	
	public StatEffect(EntityLiving Parent) {
		
		parent = Parent;
	}
	
	
	public void onAdded() {
		
		
	}
	
	public void onRemoved() {
		
		
	}
	
	
	public void update(int dt) {
		
		sinceLastUpdate += dt;
		
		if (sinceLastUpdate >= 1000) {
			
			sinceLastUpdate -= 1000;
			
			if (duration != -1) {
				
				duration--;
			}
		}
		
		if (duration != -1 && duration <= 0) {
			
			parent.removeStatEffect(this);
		}
	}
}