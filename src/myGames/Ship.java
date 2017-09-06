package myGames;

import java.awt.Image;
import java.awt.Point;

import gameCore.KoalaWorld;
import modifiers.motions.MotionController;

/* Ships are things that have weapons and health */
public class Ship extends MoveableObject {
    KoalaWorld world = KoalaWorld.getInstance();
	//protected AbstractWeapon weapon;
	protected int health;
	protected Point gunLocation;
        public Point resetPoint;
        public int respawnCounter = 0;
	
    public Ship(Point location, Point speed, int strength, Image img){
    	super(location,speed,img);
    	this.strength=strength;
    	this.health=strength;
    	this.gunLocation = new Point(15,20);
        resetPoint = new Point(location);
    }
    
    public Ship(int x, Point speed, int strength, Image img){
    	this(new Point(x,-90), speed, strength, img);
    }
    
    /*
    public void setWeapon(AbstractWeapon weapon){
    	this.weapon.remove();
    	this.weapon = weapon;
    }
    
    public AbstractWeapon getWeapon(){
    	return this.weapon;
    }
    */
    public void damage(int damageDone){
    	this.health -= damageDone;
    	if(health<=0){
    		this.die();
    	}
    	return;
    }
    
    public void detonate(){
        this.setImage(world.sprites.get("detonator"));
        this.show = true;
    }
    
    public void die(){
    	this.show=false;
    	SmallExplosion explosion = new SmallExplosion(new Point(location.x,location.y));
    	KoalaWorld.getInstance().addBackground(explosion);
    	//weapon.deleteObserver(this);
    	motion.deleteObserver(this);
    	KoalaWorld.getInstance().removeClockObserver(motion);
    }
    
    public void save(){
    	this.show=false;
    	KoalaWorld.setSpeed(new Point(0,0));
    	this.motion.delete(this);       
    }
    
        public void respawn(){
    	this.setLocation(resetPoint);
    	respawnCounter=10;
        }
    
    public void collide(GameObject otherObject){
    }
    
    /*
    public void fire()
    {
    	weapon.fireWeapon(this);
    }
    */
    /* some setters and getters!*/
    public void setHealth(int health){
    	this.health = health;
    }
    
    public int getHealth(){
    	return health;
    }
    
    
    public MotionController getMotion(){
    	return this.motion;
    }
    
    public void setMotion(MotionController motion){
    	this.motion = motion;
    }
    
    public Point getGunLocation(){
    	return this.gunLocation;
    }
}