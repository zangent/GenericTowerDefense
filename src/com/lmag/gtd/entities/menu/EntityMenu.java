package com.lmag.gtd.entities.menu;


import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.MouseTracker;
import com.lmag.gtd.entities.Tower;
import com.lmag.gtd.entities.Upgrade;
import com.lmag.gtd.entities.Upgrades;
import com.lmag.gtd.util.Executable;
import com.lmag.gtd.util.Utils;

public class EntityMenu extends Entity {
	
	public static final int width = 200;
	public static final int height = MainGame.HEIGHT;
	int size = MainGame.TOWER_SIZE, step = 4;
	int offsetX = step, offsetY = step;
	
	public Tower target;
	
	public EntityMenu(Tower ent) {
		super("buy_menu.png", new Vector2f(0, 0));
		
		target = ent;
		
		Utils.printStackTrace();

			//debug
			System.out.println("Beginning list...");
		
		for (String upgName : ent.availableUpgrades) {

			//debug
			System.out.println("|| " + upgName);
			
			addUpgrade(upgName, 5);
		}
		
		//debug
		System.out.println("Ending list...");
	}
	
	public void addUpgrade(String upgName, int price) {
		
		Executable e = new Executable() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
				
				if (/*debug*/ true || MainGame.currency >= (int)params.get("price")) {
					
					try {
						Upgrades u = new Upgrades();
						((Tower)params.get("parent")).addUpgrade((Upgrade)(Upgrades.class.getDeclaredMethod((String)params.get("name"), Tower.class)).invoke(u, (Tower)params.get("parent")));

					} catch (IllegalAccessException
							| IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {

						e.printStackTrace();
					}
				}
			}
		};
		
		/*
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
		};*/


		e.params.put("name", upgName);
		e.params.put("parent", target);
		e.params.put("price", price);
		
		try {
		
			this.addChild(new Button("upgrade_icons/" + upgName + ".png", new Vector2f(offsetX, offsetY), e));
		
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
