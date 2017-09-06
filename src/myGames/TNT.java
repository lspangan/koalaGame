/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Point;
import java.util.Observable;

import gameCore.KoalaWorld;
import java.awt.Image;
import modifiers.AbstractGameModifier;
import modifiers.motions.SimpleMotion;

public class TNT extends Ship {
    int respawnCounter = 0;
	public TNT(Ship theShip){
		super(theShip.getLocationPoint(), theShip.getSpeed(), 1, KoalaWorld.sprites.get("tnt"));
		this.motion = new SimpleMotion();
		this.motion.addObserver(this);
		//this.weapon = theShip.getWeapon();
	}
        
        public TNT(Point location, int health, Image img){
                super(new Point(location), new Point(0,0), health, img);
        } 
        
        public boolean collision(GameObject obj) {
            if(location.intersects(obj.getLocation())){
        	if(obj instanceof Ship) {
        		this.show = false;
                }
        	return true;
                
        } 
        return false;
    }        

	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
        
	@Override
	public void die(){
    	this.show=false;
        BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	KoalaWorld.getInstance().addBackground(explosion);          
    	//weapon.deleteObserver(this);
    	motion.deleteObserver(this);
    	KoalaWorld.getInstance().removeClockObserver(motion);
        this.respawn();
	}



}
