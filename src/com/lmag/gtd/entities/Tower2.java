package com.lmag.gtd.entities;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.util.Utils;

public class Tower2 extends Tower {

	public Tower2(Vector2f pos) {
		super("swaglord420.png", pos);
		turret = Utils.getImageFromPath("canun.png");
	}

}
