package com.lmag.gtd.entities;

import com.lmag.gtd.entities.enemies.EnemyEMP;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

import java.util.ArrayList;

public abstract class Enemy extends EntityLiving {

    // TODO: MAKE FACING DO THE THING BECAUSE ENEMIES DO A FANCY SHUFFLE ON PLACE :^)

	protected int t = 0;
	protected int currentSegment = 0;

	protected float endTime = 0;
	protected float speedMod = 0.05f;
	public int xXx_cashMonAy_dropped_xXx = 420;

    protected byte dir = 0;
	
	protected Image targetIcon;

    protected ArrayList<Vector2f> path = new ArrayList<Vector2f>();

	public Enemy(Vector2f position) {
		
		this("enemytst.png", position);
	}
	
	protected Enemy(String spritePath, Vector2f position) {
		super(spritePath, position);
		
		targetIcon = Utils.getImageFromPath("trgit.png");

		baseHealth = 30;
		health = getMaxHealth();
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if(this.isTarget>0) {
			g.drawImage(targetIcon, getX()-5, getY()-5);
		}
	}

    public void updatePath() {

        ArrayList<Vector2f> newPath = new ArrayList<Vector2f>();

        if (MainGame.instance.lc.heatmap == null || MainGame.instance.lc.heatmap.getWalls() == null) {

            return;
        }

        Vector2f currentPos = this.getPosOnGrid();

        byte facing = -1;

        int currentTemp = MainGame.instance.lc.heatmap.getTemp(currentPos);

        int count = MainGame.HEIGHT_IN_TILES * MainGame.WIDTH_IN_TILES;
        Vector2f lastPos = currentPos.copy();
        for (; currentPos != MainGame.instance.lc.the_holy_grail.getPosOnGrid() && count > 1; count--) {

            //Right
            if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x + 1, (int) currentPos.y) < currentTemp) {

                currentPos = new Vector2f(currentPos.x + 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 0) {

                    facing = 0;
                    newPath.add(lastPos.copy());
                }
            }
            //Up
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y + 1) < currentTemp) {

                currentPos = new Vector2f(currentPos.x, currentPos.y + 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 1) {

                    facing = 1;
                    newPath.add(lastPos.copy());
                }
            }
            //Left
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x - 1, (int) currentPos.y) < currentTemp) {

                currentPos = new Vector2f(currentPos.x - 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 2) {

                    facing = 2;
                    newPath.add(lastPos.copy());
                }
            }
            //Down
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y - 1) < currentTemp) {

                currentPos = new Vector2f(currentPos.x, currentPos.y - 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 3) {

                    facing = 3;
                    newPath.add(lastPos.copy());
                }
            }

            lastPos = currentPos.copy();
        }

        newPath.add(MainGame.instance.lc.the_holy_grail.getPosOnGrid());

        path = new ArrayList<Vector2f>();
        path.add(getPos());

        for(int i=0;i<newPath.size();i++) {

            //debug
            if (this instanceof EnemyEMP) {

                Vector2f debug = newPath.get(i);

                System.out.println("X: " + debug.getX() + "Y: " + debug.getY());
            }

            path.add(newPath.get(i).copy().scale(MainGame.GRID_SIZE));
        }
        endTime = Utils.getDist(path.get(0), path.get(1)) / speedMod;
        currentSegment = 0;
        t = 0;
    }
	
	@Override
	public void update(int dt) {

		if(path == null) {

            return;
        }
		
		targetIcon.rotate(.1f*dt);
		
		t += dt;
		
		if (t > endTime) {
			
			currentSegment++;
			t = 0;

            ///////////////////////////////////////////////////////////////////
            //////////////////////// WARNING: BAD CODE ////////////////////////
            ///////////////////////////////////////////////////////////////////
            //   DO NOT EVER EVER EVER TOUCH THIS IT MAKES NO SENSE I KNOW   //
            //  BUT WE ARE INCOMPETENT PROGRAMMERS, WE DO NOT UNDERSTAND WHY //
            //     THIS ALGORITHM DO LIKE IT DO BUT IT DOES SO WE LET IT.    //
            ///////////////////////////////////////////////////////////////////
			if (currentSegment >= path.size() - 2) {
				
				currentSegment = 0;
                endTime = Utils.getDist(path.get(currentSegment), path.get(currentSegment + 1)) / speedMod;
                return;
            }
			
			endTime = Utils.getDist(path.get(currentSegment), path.get(currentSegment + 1)) / speedMod;
		}
		
		Vector2f a = path.get(currentSegment);
		Vector2f b = path.get(currentSegment + 1);
		
		float stp = t/endTime;

        Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		);


		setPos(np);
	}


    public byte getDir() {

        return dir;
    }

    public void setDir(int direction) {

        setDir((byte) direction);
    }

    public void setDir(byte direction) {

        if (direction < 0 || direction > 3) {

            return;
        }

        dir = direction;
    }
	
	@Override
	protected void onDeath() {
		
		MainGame.currency += xXx_cashMonAy_dropped_xXx;
	}
}
