/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import gameCore.GameSounds;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Observable;


import modifiers.motions.MenuController;
import gameCore.KoalaWorld;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import myGames.PlayerShip;
import modifiers.AbstractGameModifier;

public class GameMenu extends InterfaceObject {
	int selection;
	MenuController controller;
	boolean waiting;
        KoalaWorld world = KoalaWorld.getInstance();
        Graphics g2;  
        //public static final GameSounds sound = new GameSounds();
	
	public GameMenu(){
		selection = 0;
		controller = new MenuController(this);
		waiting = true;
                world.addKeyListener(controller);
                
	}
	public void draw(Graphics g2, int x, int y){
            
             g2.drawImage(world.sprites.get("title"), x/9, y/15-10, observer);
             //g2.drawImage(world.sprites.get("start"), x/5, y-150 , observer);
             //g2.drawImage(world.sprites.get("help"), x-250, y-150, observer);
             //g2.drawImage(world.sprites.get("load"), x/5, y-70, observer);
             //g2.drawImage(world.sprites.get("quit"), x-250, y-70, observer);
                    
	g2.setFont(new Font("Calibri", Font.PLAIN, 24));
		if(selection==0) {
			g2.setColor(Color.RED);
                        g2.drawImage(world.sprites.get("start"), 270, y-140 , observer);
                       
                }else{
                    g2.drawImage(world.sprites.get("start2"), 270, y-140 , observer);
                    g2.setColor(Color.WHITE);
		//g2.drawString("Level 1", 200,150);
                }              
                
		if(selection==2){
                        g2.drawImage(world.sprites.get("quit"), 270, y-70, observer);
			g2.setColor(Color.RED);
                }else{
			g2.setColor(Color.WHITE);
                        g2.drawImage(world.sprites.get("quit2"), 270, y-70, observer);
		//g2.drawString("Quit", 200, 350); 
                }
	}
	
	public void down(){
		if(selection == 0)
                    selection = 2;
	}
	
	public void up(){
                if(selection == 2)
                    selection = 0;
	}
        
	public void applySelection(){
		if(selection == 2){
			System.exit(0);
		}
		world.sound.play("Chapter07/Click.wav");
		controller.deleteObservers();
		world.removeKeyListener(controller);
		world.loadLevel(selection);
		waiting=false;
                
	}
	
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
	
	public boolean isWaiting(){
		return this.waiting;
	}
}
