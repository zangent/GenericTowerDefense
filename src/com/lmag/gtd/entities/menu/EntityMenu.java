package com.lmag.gtd.entities.menu;

import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
import com.lmag.gtd.entities.TowerLaser;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.EntityLiving;
import com.lmag.gtd.entities.MouseTracker;
import com.lmag.gtd.entities.TowerMachineGun;
import com.lmag.gtd.entities.TowerPulseCannon;
import com.lmag.gtd.util.Executable;

public class EntityMenu extends Entity {
	
	public static final int width = 200;
	public static final int height = MainGAme.HEIGHT;
	int size = MainGAme.TOWER_SIZE, step=4;
	int bpx=step, bpy=step;
	
	public Entity target;
	
	public EntityMenu(Entity ent) {
		super("buy_menu.png", new Vector2f(0, 0));
		

		target = ent;
		

		addTower(ent, 7);
	}
	
	public void addTower(Entity add, int price) {
		
		Executable e = new Executable() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
				
				if (MainGAme.currency >= (int)params.get("price")) {
				
					try {
						
						MainGAme.instance.root.addChild(new MouseTracker((Entity) (((Class)params.get("class"))
								.getConstructor(Vector2f.class).newInstance(new Vector2f(0,0))), (int)params.get("price")));
						

					} catch (InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {

						e.printStackTrace();
					}
				}
			}
		};

		
		this.addChild(new Button(new Vector2f(bpx, bpy), e).addChild(add));
		bpx+=size+step;
		if(bpx > width) {
			bpx = step;
			bpy += size + step;
		}
	}
}
