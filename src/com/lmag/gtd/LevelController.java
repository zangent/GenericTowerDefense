package com.lmag.gtd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.lmag.gtd.entities.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.menu.BuyMenu;
import com.lmag.gtd.entities.menu.EntityMenu;
import com.lmag.gtd.util.Utils;
import sun.applet.Main;

public class LevelController extends Entity {

    int lastUpdate = 0;
    int lifetime = 0;
    int waveCount = 0;
    int enemyCount = 0;

    // Self-documenting code...
    int currentMaxEnemyCountForThePresentWaveThatIsOccuring = 1;
    int theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave = 1000;
    int theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave = 0;

    ArrayList<int[]> waveData = new ArrayList<int[]>();
    ArrayList<String[]> enemyData = new ArrayList<String[]>();

    public BuyMenu buyMenuRight;
    public EntityMenu entityMenuRight;

    public Entity selectedEntity;

    public Heatmap heatmap;

    public Wall[] walls;

    @SuppressWarnings("unchecked")
    public LevelController() {
        super("", new Vector2f(0, 0));

        this.setVisible(false);

        buyMenuRight = new BuyMenu();

        addChild(buyMenuRight);


        Image map_img = Utils.getImageFromPath("maps/coryinthehouse.png");

        ArrayList<Wall> walls = new ArrayList<Wall>();
        Entity the_holy_grail = null;
        Entity where_they_come_from;
        for(int x=0,imgw=map_img.getWidth();x<imgw;x++) {
            for(int y=0,imgh=map_img.getHeight();y<imgh;y++) {
                Color pixel_color = map_img.getColor(x,y);
                int r=pixel_color.getRed(),g=pixel_color.getGreen(),b=pixel_color.getBlue(),a=pixel_color.getAlpha();
                //System.out.println("X: "+x+" Y: "+y+" "+r+","+g+","+b+","+a);
                int tx = x * MainGame.TOWER_SIZE, ty = y * MainGame.TOWER_SIZE;
                if(r==0&&g==0&&b==0) { // Black
                    Wall wall = new Wall("asteroid.png", new Vector2f(tx,ty));
                    //MainGame.instance.root.addChild(wall);
                    MainGame.instance.root.children.add(wall);
                    wall._setParent(MainGame.instance.root);
                    walls.add(wall);
                } else if(r==0&&g==255&&b==0) {
                    the_holy_grail = new Entity(new Vector2f(tx,ty));
                }
            }
        }
        this.walls = walls.toArray(new Wall[walls.size()]);
        heatmap = new Heatmap(the_holy_grail);

        Object[] map = Utils.loadLevel("maps/checkem.txt");

        ArrayList<String[]> unparsedEnemyData = (ArrayList<String[]>) map[0];
        for(int i=0;i<unparsedEnemyData.size();i++) {
            String[] ed = unparsedEnemyData.get(i);
            ArrayList<String> nd = new ArrayList<String>();
            int[] data = new int[]{420,1000};
            for(String s:ed) {
                if(s.matches("^W[0-9]+$")) {
                    data[0] = Integer.parseInt(s.replaceFirst("W", ""));
                } else if(s.matches("^R[0-9]+$")) {
                    data[1] = Integer.parseInt(s.replaceFirst("R", ""));
                } else {
                    nd.add(s);
                }
            }
            enemyData.add(nd.toArray(new String[nd.size()]));
            waveData.add(data);
        }


        //path = (Vector2f[]) map[1];

        this.addAsInputListener();
    }


    @Override
    public boolean isAcceptingInput() {

        return true;
    }


    @Override
    public void update(int dt) {

        lifetime += dt;
        lastUpdate += dt;

        MainGame.currency += dt*0.001;

        if (selectedEntity == null) {

            entityMenuRight = null;
        }

        if(lastUpdate > theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave) {

            lastUpdate = 0;
            theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave++;
            createEnemy();
        }
        if(this.children.size()==0) {return;}
        Entity c1 = this.children.get(0);
        if(currentMaxEnemyCountForThePresentWaveThatIsOccuring <= theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave) {

            waveCount++;
            if(waveCount >= waveData.size()) {
                // TODO: A WINRAR IS U
                waveCount = 0;
            }
            theAmountOfEnemiesThatHaveSpawnedUnderTheJurisdictionOfTheCurrentWave = 0;
            int[] cdata = waveData.get(waveCount);
            currentMaxEnemyCountForThePresentWaveThatIsOccuring = cdata[0];
            theAmountOfTicksThatItTakesToSpawnAnEnemyDuringTheCurrentWave = cdata[1];
        }
    }

    public EntityLiving addBuffWithChance(EntityLiving e) {

        addChild(e);
        //((Enemy)e).setPath(path);
        return e;
    }

    public EntityLiving createEnemy() {

        String[] options = enemyData.get(waveCount);
        EntityLiving enemy = null;

        try {
            enemy = ((EntityLiving) (Class.forName("com.lmag.gtd.entities.enemies."
                    + options[(int) (Math.random() * (options.length))])
                    .getConstructor(Vector2f.class).newInstance(new Vector2f(-100, -100))));

            addBuffWithChance(enemy);

        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException
                | ClassNotFoundException e) {

            e.printStackTrace();
        }

        enemyCount++;

        return enemy;
    }

    public void setEntitySidebar(Tower e) {

        if(entityMenuRight != null)
            MainGame.instance.lc.removeChild(entityMenuRight);

        selectedEntity = e;

        entityMenuRight = new EntityMenu(e);

        MainGame.instance.lc.addChild(entityMenuRight);
        buyMenuRight.open = false;
    }

    public void setBuySidebar() {

        if(entityMenuRight != null)
            MainGame.instance.lc.removeChild(entityMenuRight);

        buyMenuRight.open = true;
        selectedEntity = null;
    }

    @Override
    public Entity removeChild(Entity e) {

        if (e instanceof EntityLiving) {

            enemyCount--;
        }

        return super.removeChild(e);
    }

    @Override
    public void onMouseReleased(int btn, int x, int y) {

    }

    @Override
    public void onKeyReleased(int kc, char ch) {


    }

    @Override
    public void renderAll(Graphics g) {

        for (Entity ent : children) {

            if (ent == buyMenuRight) {

                if (entityMenuRight == null) {

                    ent.renderAll(g);
                }
            }

            else {

                ent.renderAll(g);
            }
        }

        heatmap.render_heatmap(g);
    }
}
