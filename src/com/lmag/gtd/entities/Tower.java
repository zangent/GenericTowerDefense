package com.lmag.gtd.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public abstract class Tower extends EntityLiving {
	
	Image turret;
	
	public EntityLiving target;
	
	public boolean firing = false;
	
	public boolean constructing = false;
	
	public boolean openMenuNextTick = false;
	
	public short targetingDisablers = 0;

    private boolean first_update = true;
	
	public Tower(String sprite, Vector2f position) {
		super(sprite, position);
		
		availableUpgrades.add("UBERDamage");
		
		this.addAsInputListener();
	}
	
	
	@Override 
	public void update(int delta) {
		super.update(delta);

        if(first_update) {
            MainGame.instance.lc.on_thing_placed();
            first_update = false;
        }

		if(openMenuNextTick) {
			
			MainGame.instance.lc.setEntitySidebar(this);
			openMenuNextTick = false;
		}
	}
	
	
	@Override
	public void tick(int dt) {
		super.tick(dt);
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
	
	
	public Enemy getTarget() {
		
		if (targetingDisablers!=0) {
			
			return null;
		}
		
		return (Enemy) Utils.getNearestEntity(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Enemy"), this.getCenterPos(), getRange());
	}
	
	
	@Override
	public void onMouseClicked(int btn, int x, int y, int clickCount)  {
		
		if (this.isUpdating()) {
		
			if(this.contains(new Vector2f(x,y)) ||
					(!MainGame.instance.lc.buyMenuRight.open &&
							MainGame.instance.lc.entityMenuRight.
							contains(new Vector2f(x,y)))) {

			
				if(parent.updateChildren) {
			
					openMenuNextTick = true;
				}
			}
			
			else if(MainGame.instance.lc.selectedEntity == this) {
		
				MainGame.instance.lc.setBuySidebar();
			}
		}
	}
}
