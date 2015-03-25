package com.lmag.gtd.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
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
	
	
	/**
	 * 
	 * @param in The list of entities to search
	 * @param pos The position to search from
	 * @param maxDist Search range
	 * @param entLimit Set to -1 for all entities in range
	 * @return
	 */
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
				
				if (results.indexOf(compare) == results.size() && (results.size() <= entLimit || entLimit == -1)) {
						
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
	
	
	@SuppressWarnings("unchecked")
	public static Object[] loadLevel(String name) {
		
		//An array of arrays (First enemy paths,
		// second array enemy types.
		Object[] result = {new ArrayList<String[]>(), null};
		
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
	        
	        String enemyTypes, pathData;
	        
	        enemyTypes = everything.split("#")[0];
	        pathData = everything.split("#")[1];
	        
	        
	        for (String typeStr : enemyTypes.split(":")) {
	        	
	        	((ArrayList<String[]>)result[0]).add(typeStr.split(","));
	        }
	        
	        for (String coords : pathData.split(":")) {

	        	String x = coords.split(",")[0];
	        	String y = coords.split(",")[1];
	        	
	        	path.add(new Vector2f(Float.parseFloat(x), Float.parseFloat(y)));
	        }
	        
	        result[1] = path.toArray(new Vector2f[path.size()]);
	        
	        return result;
	        
	    } catch(Exception e) {e.printStackTrace();}
	    
	    return null;
	}
	
	
	public static Vector2f snapToGrid(Vector2f in) {
		
		float x = (float) (Math.floor(in.x/MainGame.GRID_SIZE));
		float y = (float) (Math.floor(in.y/MainGame.GRID_SIZE));
		
		if (in.x >= MainGame.WIDTH - MainGame.GRID_SIZE) {
			x--;
		}
		
		if (y >= Math.floor(MainGame.HEIGHT/MainGame.GRID_SIZE) - 1) {
			y--;
		}
		if (y >= Math.floor(MainGame.HEIGHT/MainGame.GRID_SIZE) - 1) {
			y--;
		}
		
		return new Vector2f(
				(float) (MainGame.GRID_SIZE * x),
				(float) (MainGame.GRID_SIZE * y)
		);
	}
	/**
	 * 
	 * @param lp1 Line point 1
	 * @param lp2 Line point 2
	 * @param tp  Test point
	 * @return Closest point on line to tp
	 */
	public static Vector2f GetClosestPoint(Vector2f lp1, Vector2f lp2, Vector2f tp) {
		
		double sx1 = lp1.x, sy1 = lp1.y, sx2 = lp2.x, sy2 = lp2.y, px = tp.x, py = tp.y;
		double xDelta = sx2 - sx1;
	    double yDelta = sy2 - sy1;

	    if ((xDelta == 0) && (yDelta == 0))
	    {
	      throw new IllegalArgumentException("Segment start equals segment end");
	    }

	    double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

	    final Vector2f closestPoint;
	    if (u < 0)
	    {
	      closestPoint = new Vector2f((float)sx1, (float)sy1);
	    }
	    else if (u > 1)
	    {
	      closestPoint = new Vector2f((float)sx2, (float)sy2);
	    }
	    else
	    {
	      closestPoint = new Vector2f((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
	    }

	    return closestPoint;
	}
}
