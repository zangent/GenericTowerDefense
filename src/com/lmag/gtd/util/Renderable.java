package com.lmag.gtd.util;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Renderable {
	
	public Image img;
	private Vector2f pos;
	
	public Renderable(String path, float x, float y) {
		this(path, new Vector2f(x,y));
	}
	
	public Renderable(String path) {
		this(path, new Vector2f(0,0));
	}

	public Renderable(String path, Vector2f pos) {
		
		img = Utils.getImageFromPath(path);
		
		this.pos = pos;
	}
	
	public float getX() {
		return pos.x;
	}

	public Renderable setX(float x) {
		this.pos.x = x;
		return this;
	}

	public float getY() {
		return pos.y;
	}

	public Renderable setY(float y) {
		this.pos.y = y;
		return this;
	}
	
	public Vector2f getPos() {
		return pos;
	}
	
	public Renderable setPos(Vector2f newPos) {
		pos = newPos;
		return this;
	}
	
	public int getWidth() {
		return img.getWidth();
	}
	
	public int getHeight() {
		return img.getHeight();
	}

	public void render(Graphics g) {
		g.drawImage(img, pos.x, pos.y);
	}
}
