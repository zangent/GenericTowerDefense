package com.lmag.gtd.util;

import java.io.BufferedReader;
import java.io.FileReader;
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
			if(in.get(i).getType().contains(type)) {
				newArray.add(in.get(i));
			}
		}
		
		return newArray;
	}
	
	public static float getDist(Vector2f a, Vector2f b) {
		return (float)Math.sqrt(
			(Math.pow(b.getY()-a.getY(), 2))+
			(Math.pow(b.getX()-a.getX(), 2))
		);
	}
	
	public static ArrayList<Entity> getNearestEntities(ArrayList<Entity> in, Vector2f pos, int maxDist, int entLimit) {
		
		ArrayList<Float> results = new ArrayList<Float>();
		ArrayList<Entity> entResults = new ArrayList<Entity>();
		
		for (Entity ent : in) {
			
			/*if (ent.getCopyOfChildren().size() > 0) {
				
				
			}*/
			
			float dist = getDist(pos, ent.getCenterPos());
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

					int index = results.indexOf(compare);
				
					results.add(index, dist);
					entResults.add(index, ent);
					
					if (results.size() > entLimit) {
						
						results.remove(entLimit);
						entResults.remove(entLimit);
					}
					
					break;
				}
				
				if (results.indexOf(compare) == results.size() && results.size() <= entLimit) {
						
						results.add(results.indexOf(compare), dist);
						entResults.add(results.indexOf(compare), ent);
					}
				}
			}
		
		return flipEntArray(entResults);
	}
	public static ArrayList<Entity> flipEntArray(ArrayList<Entity> in) {
		ArrayList<Entity> n = new ArrayList<Entity>();
		for(int i=in.size()-1;i>-1;i--) {
			n.add(in.get(i));
		}
		return n;
	}
	public static Entity getNearestEntity(ArrayList<Entity> in, Vector2f pos, int maxDist) {
		ArrayList<Entity> eArr = getNearestEntities(in, pos, maxDist, 1);
		if(eArr.isEmpty()) {
			return null;
		} else {
			return eArr.get(0);
		}
	}
	public static Vector2f[] getLevelPath(String name) {
		ArrayList<Vector2f> path = new ArrayList<Vector2f>();
		
	    try{
	    	BufferedReader br = new BufferedReader(new FileReader("res/"+name));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        
	        br.close();
	        
	        String everything = sb.toString();
	        
	        for (String coords : everything.split(":")) {

	        	String x = coords.split(",")[0];
	        	String y = coords.split(",")[1];
	        	
	        	path.add(new Vector2f(Float.parseFloat(x), Float.parseFloat(y)));
	        }
	        
	        return path.toArray(new Vector2f[path.size() - 1]);
	        
	    } catch(Exception e) {e.printStackTrace();}
	    
	    return new Vector2f[]{};
	}
}
