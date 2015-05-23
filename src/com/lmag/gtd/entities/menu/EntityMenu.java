package com.lmag.gtd.entities.menu;


import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.EntityLiving;
import com.lmag.gtd.entities.Upgrade;
import com.lmag.gtd.util.Executable;

public class EntityMenu extends Entity {
	
	public static final int width = 200;
	public static final int height = MainGame.HEIGHT;
	int size = MainGame.TOWER_SIZE, step = 4;
	int offsetX = step, offsetY = step;
	
	public EntityLiving target;
	
	public EntityMenu(EntityLiving ent) {
		super("buy_menu.png", new Vector2f(0, 0));
		
		target = ent;
		
		for (Upgrade upg : ent.elligibleUpgrades) {

			//debug
			System.out.println("|| " + upg.name());
			
			addUpgrade(upg, 5);
		}
	}
	
	public void addUpgrade(Upgrade upg, int price) {
		
		Executable e = new Executable() {
			
			@Override
			public void run() {
				
				try {
				
					if (MainGame.currency >= (int)params.get("price")) {
				
						MainGame.instance.lc.entityMenuRight.target.addUpgrade((Upgrade) params.get("upgrade"));
					}
				}
			
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		};


		e.params.put("upgrade", upg);
		e.params.put("price", price);
		
		try {
		
			this.addChild(new Button("Upgrade Icons/" + upg.toString() +".png", new Vector2f(offsetX, offsetY), e));
		
			offsetX += size + step;
			if(offsetX > width) {
				offsetX = step;
				offsetY += size + step;
			}
		}
		
		catch (Exception ex) {
			
			ex.printStackTrace();
		}
	}
}
