package com.lmag.gtd.entities.menu;

import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.TowerLaser;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.EntityLiving;
import com.lmag.gtd.entities.MouseTracker;
import com.lmag.gtd.entities.TowerMachineGun;
import com.lmag.gtd.entities.TowerPulseCannon;
import com.lmag.gtd.util.Executable;

public class BuyMenu extends Entity {
	
	public static final int width = 200;
	public static final int height = MainGame.HEIGHT;
	int size = MainGame.TOWER_SIZE, step=4;
	int bpx=step, bpy=step;
	
	public BuyMenu() {
		super(new Vector2f(MainGame.WIDTH-width, 0));
		
		/*
		this.addChild(new Button("topkek2015.png", new Vector2f(0,0), new Executable(){
		

			@Override
			public void run() {
				MainGame.instance.root.addChild(new MouseTracker(new Tower(new Vector2f(0,0))));
		}}));
		*/
		addTower(new TowerMachineGun(new Vector2f(0,0)));
		addTower(new TowerPulseCannon(new Vector2f(0,0)));
		addTower(new TowerLaser(new Vector2f(0,0)));
	}
	
	public void addTower(Entity add) {
		
		Executable e = new Executable() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
				
				try {
					MainGame.instance.root.addChild(new MouseTracker((Entity) (((Class)params.get("class"))
							.getConstructor(Vector2f.class).newInstance(new Vector2f(0,0)))));

				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {

					e.printStackTrace();
				}
			}
		};
		e.params.put("class", add.getClass());
		
		this.addChild(new Button(new Vector2f(bpx, bpy), e).addChild(add));
		bpx+=size+step;
		if(bpx > width) {
			bpx = step;
			bpy += size + step;
		}
	}
	
	public void render(Graphics straightUpG) {
		straightUpG.setColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
		straightUpG.fillRect((int)getX(), (int)getY(), width, height);
		super.render(straightUpG);
	}
}
