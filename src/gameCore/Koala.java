/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameCore;

import static gameCore.KoalaWorld.sound;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import modifiers.motions.InputController;
import myGames.BigExplosion;
import myGames.PlayerShip;


    public class Koala extends PlayerShip {
        public int turningAngle;
        public Koala(Point location, Image img, int[] controls, String name) {
            super(location, new Point(0,0), img, controls, name);
            resetPoint = new Point(location);
        
        this.name = name;
        motion = new InputController(this, controls, KoalaWorld.getInstance());
        lives = 1;
        saves = 0;
        respawnCounter=0;   
        width = 32;
        height = 32;
        this.location = new Rectangle(location.x,location.y,width,height);
        }      
        
        public void update(int w, int h) {
        KoalaWorld world = KoalaWorld.getInstance();
    	
    	if(right==1){
    		location.x+=(right-left);
                this.setImage(world.sprites.get("koalaRight"));
                // if right, load right koala image
                // if left, load left koala image
        } else 
        if(left==1){
            location.x+=(right-left);
            this.setImage(world.sprites.get("koalaLeft"));
            //this.setImage(world.sprites.get("koalaLeftStand"));
        } else 
    	if(down==1){
            location.y+=(down-up);
            this.setImage(world.sprites.get("koalaDown"));
            // if down, load down koala image
            // if up, load up koala image
        } else 
        if(up==1){
            location.y+=(down-up);
            this.setImage(world.sprites.get("koalaUp"));
        } else {
            this.setImage(world.sprites.get("player1"));
        }
        
    	
    	if(location.y<0) location.y=0;
    	if(location.y>h-this.height) location.y=h-this.height;
    	if(location.x<0) location.x=0;
    	if(location.x>w-this.width) location.x=w-this.width;
    }    
        
        public void draw(Graphics g, ImageObserver obs) {
    	if(respawnCounter<=0)
    		g.drawImage(img, location.x, location.y, observer);
    	else if(respawnCounter==80){
    		KoalaWorld.getInstance().addClockObserver(this.motion);
    		respawnCounter -=1;
    	}
    	else if(respawnCounter<80){
    		if(respawnCounter%2==0) g.drawImage(img, location.x, location.y, observer);
    		respawnCounter -= 1;
    	}
    	else
    		respawnCounter -= 1;
    }   
        
    public void respawn(){
    	this.setLocation(resetPoint);
    	respawnCounter=160;
    }        
        
    public void die(){
    	this.show=false;
    	KoalaWorld.setSpeed(new Point(0,0));
    	BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	KoalaWorld.getInstance().addBackground(explosion);
    	lives-=1;
    	if(lives>=0){
        	KoalaWorld.getInstance().removeClockObserver(this.motion);
    		respawn();
    	}
    	else{
    		this.motion.delete(this);
    	}
    }
    /*
    public void save(){
    	this.show=false;
        sound.play("Chapter07/Saved.wav");
    	KoalaWorld.setSpeed(new Point(0,0));
        KoalaWorld.getInstance().removeClockObserver(this.motion);
    	this.motion.delete(this);
        
    } 
    */
        
    }
