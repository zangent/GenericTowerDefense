package com.lmag.gtd.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

public class MouseTracker extends Entity {

	private Entity child;
	Vector2f lastPoint;
	
	public MouseTracker(Entity e) {
		super("", new Vector2f(0,0));
		child = e;
		this.addChild(child);
		addAsInputListener();
		lastPoint = MainGame.instance.getMousePos();
	}
	
	public void tick(int dt) {

	}
	public void render(Graphics g) {
		
		
		Vector2f tp = (Utils.snapToGrid(MainGame.instance.getMousePos()));
		boolean good = true;
		Vector2f[] pth = MainGame.instance.lc.getPath();
		for(int i=0;i<pth.length-1;i++) {
			Vector2f a = pth[i];
			Vector2f b = pth[i+1];
			if(Utils.getDist(tp, Utils.GetClosestPoint(a, b, tp)) <= 50) {
				g.setLineWidth(5);
				g.setColor(Color.blue);
				g.drawLine(a.x, a.y, b.x, b.y);
				g.drawLine(Utils.GetClosestPoint(a, b, tp).x, Utils.GetClosestPoint(a, b, tp).y, tp.x, tp.y);
				good = false;
				break;
			}
		}
		if(good) {
			lastPoint = tp;
		}
		
		
		
		this.setPos(lastPoint);
		child.render(g);
	}
	@Override
	public boolean isAcceptingInput() {
		return true;
	}
	public void mousePressed(int btn, int x, int y) {
		if(btn == 0) {
			MainGame.instance.root.addChild(child);
			child.setPos(lastPoint);
			kill();
		} else if(btn == 1) {
			kill();
		}
	}
}
