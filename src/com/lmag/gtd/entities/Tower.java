package com.lmag.gtd.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;

public abstract class Tower extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	public boolean firing = false;
	
	public boolean constructing = false;
	
	boolean openMenuNextTick = false;
	

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
		
		availableUpgrades.add("UBERDamage");
		
		//debug
		System.out.println("Array contains: ");
		for (String thing : availableUpgrades) {
			System.out.println(thing);
		}
		
		this.addAsInputListener();
	}
	
	
	@Override 
	public void update(int delta) {
		super.update(delta);
		
		if(openMenuNextTick) {
			MainGame.instance.lc.setEntitySidebar(this);
			openMenuNextTick = false;
		}
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
	
	@Override
	public void onMouseClicked(int btn, int x, int y, int clickCount)  {

		//debug
		System.out.println("Step 1");
		
		if (this.isUpdating()) {
			//debug
			System.out.println("Step 2");
		
			if(this.contains(new Vector2f(x,y)) ||
					(!MainGame.instance.lc.buyMenuRight.open &&
							MainGame.instance.lc.entityMenuRight.
							contains(new Vector2f(x,y)))) {
				//debug
				System.out.println("Step 3");

			
				if(parent.updateChildren) {
					//debug
					System.out.println("Step 4");
			
					openMenuNextTick = true;
				}
			}
			
			else if(MainGame.instance.lc.selectedEntity == this) {
		
				MainGame.instance.lc.setBuySidebar();
			}
		}
	}
}
