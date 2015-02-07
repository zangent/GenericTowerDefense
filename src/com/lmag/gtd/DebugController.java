package com.lmag.gtd;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.DebugEnemy;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.util.Utils;

public class DebugController extends Entity {

	boolean md0 = false;
	boolean md1 = false;
	boolean kdz = false;
	boolean kdx = false;
	
	ArrayList<Vector2f> pathPoints = new ArrayList<Vector2f>();
	
	Vector2f[] path;
	
	public DebugController() {
		super("", new Vector2f(0,0));
		
		this.setVisible(false);
		
		path = Utils.getLevelPath("maps/check.txt");
	}
	
	
	@Override
	public void update(int dt) {
		
		if(Mouse.isButtonDown(0)&&!md0) {
			
			MainGame.instance.root.addChild(new DebugEnemy(MainGame.instance.getMousePos()).setPath(path));
		}
		md0 = Mouse.isButtonDown(0);
		
		
		if(Mouse.isButtonDown(1)&&!md1) {
			
			pathPoints.add(MainGame.instance.getMousePos());
		}
		md1 = Mouse.isButtonDown(1);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)&&!kdz) {
			
			pathPoints.remove(pathPoints.size()-1);
		}
		kdz = Keyboard.isKeyDown(Keyboard.KEY_Z);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_X)&&!kdx) {
			
			for(int i=0;i<pathPoints.size();i++) {
				System.err.print(pathPoints.get(i).x+","+pathPoints.get(i).y);
				if(i<pathPoints.size()-1) {
					System.err.print(":");
				}
			}
			System.err.println("");
		}
		kdx = Keyboard.isKeyDown(Keyboard.KEY_X);
		
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
	}
	
	@Override
	public void render(Graphics g) {
		
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
