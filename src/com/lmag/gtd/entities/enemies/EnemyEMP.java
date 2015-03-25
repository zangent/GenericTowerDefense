package com.lmag.gtd.entities.enemies;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.Enemy;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.StatEffect;
import com.lmag.gtd.util.Utils;

public class EnemyEMP extends Enemy {
	
	
	public int EMPCooldown = (int) (6000 + 6000d * Math.random()), sinceLastEMP = EMPCooldown;
	
	protected int range = 350;

	public EnemyEMP(Vector2f position) {
		super("enemya.png", position);
	}

	
	
	@Override
	public void update(int dt) {
		super.update(dt);
		
		
		if (sinceLastEMP >= EMPCooldown) {
		
			ArrayList<Entity> targets = Utils.getNearestEntities(Utils.sortByType(MainGame.instance.lc.getCopyOfChildren(), "Tower"), this.getPos(), range, -1);
			
			if (targets != null) {
				
				for (Entity target : targets) {
					
					target.addStatEffect(StatEffect.EMP);
				}
			}
		}
	}
}
