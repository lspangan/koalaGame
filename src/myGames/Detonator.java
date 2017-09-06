/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import gameCore.KoalaWorld;
import java.awt.Image;
import java.awt.Point;
import java.util.Observable;
import modifiers.AbstractGameModifier;
import modifiers.motions.SimpleMotion;


public class Detonator extends Ship{
        KoalaWorld world = KoalaWorld.getInstance();
	public Detonator(Ship theShip){
		super(theShip.getLocationPoint(), theShip.getSpeed(), 0, KoalaWorld.sprites.get("detonator"));
                if(theShip.collision(this)){
                    this.setImage(world.sprites.get("detonatorGif"));
                    this.die();
                }                
		this.motion = new SimpleMotion();
		this.motion.addObserver(this);
		//this.weapon = theShip.getWeapon();
	}
        
        public Detonator(Point location, int health, Image img){
                super(new Point(location), new Point(0,0), health, img);
        }        

	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
	
        
	public void dead(){
        this.setImage(world.sprites.get("detonator"));
        this.show = true;
        //BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
    	//KoalaWorld.getInstance().addBackground(explosion);          
    	//weapon.deleteObserver(this);
    	//this.show=false;
    	//weapon.deleteObserver(this);
    	//motion.deleteObserver(this);
    	//KoalaWorld.getInstance().removeClockObserver(motion);
	}
        

}
