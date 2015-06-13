package com.lmag.gtd.entities;

public class Upgrades {

	public static Upgrade UBERDamage(Tower Parent) {
		
		class UBER extends Upgrade {
			
			public UBER(Tower parent) {
				super(parent);
				
				displayName = "UBER damage upgrade";
				
				internalName = "UBERDamage";
			}
				
			public void onAdded() {
					
				parent.damagePercMod += 100f;
			}
				
			public void onRemoved() {
					
				parent.damagePercMod -= 100f;
			}
		}
		
		return new UBER(Parent);
	}
}
