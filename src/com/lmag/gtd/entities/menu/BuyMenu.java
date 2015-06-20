package com.lmag.gtd.entities.menu;

import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.TowerLaser;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.MouseTracker;
import com.lmag.gtd.entities.TowerMachineGun;
import com.lmag.gtd.entities.TowerPulseCannon;
import com.lmag.gtd.util.Executable;

public class BuyMenu extends Entity {
	
	public static final int width = 200;
	public static final int height = MainGame.HEIGHT;
	
	int size = MainGame.TOWER_SIZE, step=4;
	int offsetX = step, offsetY = step;
	
	public boolean open = true;
	
	public BuyMenu() {
		super("buy_menu.png", new Vector2f(0, 0));
		
		addTower(new TowerMachineGun(new Vector2f(0,0)), 50);
		addTower(new TowerPulseCannon(new Vector2f(0,0)), 100);
		addTower(new TowerLaser(new Vector2f(0,0)), 150);
	}
	
	public void addTower(Entity add, int price) {
		
		Executable e = new Executable() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
				
				if (MainGame.currency >= (int)params.get("price")) {
				
					try {
						MainGame.instance.root.addChild(new MouseTracker(((Entity) (((Class)params.get("class"))
								.getConstructor(Vector2f.class).newInstance(new Vector2f(0,0)))).setUpdating(false), (int)params.get("price")));
						

					} catch (InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {

						e.printStackTrace();
					}
				}
			}
		};
		
		e.params.put("class", add.getClass());
		e.params.put("price", price);
		
		this.addChild(((Button)new Button(new Vector2f(offsetX, offsetY), e).addChild(add)).useChildBB());
		offsetX += size+step;
		
		if(offsetX > width) {
			
			offsetX = step;
			offsetY += size + step;
		}
	}
	
	@Override
	public boolean isUpdating() {
		
		return super.isUpdating() && open;
	}
	
	public void renderAll(Graphics straightUpG) {
		
		if(open)
			super.renderAll(straightUpG);
	}
	
	
	/*/
	public void tick(int dt) {
		if(open)
			super.tick(dt);
	}/**/
}
