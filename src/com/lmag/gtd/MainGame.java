package com.lmag.gtd;

import com.lmag.gtd.entities.menu.BuyMenu;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.menu.MainMenu;

import java.io.File;

public class MainGame extends BasicGame {
	
	// Constants.
	public static final String GAME_NAME = "Generic TD";
	public static final int TARGET_FPS = 60;
	public static final int WIDTH  = 872;
	public static final int HEIGHT = 672;
	public static final int GRID_SIZE = 32;
    public static final int WIDTH_IN_TILES = (WIDTH-BuyMenu.width)/ GRID_SIZE;
    public static final int HEIGHT_IN_TILES = HEIGHT/ GRID_SIZE;
	public static FontRenderer badFont;

	public static float currency = 9099999;
	public static GameContainer gc;
		
	public Entity root;
	public LevelController lc;

    /**
     * Enables debug mode. Duh.
     */
	public boolean debug = true;
	
	public static MainGame instance;
	
	public MainGame() {
		super(GAME_NAME);
	}
	
	public Vector2f getMousePos() {
		return new Vector2f(Mouse.getX(), HEIGHT-Mouse.getY());
	}
    @Override
    public void init(GameContainer container) throws SlickException {
    	
    	gc = container;
    	
    	badFont = new FontRenderer("badfont.png");
    	
    	root = new Entity("", new Vector2f(0, 0)).setVisible(false);
    	root.addChild(new MainMenu());
    	
    	
    	/*private boolean initialize(boolean reboot) { boolean retry = false; temp = new File("mods/tmp"); if (!mods.isDirectory()) mods.mkdir(); try { if (reboot) { modList = new File("mods").listFiles(); modList = filterFileArray(modList); } else if (!temp.isFile()) { temp.createNewFile(); temp.deleteOnExit(); modList = new File("mods").listFiles(); modList = filterFileArray(modList); } else { int answer = JOptionPane.showConfirmDialog(null, "An instance of this program may already be running!\n\nIf you wish to start anyway press OK. (WARNING: MAY CAUSE ERRORS)\nOtherwise, click CANCEL.", "Warning!", JOptionPane.OK_CANCEL_OPTION); if (answer == JOptionPane.OK_OPTION) { temp.delete(); retry = true; } else System.exit(0); } } catch (IOException e) { e.printStackTrace(); } return retry; }*/
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    	
    	root.tick(delta);
    }
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
    	
    	root.renderAll(g);
    	//badFont.render(50, 50, g, "Ayyy lamooo");
    }
    

	public static void main(String[] args) {
				
		// Load natives from lib/native folder
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "lib/native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		
		// Start game
		try {
			AppGameContainer app = new AppGameContainer(instance = new MainGame());
	        app.setDisplayMode(WIDTH, HEIGHT, false);
	        app.setTargetFrameRate(TARGET_FPS);
	        app.start();
		} catch (SlickException e) {
			// Couldn't launch D:
			e.printStackTrace();
		}
	}
}