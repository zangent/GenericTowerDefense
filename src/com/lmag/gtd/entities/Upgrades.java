package com.lmag.gtd.entities;

public class Upgrades {

	public static Upgrade UBERDamage(Tower Parent) {
		
		class UBER extends Upgrade {
			
			public UBER(Tower parent) {
				super(parent);
				
				iconName = "damage.png";
				
				name = "UBER damage upgrade";
			}
				
			public void onAdded() {
				
				//debug 
				System.out.println("\n\n\n\nworking\n\n\n");
					
				parent.damagePercMod += 100f;
			}
				
			public void onRemoved() {
					
				parent.damagePercMod -= 100f;
			}
		}
		
		return new UBER(Parent);
	}
}
