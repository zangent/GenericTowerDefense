package com.lmag.gtd.entities;

import com.lmag.gtd.util.Animation;
import org.newdawn.slick.geom.Vector2f;

public class StatEffects {

	public static StatEffect Constructing(final EntityLiving parent, final int duration) {

		return new StatEffect(parent, duration) {

			public final String internalName = "Constructing";
			public final String displayName = "Constructing...";

			public float regen = 0;

			public float defaultUpdateRatePercMod = parent.updateRatePercMod;

            public int initialDuration = 1;

			@Override
			public void onAdded() {

				if (duration < 1) {

					duration = 1;
				}

                initialDuration = duration;

				((Tower)parent).targetingDisablers++;

				parent.health = 1;
			}

            @Override
            public void onUpdate(int dt) {
                float new_health = (1-((float) duration / (float) initialDuration));
                if(new_health>1) new_health=1;
                if(new_health<0.002f) new_health=0.002f;
                parent.setHealth(new_health * parent.getMaxHealth());
            }


			@Override
			public void onRemoved() {

				((Tower)parent).targetingDisablers--;

                parent.setHealth(parent.getMaxHealth());
			}
		};
	};

	public static StatEffect EMP(final EntityLiving parent, final int duration){

		return new StatEffect(parent, duration) {

			public final String internalName = "EMP";
			public final String displayName = "EMP";

			public float updateRateMod = 0f;

			public float defaultUpdateRatePercMod = parent.updateRatePercMod;

            private Animation buzz_aldrin = new com.lmag.gtd.util.Animation("spark.png", new Vector2f(0,0), 32, 32, 50, true);

			@Override public void onAdded() {

				if (duration < 1) {

					duration = 1;
				}

				((Tower)parent).target = null;

				((Tower)parent).targetingDisablers++;

				duration = (int) (1500 + (Math.random()*1000f));

				parent.addChild(buzz_aldrin);
			}

			@Override
			public void onRemoved() {

				((Tower)parent).targetingDisablers--;

                parent.removeChild(buzz_aldrin);
			}
		};
	};


	public StatEffects() {


	}

}
