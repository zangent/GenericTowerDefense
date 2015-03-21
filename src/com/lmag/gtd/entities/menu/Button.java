package com.lmag.gtd.entities.menu;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.util.Executable;

public class Button extends Entity {
	
	protected Executable onPress;
	
	protected Entity renderEnt;
	

	public Button(Vector2f position, Executable func) {
		super(position);

		this.addAsInputListener();
		onPress = func;
		updateChildren = false;
	}

	public Button(String sprite, Vector2f position, Executable func) {
		super(sprite, position);

		this.addAsInputListener();
		onPress = func;
		updateChildren = false;
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
	
	public int getWidth() {
		return (children.size()!=0)?children.get(0).getWidth():((sprite!=null)?sprite.getWidth():0);
	}
	
	public int getHeight() {
		return (children.size()!=0)?children.get(0).getHeight():((sprite!=null)?sprite.getHeight():0);
	}
}
