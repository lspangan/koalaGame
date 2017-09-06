package myGames;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

import gameCore.KoalaWorld;
import static gameCore.KoalaWorld.sound;
import modifiers.AbstractGameModifier;
import modifiers.motions.InputController;

public class PlayerShip extends Ship implements Observer {
    public int lives;
    public int saves;
    public int totalSaves = 3;
    public int score;
    public Point resetPoint;
    public int respawnCounter;
    public int lastFired=0;
    public boolean isFiring=false;
    // movement flags
    public int left=0,right=0,up=0,down=0;
    public String name;

    public PlayerShip(Point location, Point speed, Image img, int[] controls, String name) {
        super(location,speed,100,img);
        resetPoint = new Point(location);
        this.gunLocation = new Point(18,0);
        
        this.name = name;
        motion = new InputController(this, controls);
        lives = 0;
        saves = 0;
        health = 100;
        strength = 100;
        score = 0;
        respawnCounter=0;
    }

    public void draw(Graphics g, ImageObserver observer) {
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
    
    public void damage(int damageDone){
    	if(respawnCounter<=0)
    		super.damage(damageDone);
    }
    
    public void update(int w, int h) {
    	
    	if((location.x>0 || right==1) && (location.x<w-width || left==1)){
    		location.x+=(right-left)*speed.x;
    	}
    	if((location.y>0 || down==1) && (location.y<h-height || up==1)){
    		location.y+=(down-up)*speed.x;
    	}
    }
    
    public void startFiring(){
    	isFiring=true;
    }
    
    public void stopFiring(){
    	isFiring=false;
    }
    
    public void fire()
    {
    	if(respawnCounter<=0){
    		KoalaWorld.getInstance().sound.play("Resources/snd_explosion1.wav");
    	}
    }
    
    public void die(){
    	this.show=false;
    	BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	KoalaWorld.getInstance().addBackground(explosion);
    	lives-=1;
    	if(lives>=0){
        	KoalaWorld.getInstance().removeClockObserver(this.motion);
    		//reset();
    	}
    	else{
    		this.motion.delete(this);
    	}
    }
    
    public void save(){
    	this.show=false;
        sound.play("Chapter07/Saved.wav");
    	//BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	//KoalaWorld.getInstance().addBackground(explosion);
    	KoalaWorld.getInstance().removeClockObserver(this.motion);
    	this.motion.delete(this);
    	  
    }    
    
    public void reset(){
    	this.setLocation(resetPoint);
    	health=strength;
    	respawnCounter=160;
    }
    
    public int getLives(){
    	return this.lives;
    }
    
    public int getSaves(){
        return this.saves;
    }
    
    public int getScore(){
    	return this.score;
    }
    
    public String getName(){
    	return this.name;
    }
    public void incrementSaves(int save){
        saves += save;
    }
    public void incrementScore(int increment){
    	score += increment;
    }
    
    public boolean isDead(){
    	if(lives<0 || health<=0)
    		return true;
    	else
    		return false;
    }
    
    public boolean isSaves(){
        if(saves == 3)
            return true;
        else 
            return false;
    }
    
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
}
