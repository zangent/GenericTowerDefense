package com.lmag.gtd;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.entities.DebugEnt;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.menu.Button;
import com.lmag.gtd.util.Executable;
import com.lmag.gtd.util.Renderable;

import java.io.File;

public class MainGame extends BasicGame {
	
	// Constants.
	public static final String GAME_NAME = "Generic TD";
	public static final int TARGET_FPS = 60;
	public static final int WIDTH  = 800;
	public static final int HEIGHT = 600;
	
	public static GameContainer gc;
	
	public Entity root;
	
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
    	
    	root = new Entity("", new Vector2f(0, 0)).setVisible(false);
    	root.addChild(new Renderable("maps/map1.png", new Vector2f(0,0)));
    	//root.addChild(new EditorController());
    	root.addChild(new LevelController());
    	root.addChild(new DebugEnt());
    	root.addChild(new Button("topkek2015.png", new Vector2f(420,420), new Executable(){

			@Override
			public void run() {
				System.out.println("shrekt");
			}}));
    	//root.addChild(new DebugEnemy(new Vector2f(100,100)));
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    	
    	root.tick(delta);
    }
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
    	
    	root.render(g);
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