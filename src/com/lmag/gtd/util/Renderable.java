package com.lmag.gtd.util;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;


public class Renderable extends Entity {
	
	public Renderable(String path, float x, float y) {
		this(path, new Vector2f(x,y));
	}
	
	public Renderable(String path) {
		this(path, new Vector2f(0,0));
	}

	public Renderable(String path, Vector2f pos) {
		super(path, pos);
	}
}
