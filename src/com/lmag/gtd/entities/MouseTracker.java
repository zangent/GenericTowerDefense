package com.lmag.gtd.entities;

import java.util.ArrayList;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGAme;
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
		child = e;
		this.addChild(child);
		addAsInputListener();
		lastPoint = MainGAme.instance.getMousePos();
		price = fisher_price;
	}
	
	public void tick(int dt) {

	}
	
	public boolean updatePos(Graphics g) {
		Vector2f tp = (Utils.snapToGrid(MainGAme.instance.getMousePos()));
		boolean good = true;
		Vector2f[] pth = MainGAme.instance.lc.getPath();
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

		//TODO: There is a clipping issue here.
		// If you place towers like this:
		// 				  X
		//
		//				    X
		// and try to place a tower between them, you
		// can clip into the bottom-right tower's top-left corner.

		ArrayList<Entity> neighbors = Utils.getNearestEntities(Utils.sortByType(MainGAme.instance.root.getCopyOfChildren(), "Tower"), this.getPos(), MainGAme.GRID_SIZE*4, -1);
		
		for(Entity e:neighbors) {
			
			int mx = (int)tp.x, my = (int)tp.y, mw = child.getWidth(), mh = child.getHeight();
			Vector2f op = Utils.snapToGrid(e.getPos());
			int ox = (int)op.x, oy = (int)op.y, ow = e.getWidth(), oh = e.getHeight();
			if(
					//(mx+mw>ox||ox+ow<=mx) ||
					//(my+mh<=oy||oy+oh<=my)
					((mx<=ox&&mx+mw>ox)||
					(ox<=mx&&ox+ow>mx))&&(
					(my<=oy&&my+mh>oy)||
					(oy<=my&&oy+oh>my))
			   ) {
				good = false;
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
	public void mousePressed(int btn, int x, int y) {
		if(btn == 0) {
			if(updatePos(null) && MainGAme.currency >= price) {
				MainGAme.instance.root.addChild(child);
				child.setPos(lastPoint);
				MainGAme.currency -= price;
				kill();
			}
		} else if(btn == 1) {
			kill();
		}
	}
}
