package modifiers.motions;

import myGames.Ship;
import java.util.Observable;
import java.util.Observer;

import gameCore.KoalaWorld;
import modifiers.AbstractGameModifier;

/*MotionControllers move around objects!*/
public abstract class MotionController extends AbstractGameModifier implements Observer {
	int fireInterval;
	
	public MotionController(){
		this(KoalaWorld.getInstance());
		fireInterval = -1;
	}
	
	public MotionController(KoalaWorld world){
		world.addClockObserver(this);
	}
	
	public void delete(Observer theObject){
		KoalaWorld.getInstance().removeClockObserver(this);
		this.deleteObserver(theObject);
	}
	
	/*Motion Controllers observe the GameClock and fire on every clock tick
	 * The default controller doesn't do anything though*/
	public void update(Observable o, Object arg){
		this.setChanged();
		this.notifyObservers();
	}
	
	public void read(Object theObject){
		Ship object = (Ship) theObject;
		object.move();
		/*
		if(KoalaWorld.getInstance().getFrameNumber()%fireInterval==0){
			object.fire();
		}*/
	}
}
