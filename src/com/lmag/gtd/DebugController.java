package com.lmag.gtd;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.DebugEnemy;
import com.lmag.gtd.entities.Entity;

public class DebugController extends Entity {

	boolean md0 = false;
	
	public DebugController() {
		super("", new Vector2f(0,0));
		this.setVisible(false);
	}
	
	@Override
	public void update(int dt) {
		if(Mouse.isButtonDown(0)&&!md0) {
			MainGame.instance.root.addChild(new DebugEnemy(MainGame.instance.getMousePos()));
		}
		md0 = false;// Mouse.isButtonDown(0);
	}

}
