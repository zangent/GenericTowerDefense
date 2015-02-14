package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.util.Utils;

public class Entity {
	
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
}
