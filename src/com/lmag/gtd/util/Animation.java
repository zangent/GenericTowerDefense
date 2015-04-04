package com.lmag.gtd.util;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;

public class Animation extends Entity {
	
	int m_frameWidth;
	int m_frameHeight;
	Image m_sprite;
	int m_msPerFrame;
	int m_frameX = 0, m_frameY = 0;
	int m_accumTicks = 0;
	boolean m_loop;
	boolean m_dead = false;
	
	public Animation(String path, Vector2f pos, int frameWidth, int frameHeight, int msPerFrame, boolean loop) {
		super("", pos);
		m_sprite = Utils.getImageFromPath(path);
		m_frameWidth = frameWidth;
		m_frameHeight = frameHeight;
		m_msPerFrame = msPerFrame;
		m_loop = loop;
	}
	@Override
	public void update(int dt) {
		if(m_dead) return;

		m_accumTicks += dt;
		if(m_accumTicks >= m_msPerFrame) {
			m_frameX++;
			System.out.println("upd");
			if(m_frameX*m_frameWidth>=m_sprite.getWidth()) {
				m_frameX=0;
				m_frameY++;
				if(m_frameY*m_frameHeight>=m_sprite.getHeight()) {
					if(m_loop) {
						m_frameX=0;
						m_frameY=0;
					} else m_dead=true;
				}
			}
			m_accumTicks = 0;
		}
	}
	@Override
	public void render(Graphics g) {
		
		if(!m_dead) {
			g.drawImage(m_sprite, getX(), getY(), getX()+m_frameWidth, getY()+m_frameHeight, m_frameX*m_frameWidth, m_frameY*m_frameHeight, m_frameX*m_frameWidth+m_frameWidth, m_frameHeight);
		}
	}
	public boolean isDead(){return m_dead;}
}
