package com.lmag.gtd.entities;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.util.Utils;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Heatmap {

    Entity end;
    boolean[][] walls;

    int[][] heatmap;

    public Heatmap(Entity end) {
        this.end = end;
        heatmap = new int[MainGame.WIDTH_IN_TILES][MainGame.HEIGHT_IN_TILES];
        this.walls = new boolean[MainGame.WIDTH_IN_TILES][MainGame.HEIGHT_IN_TILES];
        update_heatmap();
    }

    protected Boolean outside(int x, int y) {
        //System.out.println("X: "+x+" MAX: "+MainGame.WIDTH_IN_TILES);
        return x<0||x>=MainGame.WIDTH_IN_TILES||y<0||y>=MainGame.HEIGHT_IN_TILES;
    }

    protected void emit(int x, int y, int new_value) {
        //System.out.println(x+" "+y);
        //if(outside(x,y)) System.out.println("outside"); else System.out.println(outside(x,y) + " " + walls[x][y] + " " + heatmap[x][y] +  "<" + offset + " " + offset);
        if(outside(x,y) || walls[x][y] || heatmap[x][y]<=new_value) {

            return;
        }


        heatmap[x][y] = new_value;
        emit(x+1,y,new_value+1);
        emit(x-1,y,new_value+1);
        emit(x,y+1,new_value+1);
        emit(x,y-1,new_value+1);
    }

    protected void update_walls() {
        ArrayList<Entity> new_walls = Utils.sortByType(Utils.getNearestEntities(MainGame.instance.root.getCopyOfChildren(), new Vector2f(0,0), Integer.MAX_VALUE, -1), "Tower||Wall");
        for(int x=0;x<walls.length;x++) {
            for(int y=0;y<walls[x].length;y++) {
                walls[x][y]=false;
            }
        }
        for(int i=0;i<new_walls.size();i++) {
            walls[new_walls.get(i).getTileX()][new_walls.get(i).getTileY()] = true;
        }
    }

    public void update_heatmap() {
        update_walls();
        for(int x=0;x<heatmap.length;x++) {
            for(int y=0;y<heatmap[x].length;y++) {
                heatmap[x][y] = Integer.MAX_VALUE;
            }
        }
        emit(end.getTileX(),end.getTileY(),0);
    }

    public int[][] get_heatmap() {
        return heatmap;
    }

    public void render_heatmap(Graphics g) {
        float highest_value = 0;
        for(int x=0;x<heatmap.length;x++) {
            for(int y=0;y<heatmap[x].length;y++) {
                if(heatmap[x][y]>highest_value && heatmap[x][y]!=Integer.MAX_VALUE) {
                    highest_value = heatmap[x][y];
                }
            }
        }

        for(int x=0;x<heatmap.length;x++) {
            for(int y=0;y<heatmap[x].length;y++) {
                //System.out.println(heatmap[x][y]);
                float value = 1f-(((float)heatmap[x][y])/highest_value);
                //float value = 255 - heatmap[x][y];
                g.setColor(new org.newdawn.slick.Color(value,value,value));
                g.fillRect(x*MainGame.GRID_SIZE,y*MainGame.GRID_SIZE,MainGame.GRID_SIZE,MainGame.GRID_SIZE);
                if(heatmap[x][y]!=Integer.MAX_VALUE)MainGame.badFont.render(x*MainGame.GRID_SIZE,y*MainGame.GRID_SIZE,g,heatmap[x][y]+"");
            }
        }
    }

    public boolean[][] getWalls() {
        return walls;
    }


    /**
     *
     * @param pos divides by GRID_SIZE for compatibility with Entity pos
     */
    public int getTemp(Vector2f pos) {

        return outside((int)(pos.x/MainGame.GRID_SIZE),(int)(pos.y/MainGame.GRID_SIZE))?Integer.MAX_VALUE:heatmap[(int)(pos.x/MainGame.GRID_SIZE)][(int)(pos.y/MainGame.GRID_SIZE)];
    }


    public int getTemp(int x, int y) {

        return outside(x,y)?Integer.MAX_VALUE:heatmap[x][y];
    }

    public Entity getEnd() {
        return end;
    }
}
