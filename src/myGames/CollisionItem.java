package myGames;

import java.awt.Point;
import java.util.Observable;
import java.awt.Image;
import gameCore.KoalaWorld;
import modifiers.AbstractGameModifier;
import modifiers.motions.SimpleMotion;

/* PowerUp extends ship so that it can hold a weapon to give to player*/
public class CollisionItem extends Ship {
	public CollisionItem(Ship theShip){
		super(theShip.getLocationPoint(), theShip.getSpeed(), 1, KoalaWorld.sprites.get("saw"));
		this.motion = new SimpleMotion();
		this.motion.addObserver(this);
        }
	
        
        public CollisionItem(Point location, int health, Image img){
                super(new Point(location), new Point(0,0), health, img);
                this.motion = new SimpleMotion();
                this.motion.addObserver(this);
        }
	
	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
	
	public void die(){
    	this.show=false;
    	//weapon.deleteObserver(this);
    	motion.deleteObserver(this);
    	KoalaWorld.getInstance().removeClockObserver(motion);
	}


}
