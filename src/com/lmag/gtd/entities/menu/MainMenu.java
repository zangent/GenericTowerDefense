package com.lmag.gtd.entities.menu;

import com.lmag.gtd.LevelController;
import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.MainGame;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.Node;
import com.lmag.gtd.util.Executable;
import com.lmag.gtd.util.Renderable;
import org.newdawn.slick.state.GameState;

public class MainMenu extends Node {

	public MainMenu() {
		super(new Vector2f(0, 0));
		this.addChild(new Renderable("menu.png", new Vector2f(0,0)));

		Executable open_MaP = new Executable(){

			@Override
			public void run() {
				MainGame.instance.root.removeChild((Entity) params.get("parent"));
		    	//MainGame.instance.root.addChild(new Renderable("maps/stars.jpg", new Vector2f(0,0)));
		    	//root.addChild(lc = new EditorController());
		    	MainGame.instance.root.addChild(MainGame.instance.lc = new LevelController());
		    	//root.addChild(new DebugEnemy(new Vector2f(100,100)));
			}
			
		};
		
		open_MaP.params.put("parent", this);
		
		this.addChild(new Button(new Vector2f(0,0), open_MaP).forceSize(MainGame.WIDTH, MainGame.HEIGHT/2));
		this.addChild(new Button(new Vector2f(0,MainGame.HEIGHT/2), new Executable(){

			@Override
			public void run() {
				
				System.out.println("OPTSHUNZ XDXDXDXD");
			}
			
		}).forceSize(MainGame.WIDTH/2, MainGame.HEIGHT/2));
		this.addChild(new Button(new Vector2f(MainGame.WIDTH/2,MainGame.HEIGHT/2), new Executable(){

			@Override
			public void run() {
				
				System.exit(420);
			}
			
		}).forceSize(MainGame.WIDTH/2, MainGame.HEIGHT/2));
	}
}
