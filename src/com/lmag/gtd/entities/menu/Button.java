package com.lmag.gtd.entities.menu;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.util.Executable;

public class Button extends Entity {
	
	protected Executable onPress;

	public Button(Vector2f position, Executable func) {
		super(position);

		this.addAsInputListener();
		onPress = func;
	}

	public Button(String sprite, Vector2f position, Executable func) {
		super(sprite, position);

		this.addAsInputListener();
		onPress = func;
	}

	@Override
	public boolean isAcceptingInput() {
		
		return true;
	}
	
	
	@Override
	public void update(int dt) {
		super.update(dt);
		
		
	}

	
	public void execFunc(Object... args) {
		
		onPress.run();
	}
	
	boolean buttonDown = false;
	
	@Override
	public void mousePressed(int btn, int x, int y) {
		
		buttonDown = (btn==0)&&isPointInside(new Vector2f(x,y));
		System.out.println(buttonDown);
	}
	
	@Override
	public void mouseReleased(int btn, int x, int y) {
		
		if(buttonDown && isPointInside(new Vector2f(x,y))) {
			execFunc();
		}
		
		buttonDown = false;
	}
}
