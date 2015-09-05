package com.lmag.gtd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Enemy;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.EntityLiving;
import com.lmag.gtd.entities.Tower;
import com.lmag.gtd.entities.menu.BuyMenu;
import com.lmag.gtd.entities.menu.EntityMenu;
import com.lmag.gtd.util.Utils;

public class LegacyLevelController extends Entity {

	Vector2f[] path;
	
	int lastUpdate = 0;
	int lifetime = 0;
	int waveCount = 0;
	int enemyCount = 0;
	
	// Self-documenting code...
	int currentMaxEnemyCountForThePresentWaveThatIsOccuring = 1;
	int theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave = 1000;
	int theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave = 0;
	
	ArrayList<int[]> waveData = new ArrayList<int[]>();
	ArrayList<String[]> enemyData = new ArrayList<String[]>();
	
	public BuyMenu buyMenuRight;
	public EntityMenu entityMenuRight;
	
	public Entity selectedEntity;
	
	
	@SuppressWarnings("unchecked")
	public LegacyLevelController() {
		super("", new Vector2f(0,0));
		
		this.setVisible(false);
		
		buyMenuRight = new BuyMenu();
		
		addChild(buyMenuRight);
		
		Object[] map = Utils.loadLevel("maps/checkem.txt");
		
		ArrayList<String[]> unparsedEnemyData = (ArrayList<String[]>) map[0];
		for(int i=0;i<unparsedEnemyData.size();i++) {
			String[] ed = unparsedEnemyData.get(i);
			ArrayList<String> nd = new ArrayList<String>();
			int[] data = new int[]{420,1000};
			for(String s:ed) {
				if(s.matches("^W[0-9]+$")) {
					data[0] = Integer.parseInt(s.replaceFirst("W", ""));
				} else if(s.matches("^R[0-9]+$")) {
					data[1] = Integer.parseInt(s.replaceFirst("R", ""));
				} else {
					nd.add(s);
				}
			}
			enemyData.add(nd.toArray(new String[nd.size()]));
			waveData.add(data);
		}
		
		
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
		
		MainGame.currency += dt*0.001;
		
		if (selectedEntity == null) {
			
			entityMenuRight = null;
		}
		
		if(lastUpdate > theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave) {
			
			lastUpdate = 0;
			theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave++;
			createEnemy();
		}
		if(this.children.size()==0) {return;}
		Entity c1 = this.children.get(0);
		if(currentMaxEnemyCountForThePresentWaveThatIsOccuring <= theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave) {
			
			waveCount++;
			if(waveCount >= waveData.size()) {
				// TODO: A WINRAR IS U
				waveCount = 0;
			}
			theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave = 0;
			int[] cdata = waveData.get(waveCount);
			currentMaxEnemyCountForThePresentWaveThatIsOccuring = cdata[0];
			theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave = cdata[1];
		}
	}
	
	public EntityLiving addBuffWithChance(EntityLiving e) {

		addChild(e);
		((Enemy)e).setPath(path);
		return e;
	}
	
	public EntityLiving createEnemy() {
		
		String[] options = enemyData.get(waveCount);
		EntityLiving enemy = null;
		
		try {
			enemy = ((EntityLiving) (Class.forName("com.lmag.gtd.entities.enemies."
					+ options[(int) (Math.random() * (options.length))])
					.getConstructor(Vector2f.class).newInstance(new Vector2f(-100, -100))));

			addBuffWithChance(enemy);

		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {

			e.printStackTrace();
		}
		
		enemyCount++;

		return enemy;
	}
	
	public void setEntitySidebar(Tower e) {

		if(entityMenuRight != null)
			MainGame.instance.lc.removeChild(entityMenuRight);
		
		selectedEntity = e;
		
		entityMenuRight = new EntityMenu(e);
		
		MainGame.instance.lc.addChild(entityMenuRight);
		buyMenuRight.open = false;
	}
	
	public void setBuySidebar() {
		
		if(entityMenuRight != null)
			MainGame.instance.lc.removeChild(entityMenuRight);
		
		buyMenuRight.open = true;
		selectedEntity = null;
	}
	
	@Override
	public Entity removeChild(Entity e) {
		
		if (e instanceof EntityLiving) {

			enemyCount--;
		}
		
		return super.removeChild(e);
	}

	@Override
	public void onMouseReleased(int btn, int x, int y) {
		
	}
	
	@Override
	public void onKeyReleased(int kc, char ch) {
		
		
	}
	
	@Override	
	public void renderAll(Graphics g) {
		
		for (Entity ent : children) {
			
			if (ent == buyMenuRight) {
				
				if (entityMenuRight == null) {

					ent.renderAll(g);
				}
			}
			
			else {
				
				ent.renderAll(g);
			}
		}
	}
	
	public Vector2f[] getPath() {
		return path;
	}
}
