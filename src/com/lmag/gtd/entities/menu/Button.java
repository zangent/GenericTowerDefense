package com.lmag.gtd.entities.menu;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.util.Executable;

public class Button extends Entity {
	
	protected Executable onPress;
	
	protected Entity renderEnt;
	
	protected int forcedWidth=0, forcedHeight = 0;

    boolean use_child_bb = false;

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
	
	public Button forceSize(Vector2f size) {
		forcedWidth = (int)size.x;
		forcedHeight= (int)size.y;
        update_bb();
		return this;
	}
	
	public Button forceSize(int x, int y) {
		forcedWidth = x;
		forcedHeight= y;
        update_bb();
		return this;
	}

	
	public void execFunc(Object... args) {
		
		onPress.run();
	}
	
	boolean buttonDown = false;
	
	@Override
	public void onMousePressed(int btn, int x, int y) {
		
		buttonDown = (btn==0)&&contains(new Vector2f(x,y));
	}
	
	@Override
	public void onMouseReleased(int btn, int x, int y) {
        System.out.println("--------------------");
        System.out.println(get_bb());
        System.out.println("vs");
        System.out.println(new Vector2f(x,y));
        System.out.println("--------------------");
		if(buttonDown && contains(new Vector2f(x,y))) {
			execFunc();
		}
		
		buttonDown = false;
	}
	
	public int getWidth() {
		return (forcedWidth!=0)?forcedWidth:(children.size()!=0)?children.get(0).getWidth():((sprite!=null)?sprite.getWidth():0);
	}
	
	public int getHeight() {
		return (forcedHeight!=0)?forcedHeight:(children.size()!=0)?children.get(0).getHeight():((sprite!=null)? sprite.getHeight():0);
	}

    public Rectangle get_bb() {
        return use_child_bb?this.getChildren().get(0).get_bb():super.get_bb();
    }

    public Button useChildBB() {
        use_child_bb = true;
        return this;
    }
}
