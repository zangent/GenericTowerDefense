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
	
	protected boolean shouldUpdate = true;
	public boolean updateChildren = true;
	
	public boolean killed = false;
	
	protected ArrayList<StatEffect> statEffects = new ArrayList<StatEffect>();
	
	protected int EMPTimer = 0;
	
	public short EMPEndTime = 1500;
	
	protected Entity EMPIndicator;
	
	private boolean acceptingInput = false;
	
	

	public Entity(String sprite, Vector2f position) {
		
		this(position, ((sprite!=null && !sprite.trim().matches(""))?Utils.getImageFromPath(sprite):null));
		
	}

	public Entity(Vector2f position) {
		
		this(position, null);
		visible = false;
	}
	
	private Entity(Vector2f position, Image sprite) {

		this.sprite = sprite;
		
		setPos(position);
		
		updateRate = UPDATE_FAST;
		
		children = new ArrayList<Entity>();
		addChild = new ArrayList<Entity>();
		remChild = new ArrayList<Entity>();
	}
	
	public Entity _setParent(Entity p) {
		parent = p;

		return this;
	}
	
	public void addAsInputListener() {
		MainGame.gc.getInput().addListener(this);
		acceptingInput = true;
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
		
		if(EMPIndicator != null)
		EMPIndicator.tick(delta);
		
		if (statEffects.contains(hasStatEffect(StatEffects.EMP))) {
			
			if (EMPTimer < EMPEndTime) {
				
				EMPTimer += delta;
				return;
			}
			
			if (EMPTimer >= EMPEndTime) {
				
				EMPTimer = 0;
				removeStatEffect(StatEffects.EMP);
			}
		}
		
		if (updateTime + delta >= updateRate) {
			
			if (isUpdating()) {	
				
				update(updateTime + delta);
			}
			
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
			if(!ent.killed) {
				ent.killed = true;
				ent.kill();
			}
		}
		addChild.clear();
		remChild.clear();

		if(updateChildren)
		for (Entity ent : children) {
			
			if(ent != EMPIndicator)
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
	
	
	public ArrayList<StatEffect> getStatEffects() {
		
		return statEffects;
	}
	
	public void addStatEffect(StatEffect effect) {
		
		if (!statEffects.contains(effect)) {
			
			statEffects.add(effect);
			effect.onAdded();
		}
	}
	
	public void removeStatEffect(StatEffect effect) {
		effect.onRemoved();
		statEffects.remove(effect);
	}
	
	public void removeStatEffect(String effect) {
		for(StatEffect se : statEffects) {
			if(se.name.matches(effect)) {
				se.onRemoved();
				statEffects.remove(se);
			}
		}
	}
	
	public boolean hasStatEffect(StatEffect effect) {
		
		return statEffects.contains(effect);
	}
	
	
	public void render(Graphics g) {
		
		if(visible&&sprite!=null) g.drawImage(sprite, getX(), getY());
		
	}
	
	public void renderAll(Graphics g) {
		render(g);
		for (Entity ent : children) {
			if(ent != EMPIndicator)
				ent.renderAll(g);
		}
		if(EMPIndicator != null)
			EMPIndicator.renderAll(g);
	}
	
	public Entity setVisible(boolean visible) {
		
		this.visible = visible;
		
		return this;
	}
	
	public boolean isUpdating() {

		if (hasStatEffect(StatEffects.constructing)) {
			
			
			this.removeStatEffect(StatEffects.constructing);
			
			return false;
		}
		return shouldUpdate && (parent == null ? true : (parent.isUpdating() && parent.updateChildren));
	}

	public Entity setUpdating(boolean shouldUpdate) {
		
		this.shouldUpdate = shouldUpdate;
		
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
	
	public void kill() {
		
		if (this.isAcceptingInput()) {
			MainGame.gc.getInput().removeListener(this);
		}
		
		if (!killed) {
			
			parent.removeChild(this);
		}
		
		if (MainGame.instance.lc.selectedEntity == this) {
			
			MainGame.instance.lc.setBuySidebar();
		}
		
		
		for(Entity e : children) {
			
			e.kill();
		}
		
		killed = true;
	}
	
	/**
	 * 
	 * I'm so, so sorry.
	 * 
	 * @param in point
	 * @return is it in dawg
	 */
	
	public boolean contains(Vector2f in) {
		return (in.x>getX()&&in.x<getX()+getWidth()&&in.y>getY()&&in.y<getY()+getHeight());
	}

	
	public boolean isAcceptingInput() {

		return acceptingInput;
	}
	
	public boolean hasStatEffect(String name) {
		
		for(StatEffect se : this.statEffects) {
			if(se.name.matches(name)) return true;
		}
		return false;
	}
	
	
	
	/*
	 * YE OLDE INPUT LISTENER LIST O' INPUTS
	 */

	
	public void onMouseClicked(int btn, int x, int y, int clickCount)  {

	}

	
	public void onMouseDragged(int x1, int y1, int x2, int y2) {
		
	}

	
	public void onMouseMoved(int x1, int y1, int x2, int y2) {
		
	}

	
	public void onMousePressed(int btn, int x, int y) {
		
	}

	
	public void onMouseReleased(int btn, int x, int y) {
		
	}

	
	public void onMouseWheelMoved(int amt) {
		
	}

	
	public void onInputEnded() {
		
	}

	
	public void onInputStarted() {
		
	}

	
	public void setInputEntity(Input arg0) {
		
	}

	
	public void onKeyPressed(int kc, char ch) {
		
	}

	
	public void onKeyReleased(int kc, char ch) {
		
	}

	
	
	
	
	
	
	
	@Override
	public void mouseClicked(int btn, int x, int y, int clickCount) {

		if (this.isUpdating()) {
			
			onMouseClicked(btn, x, y, clickCount);
		}
	}

	@Override
	public void mouseDragged(int x1, int y1, int x2, int y2) {
		if (this.isUpdating()) {
			
			onMouseDragged(x1, y1, x2, y2);
		}
	}

	@Override
	public void mouseMoved(int x1, int y1, int x2, int y2) {

		if (this.isUpdating()) {
			
			onMouseMoved(x1, y1, x2, y2);
		}
	}

	@Override
	public void mousePressed(int btn, int x, int y) {

		if (this.isUpdating()) {
			
			onMousePressed(btn, x, y);
		}
	}

	@Override
	public void mouseReleased(int btn, int x, int y) {

		if (this.isUpdating()) {
			
			onMouseReleased(btn, x, y);
		}
	}

	@Override
	public void mouseWheelMoved(int amt) {

		if (this.isUpdating()) {
			
			onMouseWheelMoved(amt);
		}
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

		if (this.isUpdating()) {
			
			onKeyPressed(kc, ch);
		}
	}

	@Override
	public void keyReleased(int kc, char ch) {

		if (this.isUpdating()) {
			
			onKeyReleased(kc, ch);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void controllerButtonPressed(int arg0, int arg1) {
		
	}

	
	public void controllerButtonReleased(int arg0, int arg1) {
		
	}

	
	public void controllerDownPressed(int arg0) {
		
	}

	
	public void controllerDownReleased(int arg0) {
		
	}

	
	public void controllerLeftPressed(int arg0) {
		
	}

	
	public void controllerLeftReleased(int arg0) {
		
	}

	
	public void controllerRightPressed(int arg0) {
		
	}

	
	public void controllerRightReleased(int arg0) {
		
	}

	
	public void controllerUpPressed(int arg0) {
		
	}

	
	public void controllerUpReleased(int arg0) {
		
	}
}
