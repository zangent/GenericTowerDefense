package com.lmag.gtd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Enemy;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.EntityLiving;
import com.lmag.gtd.util.Utils;

public class LevelController extends Entity {
	
	Vector2f[] path;
	
	int lastUpdate = 0;
	int lifetime = 0;
	int waveCount = 0;
	int enemyCount = 0;
	
	ArrayList<String[]> enemyData = new ArrayList<String[]>();
	
	
	@SuppressWarnings("unchecked")
	public LevelController() {
		super("", new Vector2f(0,0));
		
		this.setVisible(false);
		
		Object[] map = Utils.loadLevel("maps/check.txt");
		
		enemyData = (ArrayList<String[]>) map[0];
		
		path = (Vector2f[]) map[1];
		
		this.addAsInputListener();
	}
	
	
	@Override
	public boolean isAcceptingInput() {
		
		return true;
	}
	
	
	@Override
	public void update(int dt) {
		
		lifetime += dt;
		lastUpdate += dt;
		
		if(lastUpdate > 200) {
			
			lastUpdate = 0;
			
			addEnemy();
		}
		if(this.children.size()==0) {return;}
		Entity c1 = this.children.get(0);
		if(c1 != null) {
			System.err.println(c1.getPos());
		}
	}
	
	public EntityLiving addBuffWithChance(EntityLiving e) {

		System.out.println("ayy4");
		addChild(e);
		((Enemy)e).setPath(path);
		System.out.println("ayy5");
		enemyCount++;
		return e;
	}
	
	public EntityLiving addEnemy() {
		System.out.println("ayy1");
		String[] options = enemyData.get(waveCount);
		EntityLiving enemy = null;

		System.out.println("ayy2");
		try {
			enemy = ((EntityLiving) (Class.forName("com.lmag.gtd.entities.enemies."
					+ options[(int) (Math.random() * (options.length))])
					.getConstructor(Vector2f.class).newInstance(new Vector2f(-100, -100))));

			System.out.println(enemy);
			addBuffWithChance(enemy);

		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {

			e.printStackTrace();
		}

		return enemy;
	}
	
	@Override
	public Entity removeChild(Entity e) {
		
		enemyCount--;
		return super.removeChild(e);
	}

	@Override
	public void mouseReleased(int btn, int x, int y) {
		
		if(btn==0) {
			//MainGame.instance.root.addChild(new DebugEnt(MainGame.instance.getMousePos()));
		}
	}
	
	@Override
	public void keyReleased(int kc, char ch) {
		
		
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
	}
	
	public Vector2f[] getPath() {
		return path;
	}
}
