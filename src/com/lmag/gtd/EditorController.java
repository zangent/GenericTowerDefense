package com.lmag.gtd;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class EditorController extends LegacyLevelController {
	
	ArrayList<Vector2f> pathPoints = new ArrayList<Vector2f>();
	
	public EditorController() {
		super();
	}	
	
	@Override
	public void update(int dt) {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			pathPoints.get(pathPoints.size()-1).add(new Vector2f(0, -1));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			pathPoints.get(pathPoints.size()-1).add(new Vector2f(-1, 0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			pathPoints.get(pathPoints.size()-1).add(new Vector2f(1, 0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
			pathPoints.get(pathPoints.size()-1).add(new Vector2f(0, 1));
		}
		
		super.update(dt);
	}

	@Override
	public void onMouseReleased(int btn, int x, int y) {
		
		if(btn==1) {
			pathPoints.add(MainGame.instance.getMousePos());
		}
	}
	
	@Override
	public void onKeyReleased(int kc, char ch) {
		
		if(kc==Keyboard.KEY_Z) {
			
			pathPoints.remove(pathPoints.size()-1);
			
		} else if(kc==Keyboard.KEY_X) {
			
			for(int i=0;i<pathPoints.size();i++) {
				System.err.print(pathPoints.get(i).x+","+pathPoints.get(i).y);
				if(i<pathPoints.size()-1) {
					System.err.print(":");
				}
			}
			System.err.println("");
			
		}
	}
	
	@Override
	public void render(Graphics g) {
		
		super.render(g);
		
		g.setColor(Color.orange);
		
		for(int i=0;i<pathPoints.size();i++) {
			
			Vector2f a = pathPoints.get(i);
			
			g.drawRect(a.x, a.y, 50, 50);

			if(i<pathPoints.size()-1) {
				Vector2f b = pathPoints.get(i+1);
				g.drawLine(a.x+25, a.y+25, b.x+25, b.y+25);
			}
		}
	}
}
