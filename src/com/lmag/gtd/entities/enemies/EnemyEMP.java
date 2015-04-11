package com.lmag.gtd.entities.enemies;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
import com.lmag.gtd.entities.Enemy;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.StatEffect;
import com.lmag.gtd.util.Utils;

public class EnemyEMP extends Enemy {
	
	
	public int EMPCooldown = 0, sinceLastEMP = EMPCooldown;
	public float EMPAlpha = 0, EMPAlphaDecay = 0.8f;

	public EnemyEMP(Vector2f position) {
		super("enemya.png", position);
		resetEMPCooldown();
		//TODO: Do we want this?
		sinceLastEMP = EMPCooldown;
	}
	
	
	private void resetEMPCooldown() {
		
		EMPCooldown = (int) (2500 + 2500d * Math.random());
		
		//TODO debug
		System.out.println(EMPCooldown);
		
		range = 150;
	}
	
	
	@Override
	public void update(int dt) {
		super.update(dt);
		
		sinceLastEMP+=dt;
		
		if (sinceLastEMP >= EMPCooldown) {
			
			//TODO debug
			System.out.println("Cooldown is over!");
		
			ArrayList<Entity> targets = Utils.getNearestEntities(Utils.sortByType(MainGAme.instance.root.getCopyOfChildren(), "Tower"), this.getPos(), range, -1);
			
			if (targets != null) {
				
				//TODO debug
				System.out.println("Found targets! Targets: " + targets.size());
				
				for (Entity target : targets) {
					
					target.addStatEffect(StatEffect.EMP);
					EMPAlpha = 0.5f;
				}
				
				resetEMPCooldown();
				
				sinceLastEMP = 0;
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		
		g.setColor(new Color(.4f, .4f, 1f, EMPAlpha));
		EMPAlpha *= EMPAlphaDecay;
		g.fillRect(0, 0, MainGAme.WIDTH, MainGAme.HEIGHT);
		
		
		
		super.render(g);

		if (MainGAme.instance.debug) {
			
			MainGAme.badFont.render((int)this.getX(), (int)this.getY() - 8, g, " "+String.valueOf(sinceLastEMP)+" ");
		}
	}
}
