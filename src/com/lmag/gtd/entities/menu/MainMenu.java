package com.lmag.gtd.entities.menu;

import org.newdawn.slick.geom.Vector2f;

import com.lmag.gtd.LevelController;
import com.lmag.gtd.MainGAme;
import com.lmag.gtd.entities.Entity;
import com.lmag.gtd.entities.Node;
import com.lmag.gtd.util.Executable;
import com.lmag.gtd.util.Renderable;

public class MainMenu extends Node {

	public MainMenu() {
		super(new Vector2f(0,0));
		this.addChild(new Renderable("menu.png", new Vector2f(0,0)));
		
		Executable dank_weed = new Executable(){

			@Override
			public void run() {
				System.out.println("OPTSHUNZ XDXDXDXD");
				MainGAme.instance.root.removeChild((Entity)params.get("parent"));
		    	MainGAme.instance.root.addChild(new Renderable("maps/stars.jpg", new Vector2f(0,0)));
		    	//root.addChild(lc = new EditorController());
		    	MainGAme.instance.root.addChild(MainGAme.instance.lc = new LevelController());
		    	//root.addChild(new DebugEnemy(new Vector2f(100,100)));
			}
			
		};
		
		dank_weed.params.put("parent", this);
		
		this.addChild(new Button(new Vector2f(0,0), dank_weed).forceSize(MainGAme.WIDTH, MainGAme.HEIGHT/2));
		this.addChild(new Button(new Vector2f(0,MainGAme.HEIGHT/2), new Executable(){

			@Override
			public void run() {
				
				System.out.println("OPTSHUNZ XDXDXDXD");
			}
			
		}).forceSize(MainGAme.WIDTH/2, MainGAme.HEIGHT/2));
		this.addChild(new Button(new Vector2f(MainGAme.WIDTH/2,MainGAme.HEIGHT/2), new Executable(){

			@Override
			public void run() {
				
				System.exit(420);
			}
			
		}).forceSize(MainGAme.WIDTH/2, MainGAme.HEIGHT/2));
	}
}
