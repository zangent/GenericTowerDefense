package com.lmag.gtd.entities;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.util.Utils;

public class CopyOfTower2 extends TowerMachineGun {

	public CopyOfTower2(Vector2f pos) {
		super("edgeymemes.png", pos);
		turret = Utils.getImageFromPath("canun.png");
	}

}
