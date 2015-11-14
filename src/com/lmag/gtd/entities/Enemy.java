package com.lmag.gtd.entities;

import com.lmag.gtd.entities.enemies.EnemyEMP;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;
import sun.applet.Main;

import java.util.ArrayList;

public abstract class Enemy extends EntityLiving {

    // TODO: MAKE FACING DO THE THING BECAUSE ENEMIES DO A FANCY SHUFFLE ON PLACE :^)

	protected int t = 0;
	protected int currentSegment = 0;

    byte currentFacing = -1;

	protected float endTime = 0;
	protected float speedMod = 0.05f;
	public int xXx_cashMonAy_dropped_xXx = 420;

    protected byte dir = 0;
	
	protected Image targetIcon;

    protected ArrayList<Vector2f> path = new ArrayList<Vector2f>();

    //debug
    public ArrayList<Integer> heatList = new ArrayList<Integer>();

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
		
		if (this.isTarget>0) {

			g.drawImage(targetIcon, getX()-5, getY()-5);
		}

        if (MainGame.instance.debug && this instanceof EnemyEMP) {

            int counter = -2;
            for (Vector2f node : path) {

                counter++;

                g.setColor(Color.red);
                g.drawRect(node.x, node.y, 32, 32);

                if (counter < heatList.size() && counter > -1) {
                    MainGame.badFont.render((int) node.x + 16, (int) node.y + 16, g,
                            heatList.get(counter).toString());
                }
            }
        }
	}

    private char[] ohno = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public void get_thing() {
        for(int y=0;y<MainGame.HEIGHT_IN_TILES;y++) {
            String youre_a_disgusting_weeaboo = "";
            for(int x=0;x<MainGame.WIDTH_IN_TILES;x++) {
                int t = MainGame.instance.lc.heatmap.getTemp(x,y);
                char r = '#';
                if(t != Integer.MAX_VALUE) r = ohno[MainGame.instance.lc.heatmap.getTemp(x,y)];
                youre_a_disgusting_weeaboo += r;
            }
            System.err.println(youre_a_disgusting_weeaboo);
        }
    }
    public void updatePath() {

        //debug
        heatList.clear();

        ArrayList<Vector2f> newPath = new ArrayList<Vector2f>();

        if (MainGame.instance.lc.heatmap == null || MainGame.instance.lc.heatmap.getWalls() == null) {

            return;
        }

        byte facing = -1;

        byte the_first_value_of_facingStored_in_byte_FORMAT = -1;

        Vector2f currentPos = this.getPos();
        Vector2f currentPosOnGrid = this.getPosOnGrid();
        if (currentPos.x % (float)MainGame.GRID_SIZE != 0 && currentFacing==0) {
            currentPos = new Vector2f(currentPosOnGrid.x+1, currentPosOnGrid.y);
        } else if (currentPos.y % (float)MainGame.GRID_SIZE != 0 && currentFacing==3) {
            currentPos = new Vector2f(currentPosOnGrid.x, currentPosOnGrid.y+1);
        } else {
            currentPos = currentPosOnGrid;
        }


        Vector2f lastPos = currentPos.copy();

        int currentTemp = MainGame.instance.lc.heatmap.getTemp(currentPos);

        int count = MainGame.HEIGHT_IN_TILES * MainGame.WIDTH_IN_TILES;
        for (; (currentPos.x != MainGame.instance.lc.the_holy_grail.getPosOnGrid().x
                || currentPos.y != MainGame.instance.lc.the_holy_grail.getPosOnGrid().y)
                && count > 1; count--) {
            // TODO: This should go away.  Really.
            int dbg_right  = MainGame.instance.lc.heatmap.getTemp((int) currentPos.x + 1, (int) currentPos.y);
            int dbg_left   = MainGame.instance.lc.heatmap.getTemp((int) currentPos.x - 1, (int) currentPos.y);
            int dbg_top    = MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y + 1);
            int dbg_bottom = MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y - 1);

            //Right
            if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x + 1, (int) currentPos.y) < currentTemp) {

                currentPos = new Vector2f(currentPos.x + 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 0) {

                    facing = 0;
                    newPath.add(lastPos.copy());

                    heatList.add(currentTemp);
                }
            }
            //Up
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y + 1) < currentTemp) {

                currentPos = new Vector2f(currentPos.x, currentPos.y + 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 1) {

                    facing = 1;
                    newPath.add(lastPos.copy());

                    heatList.add(currentTemp);
                }
            }
            //Left
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x - 1, (int) currentPos.y) < currentTemp) {

                currentPos = new Vector2f(currentPos.x - 1, currentPos.y);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 2) {

                    facing = 2;
                    newPath.add(lastPos.copy());

                    heatList.add(currentTemp);
                }
            }
            //Down
            else if (MainGame.instance.lc.heatmap.getTemp((int) currentPos.x, (int) currentPos.y - 1) < currentTemp) {

                currentPos = new Vector2f(currentPos.x, currentPos.y - 1);
                currentTemp = MainGame.instance.lc.heatmap.getTemp((int)currentPos.x, (int)currentPos.y);

                if (facing != 3) {

                    facing = 3;
                    newPath.add(lastPos.copy());

                    heatList.add(currentTemp);
                }
            }

            //MainGame.

            System.out.println("X: " + lastPos.x + "Y: " + lastPos.y);

            if(the_first_value_of_facingStored_in_byte_FORMAT==-1) {

                the_first_value_of_facingStored_in_byte_FORMAT = facing;
            }

            lastPos = currentPos.copy();
        }

        newPath.add(MainGame.instance.lc.the_holy_grail.getPosOnGrid());

        path = new ArrayList<Vector2f>();
        path.add(getPos());

        for(int i=0;i<newPath.size();i++) {

            if(currentFacing==the_first_value_of_facingStored_in_byte_FORMAT&&i==0) {

                continue;
            }

            /* Code over here --> */ path.add(newPath.get(i).copy().scale(MainGame.GRID_SIZE));
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

        //Right
        if (a.x < b.x) {

           currentFacing = 0;
        }

        //Up
        else if (a.y < b.y) {

            currentFacing = 1;
        }

        //Left
        else if (a.x > b.x) {

            currentFacing = 2;
        }

        //Down
        else if (a.y > b.y) {

            currentFacing = 3;
        }
		
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
