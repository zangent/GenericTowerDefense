package com.lmag.gtd;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.DebugEnemy;
import com.lmag.gtd.entities.DebugEnt;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.util.Utils;

public class LevelController extends Entity {
	
	Vector2f[] path;
	int t = 0;
	
	
	public LevelController() {
		super("", new Vector2f(0,0));
		
		this.setVisible(false);
		
		path = Utils.getLevelPath("maps/check.txt");
		
		this.addAsInputListener();
	}
	
	@Override
	public boolean isAcceptingInput() {
		
		return true;
	}
	
	
	@Override
	public void update(int dt) {
		
		t+= dt;
		if( t> 200) {
			t=0 ;
			 MainGame.instance.root.addChild(new DebugEnemy(new Vector2f(0,0)).setPath(path));
		}
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
		
	}
	
	public Vector2f[] getPath() {
		return path;
	}
}
