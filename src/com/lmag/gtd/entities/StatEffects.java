package com.lmag.gtd.entities;

import org.newdawn.slick.geom.Vector2f;

public class StatEffects {
	
	public static final String constructing = "Constructing...";
	public static final String EMP = "EMP";
	
	public static StatEffect Constructing(final EntityLiving parent, final int duration) {
		
		return new StatEffect(parent, duration) {
			
			public final String type = constructing;
			
			public float updateRateMod = 0f;
			
			public float percRegen = parent.maxHealth / duration;
		};
	};
	
	public static StatEffect EMP(final EntityLiving parent, final int duration){
		
		return new StatEffect(parent, duration) {

			public final String type = EMP;
			
			@Override public void onAdded() {
				
				parent.EMPTimer = 0;
				parent.EMPEndTime = (short) (1500 + (Math.random()*1000f));
				
				if(parent.EMPIndicator == null) {
					parent.addChild(parent.EMPIndicator = new com.lmag.gtd.util.Animation("spark.png", new Vector2f(0,0), 32, 32, 50, true));
				}
			}
			
			@Override public void onRemoved() {
				
				parent.removeChild(parent.EMPIndicator);
				parent.EMPIndicator = null;
			}
		};
	};


	public StatEffects() {
			
			
	}

}
