package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class Entity implements InputListener {
	
	public static int UPDATE_EVERY_EON = 500, UPDATE_SLOW = 200, UPDATE_MEDIUM = 75, UPDATE_FAST = 20;



	/**
	 * Base update rate; Shouldn't be modified.
	 */
	protected int baseUpdateRate;
	/**
	 * Value added directly to base stat.
	 */
	public int updateRateMod = 0;
	/**
	 * Update rate is multiplied by this percentage.
	 */
	public float updateRatePercMod = 1.0f;
	
	private int updateTime = 0;
	
	
	public boolean visible = true;
	
	public ArrayList<Entity> children;
	private ArrayList<Entity> addChild;
	private ArrayList<Entity> remChild;
	
	protected Image sprite;
	
	protected Vector2f offset = new Vector2f(0);

	public Entity parent;
	
	protected boolean shouldUpdate;
	public boolean updateChildren = true;
	
	public boolean killed = false;
	
	protected ArrayList<StatEffect> statEffects = new ArrayList<StatEffect>();
	
	private boolean acceptingInput = false;

    protected Rectangle bounding_box;
	
	

	public Entity(String sprite, Vector2f position) {
		
		this(position, ((sprite!=null && !sprite.trim().matches(""))?Utils.getImageFromPath(sprite):null));
		
	}

	public Entity(Vector2f position) {
		
		this(position, null);
		visible = false;
	}
	
	private Entity(Vector2f position, Image sprite) {
		
		shouldUpdate = true;

		this.sprite = sprite;
		
		baseUpdateRate = UPDATE_FAST;
		
		children = new ArrayList<Entity>();
		addChild = new ArrayList<Entity>();
		remChild = new ArrayList<Entity>();

        this._setParent(MainGame.instance.root);

        setPos(position);
	}
	
	public Entity _setParent(Entity p) {
		parent = p;

        update_bb();

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

    protected void update_bb() {

        if (parent == null) {

            return;
        }

        if(this instanceof com.lmag.gtd.entities.TowerLaser &&
                this.parent instanceof com.lmag.gtd.entities.menu.Button) {

        }

        bounding_box = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public Rectangle get_bb() {

        return new Rectangle (
                bounding_box.getX(),bounding_box.getY(),
                bounding_box.getWidth(),bounding_box.getHeight()
        );
    }
	
	public void update(int dt) {}
	
	
	public void tick(int delta) {
        try {
            bounding_box = new Rectangle(this.getX()+1, this.getY()+1, this.getWidth()-2, this.getHeight()-2);
        } catch (Exception e) {
            bounding_box = new Rectangle(this.getX(), this.getY(), 1, 1);
        }
		
		float finalUpdateRate = getUpdateRate();
		
		if (updateTime + delta >= finalUpdateRate) {
			
			if (isUpdating()) {	
				
				update(updateTime + delta);
			}
			
			if (!statEffects.isEmpty()) {
					
				for (StatEffect effect : ((ArrayList<StatEffect>)statEffects.clone())) {
					
					effect.update(updateTime + delta);
				}
			}
			
			updateTime -= finalUpdateRate;
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

		if(updateChildren) {
			
			for (Entity ent : children) {
			
				ent.tick(delta);
			}
		}
	}
	
	public float getX() {
		return getPos().getX();
	}
    public int getTileX() {
        return (int)(getX()/MainGame.TOWER_SIZE);
    }

	public Entity setX(float x) {

		setPos(new Vector2f(x,getY()));
        this.update_bb();

		return this;
	}

	public float getY() {
        return getPos().getY();
	}
    public int getTileY() {
        return (int)(getY()/MainGame.TOWER_SIZE);
    }

	public Entity setY(float y) {

		setPos(new Vector2f(getX(),y));
        this.update_bb();

		return this;
	}
	
	public Vector2f getPos() {
		return offset.copy().add( (parent != null) ? parent.getPos() : new Vector2f(0,0));
	}
	
	public Entity setPos(Vector2f newPos) {
		offset = newPos.sub( (parent != null) ? parent.getPos() : new Vector2f(0,0));

        update_bb();

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
        update_bb();
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
		
		if (statEffects.contains(effect)) {
			StatEffect ose = getStatEffect(effect.internalName);
			effect.replaces(ose);
			statEffects.remove(ose);
		}
		statEffects.add(effect);
		effect.onAdded();
	}
	
	public void removeStatEffect(StatEffect effect) {
		effect.onRemoved();
		statEffects.remove(effect);
	}
	
	public void removeStatEffect(String effect) {
		for(StatEffect se : statEffects) {
			if(se.internalName.matches(effect)) {
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

			ent.renderAll(g);
		}
	}
	
	public Entity setVisible(boolean visible) {
		
		this.visible = visible;
		
		return this;
	}
	
	public boolean isUpdating() {
		
		return shouldUpdate && (parent == null ? true : (parent.isUpdating() && parent.updateChildren));
	}

	public Entity setUpdating(boolean shouldUpdate) {

		this.shouldUpdate = shouldUpdate;
		
		return this;
	}
	
	public float getUpdateRate() {
		
		return (baseUpdateRate * updateRatePercMod) + updateRateMod;
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
		
		this.onDeath();
	}
	
	protected void onDeath() {}
	
	/**
	 * 
	 * I'm so, so sorry.
	 * 
	 * @param in point
	 * @return is it in dawg
	 */
	
	public boolean contains(Vector2f in) {
		return get_bb().contains(in.getX(), in.getY());
	}

	
	public boolean isAcceptingInput() {

		return acceptingInput;
	}
	
	public StatEffect getStatEffect(String name) {
		
		for(StatEffect se : this.statEffects) {

			if(se.internalName.matches(name)) {
				
				return se;
			}
		}
		
		return null;
	}
	
	public boolean hasStatEffect(String name) {
		
		for(StatEffect se : this.statEffects) {
			
			if(se.internalName.matches(name)) {
				
				return true;
			}
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
