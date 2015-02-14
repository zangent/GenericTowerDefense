package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class Entity implements InputListener {
	
	public static int UPDATE_EVERY_EON = 500, UPDATE_SLOW = 200, UPDATE_MEDIUM = 75, UPDATE_FAST = 20;
	
	public int  updateRate;
	private int updateTime=0;
	
	public boolean visible = true;
	
	public ArrayList<Entity> children;
	private ArrayList<Entity> addChild;
	private ArrayList<Entity> remChild;
	
	protected Image sprite;
	
	protected Vector2f offset;
	
	public Entity parent;

	public Entity(String sprite, Vector2f position) {
		
		if(sprite!=null && !sprite.trim().matches(""))
			this.sprite = Utils.getImageFromPath(sprite);
		
		
		setPos(position);
		
		updateRate = UPDATE_FAST;
		
		children = new ArrayList<Entity>();
		addChild = new ArrayList<Entity>();
		remChild = new ArrayList<Entity>();
	}

	public Entity(Vector2f position) {
		
		this.sprite = null;
		
		visible = false;
		
		setPos(position);
		
		updateRate = UPDATE_FAST;
		
		children = new ArrayList<Entity>();
	}
	
	public Entity _setParent(Entity p) {
		parent = p;
		return this;
	}
	
	public void addAsInputListener() {
		MainGame.gc.getInput().addListener(this);
	}
	
	public Entity addChild(Entity futureChildYo) {
		addChild.add(futureChildYo);
		futureChildYo._setParent(this);
		return this;
	}
	
	public Entity removeChild(Entity futureChildYo) {
		remChild.add(futureChildYo);
		return this;
	}
	
	public void update(int dt) {}
	
	public void tick(int delta) {
		
		if (updateTime + delta >= updateRate) {
			
			update(updateTime + delta);
			
			updateTime = 0;
		}
		
		else {
			
			updateTime += delta;
		}
		

		for (Entity ent : addChild) {
			this.children.add(ent);
		}
		for (Entity ent : remChild) {
			this.children.remove(ent);
		}
		addChild.clear();
		remChild.clear();

		
		for (Entity ent : children) {
			
			ent.tick(delta);
		}
	}
	
	public float getX() {
		return getPos().x;
	}

	public Entity setX(float x) {
		setPos(new Vector2f(x,getY()));
		return this;
	}

	public float getY() {
		return getPos().y;
	}

	public Entity setY(float y) {
		setPos(new Vector2f(getX(),y));
		return this;
	}
	
	public Vector2f getPos() {
		return offset.copy().add( (parent != null) ? parent.getPos() : new Vector2f(0,0));
	}
	
	public Entity setPos(Vector2f newPos) {
		offset = newPos.sub( (parent != null) ? parent.getPos() : new Vector2f(0,0));
		return this;
	}
	
	
	public float getXOffset() {
		return getOffset().x;
	}

	public Entity setXOffset(float x) {
		setOffset(new Vector2f(x,getY()));
		return this;
	}

	public float getYOffset() {
		return getOffset().y;
	}

	public Entity setYOffset(float y) {
		setOffset(new Vector2f(getX(),y));
		return this;
	}
	
	public Vector2f getOffset() {
		return offset.copy();
	}
	
	public Entity setOffset(Vector2f newPos) {
		offset = newPos;
		return this;
	}
	
	public int getWidth() {
		return (sprite!=null)?sprite.getWidth():0;
	}
	
	public int getHeight() {
		return (sprite!=null)?sprite.getHeight():0;
	}
	
	public Vector2f getCenterPos() {
		return getPos().add(new Vector2f(getWidth()/2, getHeight()/2));
	}
	
	public Vector2f getCenterOffset() {
		return getOffset().add(new Vector2f(getWidth()/2, getHeight()/2));
	}
	
	public void render(Graphics g) {
		
		if(visible&&sprite!=null) g.drawImage(sprite, getX(), getY());
		
		for (Entity ent : children) {
			
			ent.render(g);
		}
	}
	
	
	public Entity setVisible(boolean visible) {
		
		this.visible = visible;
		
		return this;
	}
	
	/**
	 * Not preferred - use getCopyOfChildren() instead.
	 * @return The actual children
	 */
	public ArrayList<Entity> getChildren() {
		
		return children;
	}
	
	/**
	 * 
	 * Preferred - returns a copy of the children rather than the actual.
	 * @return a clone of the children.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Entity> getCopyOfChildren() {
		
		return (ArrayList<Entity>) children.clone();
	}
	
	public String getType() {
		
		Class<?> enclosingClass = getClass().getEnclosingClass();
		
		if (enclosingClass != null) {
			
			return enclosingClass.getName().replaceFirst("com.lmag.gtd.", "");
		}
		
		else {
			
			return getClass().getName().replaceFirst("com.lmag.gtd.", "");
		}
	}
	
	/**
	 * 
	 * I'm so, so sorry.
	 * 
	 * @param in point
	 * @return is it in dawg
	 */
	
	public boolean isPointInside(Vector2f in) {
		return (in.x>getX()&&in.x<getX()+getWidth()&&in.y>getY()&&in.y<getY()+getHeight());
	}

	@Override
	public boolean isAcceptingInput() {

		return false;
	}
	
	
	
	/*
	 * YE OLDE INPUT LISTENER LIST O' INPUTS
	 */

	@Override
	public void mouseClicked(int btn, int x, int y, int clickCount)  {
		
	}

	@Override
	public void mouseDragged(int x1, int y1, int x2, int y2) {
		
	}

	@Override
	public void mouseMoved(int x1, int y1, int x2, int y2) {
		
	}

	@Override
	public void mousePressed(int btn, int x, int y) {
		
	}

	@Override
	public void mouseReleased(int btn, int x, int y) {
		
	}

	@Override
	public void mouseWheelMoved(int amt) {
		
	}

	@Override
	public void inputEnded() {
		
	}

	@Override
	public void inputStarted() {
		
	}

	@Override
	public void setInput(Input arg0) {
		
	}

	@Override
	public void keyPressed(int kc, char ch) {
		
	}

	@Override
	public void keyReleased(int kc, char ch) {
		
	}

	@Override
	public void controllerButtonPressed(int arg0, int arg1) {
		
	}

	@Override
	public void controllerButtonReleased(int arg0, int arg1) {
		
	}

	@Override
	public void controllerDownPressed(int arg0) {
		
	}

	@Override
	public void controllerDownReleased(int arg0) {
		
	}

	@Override
	public void controllerLeftPressed(int arg0) {
		
	}

	@Override
	public void controllerLeftReleased(int arg0) {
		
	}

	@Override
	public void controllerRightPressed(int arg0) {
		
	}

	@Override
	public void controllerRightReleased(int arg0) {
		
	}

	@Override
	public void controllerUpPressed(int arg0) {
		
	}

	@Override
	public void controllerUpReleased(int arg0) {
		
	}
}
