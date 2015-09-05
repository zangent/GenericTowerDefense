package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

import static org.lwjgl.opengl.GL11.*;

public class MouseTracker extends Entity {

	private Entity child;
	Vector2f lastPoint;
	int price=0;
	
	public MouseTracker(Entity e) {
		this(e, 0);
	}
	
	public MouseTracker(Entity e, int fisher_price) {
		super("", new Vector2f(0,0));
		
		if (e.isUpdating()) {
			
			e.setUpdating(false);
		}
		
		child = e;
		this.addChild(child);
		addAsInputListener();
		lastPoint = MainGame.instance.getMousePos();
		price = fisher_price;
	}
	
	public void tick(int dt) {

	}
	
	public boolean updatePos(Graphics g) {
		Vector2f tp = (Utils.snapToGrid(MainGame.instance.getMousePos()));
		boolean good = true;
        /*
		Vector2f[] pth = MainGame.instance.lc.getPath();
		for(int i=0;i<pth.length-1;i++) {
			Vector2f a = pth[i];
			Vector2f b = pth[i+1];
			if(Utils.getDist(tp, Utils.GetClosestPoint(a, b, tp)) <= 50) {
				if(g != null) {
					g.setLineWidth(5);
					g.setColor(Color.blue);
					g.drawLine(a.x, a.y, b.x, b.y);
					g.drawLine(Utils.GetClosestPoint(a, b, tp).x, Utils.GetClosestPoint(a, b, tp).y, tp.x, tp.y);
				}
				good = false;
				break;
			}
		}
		*/

		ArrayList<Entity> neighbors = Utils.getNearestEntities(Utils.sortByType(
                MainGame.instance.root.getCopyOfChildren(), "Tower||Wall"),
                this.getPos(), MainGame.TOWER_SIZE*5, 100);

		Rectangle new_bb = new Rectangle(tp.getX(), tp.getY(), child.getWidth(), child.getHeight());
		for(Entity e:neighbors) {
			
			if(new_bb.intersects(e.get_bb())) {
				good = false;
                break;
			}
			
		}
		
		if(good) {
			lastPoint = tp;
		}
		return good;
	}
	
	public void render(Graphics g) {
		
		if(!updatePos(g)) {
			this.setPos(lastPoint);
			//child.render(g);
			glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
			child.sprite.setImageColor(20f, 0, 0, 1f);
			child.render(g);
			glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			//child.sprite.setImageColor(1f, 1f, 1f, .1f);
			//child.render(g);
			child.sprite.setImageColor(1f, 1f, 1f, 1f);
		} else {
			if(child instanceof TowerMachineGun) {
				g.setColor(new Color(0.7f, 1f, 0.7f, 0.5f));
				Vector2f cp = child.getCenterPos();
				g.fillOval(cp.x-((TowerMachineGun)child).range, cp.y-((TowerMachineGun)child).range, ((TowerMachineGun)child).range*2, ((TowerMachineGun)child).range*2);
			}
			this.setPos(lastPoint);
			child.render(g);
		}
	}
	public void onMousePressed(int btn, int x, int y) {
		if(btn == 0) {
			if(updatePos(null) && MainGame.currency >= price) {
				this.removeChild(child);
				MainGame.instance.root.addChild(child);
				child.setPos(lastPoint);
				MainGame.currency -= price;
				child.addStatEffect(StatEffects.Constructing((Tower)child, 5000));
				child.setUpdating(true);

				kill();
			}
		} else if(btn == 1) {
			kill();
		}
	}
}
