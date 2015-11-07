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

public class LevelController extends Entity {

    int lastUpdate = 0;
    int lifetime = 0;
    int waveCount = 0;
    int enemyCount = 0;

    //public ArrayList<Vector2f> path = new ArrayList<Vector2f>();

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
    /**
     * The exit
     */
    public Entity the_holy_grail = null;
    /**
     * The entrance
     */
    public Entity the_powerhouse_of_the_map = null;

    boolean HOLY_GOD_THIS_ISA_BADTH_ING = false;

    @SuppressWarnings("unchecked")
    public LevelController() {
        super("", new Vector2f(0, 0));

        this.setVisible(false);

        buyMenuRight = new BuyMenu();

        addChild(buyMenuRight);


        Image map_img = Utils.getImageFromPath("maps/coryinthehouse.png");

        ArrayList<Wall> walls = new ArrayList<Wall>();

        for(int x=0,imgw=map_img.getWidth();x<imgw;x++) {

            for(int y=0,imgh=map_img.getHeight();y<imgh;y++) {

                Color pixel_color = map_img.getColor(x,y);
                int r=pixel_color.getRed(),g=pixel_color.getGreen(),b=pixel_color.getBlue(),a=pixel_color.getAlpha();
                int tx = x * MainGame.GRID_SIZE, ty = y * MainGame.GRID_SIZE;

                if(r==0&&g==0&&b==0) { // Black
                    Wall wall = new Wall("asteroid.png", new Vector2f(tx,ty));
                    //MainGame.instance.root.addChild(wall);
                    MainGame.instance.root.children.add(wall);
                    wall._setParent(MainGame.instance.root);
                    walls.add(wall);
                } else if(r==0&&g==255&&b==0) {
                    the_holy_grail = new Entity(new Vector2f(tx,ty));
                } else if(r==255&&g==0&&b==0) {
                    the_powerhouse_of_the_map = new Entity(new Vector2f(tx,ty));
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
            if(!HOLY_GOD_THIS_ISA_BADTH_ING) {createEnemy();HOLY_GOD_THIS_ISA_BADTH_ING=true;}
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
        if(e instanceof Enemy) {
            e.setPos(the_powerhouse_of_the_map.getPos());
            ((Enemy)e).updatePath();
        }
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

    public void on_thing_placed() {
        heatmap.update_heatmap();
        ArrayList<Entity> enemies = Utils.sortByType(Utils.getNearestEntities(MainGame.instance.lc.getCopyOfChildren(), new Vector2f(0,0), -1, -1), "Enemy");
        for(int i=0;i<enemies.size();i++) {
            ((Enemy)enemies.get(i)).updatePath();
        }
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

        if (MainGame.instance.debug) {

            heatmap.render_heatmap(g);
        }

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

    }
}
