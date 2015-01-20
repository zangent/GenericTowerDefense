package com.lmag.gtd.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;

public class DebugEnt extends Entity {
	public Entity smallBox;
	public DebugEnt(Entity attachTo) {
		super("redd.png", 0,0);
		attachTo.addChild(this);
		this.addChild(smallBox=new Entity("greend.png", 60,60) {
			public void update(int dt) {
				//super.update(dt);
				
				if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
					offset.add(new Vector2f(0,-1));
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
					offset.add(new Vector2f(-1,0));
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
					offset.add(new Vector2f(0,1));
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
					offset.add(new Vector2f(1,0));
				}
			}
		});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(int delta) {
		this.setOffset(MainGame.instance.getMousePos().sub(new Vector2f(MainGame.WIDTH/2, MainGame.HEIGHT/2)));
	}
}
