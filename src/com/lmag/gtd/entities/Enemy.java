package com.lmag.gtd.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;

import java.util.ArrayList;

public abstract class Enemy extends EntityLiving {

	protected int t = 0;
	protected int currentSegment = 0;

	protected float endTime = 0;
	protected float speedMod = 0.005f;
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
        int currentTemp = MainGame.instance.lc.heatmap.getTemp(MainGame.instance.lc.the_powerhouse_of_the_map.getPos());

        int count = MainGame.HEIGHT_IN_TILES * MainGame.WIDTH_IN_TILES;

        for (; currentPos != MainGame.instance.lc.the_holy_grail.getPosOnGrid() && count > 1; count--) {

            //Right
            if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x + 1, (int) currentPos.y) < currentTemp) {

                //debug
                System.out.println("CurrentPos: " + currentPos + "\nnewPos: \nx: " + currentPos.x + "y: " + (currentPos.y - 1));

                currentPos = new Vector2f(currentPos.x + 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                //temp
                newPath.add(currentPos);

                /*if (facing != 0) {

                    facing = 0;
                    newPath.add(currentPos.copy());
                }*/
            }
            //Up
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y + 1) < currentTemp) {

                //debug
                System.out.println("CurrentPos: " + currentPos + "\nnewPos: \nx: " + currentPos.x + "y: " + (currentPos.y - 1));

                currentPos = new Vector2f(currentPos.x, currentPos.y + 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                //temp
                newPath.add(currentPos);

                /*if (facing != 1) {

                    facing = 1;
                    newPath.add(currentPos.copy());
                }*/
            }
            //Left
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x - 1, (int) currentPos.y) < currentTemp) {

                //debug
                System.out.println("CurrentPos: " + currentPos + "\nnewPos: \nx: " + currentPos.x + "y: " + (currentPos.y - 1));

                currentPos = new Vector2f(currentPos.x - 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                //temp
                newPath.add(currentPos);

                /*if (facing != 2) {

                    facing = 2;
                    newPath.add(currentPos.copy());
                }*/
            }
            //Down
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y - 1) < currentTemp) {

                //debug
                System.out.println("CurrentPos: " + currentPos + "\nnewPos: \nx: " + currentPos.x + "y: " + (currentPos.y - 1));

                currentPos = new Vector2f(currentPos.x, currentPos.y - 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                //temp
                newPath.add(currentPos);

                /*if (facing != 3) {

                    facing = 3;
                    newPath.add(currentPos.copy());
                }*/
            }
        }

        newPath.add(MainGame.instance.lc.the_holy_grail.getPosOnGrid());

        path = (ArrayList<Vector2f>)newPath.clone();
        endTime = Utils.getDist(path.get(0), path.get(1)) / speedMod;
        currentSegment = 0;
        t = 0;
    }
	
	@Override
	public void update(int dt) {

		if(path == null) {

            return;
        }

        //debug
        for (Vector2f p : path) {

            System.out.println(p);
        }
		
		targetIcon.rotate(.1f*dt);
		
		t += dt;
		
		if (t > endTime) {
			
			currentSegment++;
			t = 0;
			
			if (currentSegment >= path.size() - 1) {
				
				currentSegment = 0;
			}
			
			endTime = Utils.getDist(path.get(currentSegment), path.get(currentSegment + 1)) / speedMod;

            //debug
            System.out.println("path1: " + path.get(currentSegment) + " \npath2: " + path.get(currentSegment + 1));
		}
		
		Vector2f a = path.get(currentSegment);
		Vector2f b = path.get(currentSegment + 1);


		
		float stp = t/endTime;

		Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		).scale(MainGame.GRID_SIZE);

        //debug
		System.out.println("np: " + np);
        System.out.println("t: " + t);


		setPos(np);

		/*

		if ((int)Math.floor(t/percentJump)>=path.length-1) {

			t=1;
		}

		int slot = (int)Math.floor(t / percentJump);

		Vector2f a = path[slot];
		Vector2f b = path[slot + 1];

		float stp = t - (slot) * percentJump;

		stp *= (100 / percentJump) * 0.01f;

		Vector2f np = new Vector2f(
				(a.x*(1-stp))+(b.x*(stp)),
				(a.y*(1-stp))+(b.y*(stp))
		);

		setPos(np);

		*/
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
