package com.lmag.gtd.util;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;

public class Utils {
	
	public static Image getImageFromPath(String path) {
		
		try {
			return new Image("res/"+path);
		} catch (SlickException e) {
			try {
				return new Image("res/error.png");
			} 
			catch (SlickException e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static float getAngle(Vector2f pos, Vector2f target) {
		float angle = (float)Math.toDegrees(Math.atan2(target.getY()-pos.getY(), target.getX()-pos.getX()));
		if(angle < 0) angle += 360;
		if(angle > 360) angle -= 360;
		return angle;
	}
	
	public static ArrayList<Entity> sortByType(ArrayList<Entity> in, String type) {
		
		ArrayList<Entity> newArray = new ArrayList<Entity>();
		for(int i=0;i<in.size();i++) {
			System.out.println(in.get(i).getType()+":"+type);
			if(in.get(i).getType().contains(type)) {
				System.out.println(i);
				newArray.add(in.get(i));
			}
		}
		
		return newArray;
	}
	
	public static float getDist(Vector2f a, Vector2f b) {
		return (float)Math.sqrt(
			(Math.pow(b.getY(), 2)-Math.pow(a.getY(), 2))+
			(Math.pow(b.getX(), 2)-Math.pow(a.getX(), 2))
		);
	}
	
	public static ArrayList<Entity> getNearestEntities(ArrayList<Entity> in, Vector2f pos, int maxDist, int entLimit) {
		
		ArrayList<Float> results = new ArrayList<Float>();
		ArrayList<Entity> entResults = new ArrayList<Entity>();
		
		for (Entity ent : in) {
			
			/*if (ent.getCopyOfChildren().size() > 0) {
				
				
			}*/
			
			float dist = getDist(pos, ent.getPos());
			
			if (dist >= maxDist) {
				
				continue;
			}
			
			
			if (results.isEmpty()) {
				
				results.add(dist);
				entResults.add(ent);
				
				continue;
			}
			
			
			for (float compare : results) {

				if (dist < compare) {
					
					if (results.size() <= entLimit) {
						
						results.add(results.indexOf(compare), dist);
						entResults.add(results.indexOf(compare), ent);
					}
					
					else {
						
						int index = results.indexOf(compare);
						
						results.remove(index);
						entResults.remove(index);
					
						results.add(index, dist);
						entResults.add(index, ent);
					}
					
					break;
				}
				
				if (results.indexOf(compare) == results.size() && results.size() <= entLimit) {
						
						results.add(results.indexOf(compare), dist);
						entResults.add(results.indexOf(compare), ent);
					}
				}
			}
		
		return entResults;
	}
	public static Entity getNearestEntity(ArrayList<Entity> in, Vector2f pos, int maxDist) {
		ArrayList<Entity> eArr = getNearestEntities(in, pos, maxDist, 1);
		if(eArr.isEmpty()) {
			return null;
		} else {
			return eArr.get(0);
		}
	}

}
