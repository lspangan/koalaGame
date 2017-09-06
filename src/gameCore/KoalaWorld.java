package gameCore;

import ui.*;
import modifiers.*;
import myGames.*;
import modifiers.motions.*;
import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Observer;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;

import javax.swing.*;

import modifiers.motions.MotionController;

// extending JPanel to hopefully integrate this into an applet
// but I want to separate out the Applet and Application implementations
public final class KoalaWorld extends JPanel implements Runnable, Observer {

    private Thread thread;
    
    // GameWorld is a singleton class!
    private static final KoalaWorld game = new KoalaWorld();
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();
    GameMenu menu;
    public SourceReader reader;
   
    private BufferedImage bimg;
    static Point speed = new Point(0,0), arena;
    int sizeX, sizeY;
    
    /*Some ArrayLists to keep track of game things*/
    private ArrayList<BackgroundObject> background;
    //private ArrayList<Bullet> bullets;
    private ArrayList<PlayerShip> players;
    private ArrayList<InterfaceObject> ui;
    private ArrayList<Ship> rocks;
    private ArrayList<Ship> tnts;
    private ArrayList<Ship> detonators;
    private ArrayList<Ship> exits;
    private ArrayList<Ship> springs;
    private ArrayList<Ship> saws;
    private ArrayList<Ship> switches;
    private ArrayList<Ship> locks;
    
    
    public static HashMap<String, Image> sprites;
    public static HashMap<String, MotionController> motions = new HashMap<String, MotionController>();

    // is player still playing, did they win, and should we exit
    boolean gameOver, gameWon, gameFinished, levelFinished = false;
    ImageObserver observer;
        
    // constructors makes sure the game is focusable, then
    // initializes a bunch of ArrayLists
    private KoalaWorld(){
        this.setFocusable(true);
        background = new ArrayList<BackgroundObject>();
        players = new ArrayList<PlayerShip>();
        ui = new ArrayList<InterfaceObject>();
        rocks = new ArrayList<Ship>();
        springs = new ArrayList<Ship>();
        saws = new ArrayList<Ship>();
        switches = new ArrayList<Ship>();
        exits = new ArrayList<Ship>();
        tnts = new ArrayList<Ship>();
        locks = new ArrayList<Ship>();
        detonators = new ArrayList<Ship>();
        sprites = new HashMap<String,Image>();
    }
    
    /* This returns a reference to the currently running game*/
    public static KoalaWorld getInstance(){
    	return game;
    }

    /*Game Initialization*/
    public void init() {
        setBackground(Color.WHITE);
        sound.playLoop("Chapter07/Music.mid");
        loadSprites();
        reader = new SourceReader(0);
        reader.addObserver(this);
        clock.addObserver(reader);
        arena = new Point(reader.w*40,reader.h*40);
        KoalaWorld.setSpeed(new Point(0,0));
        gameOver = false;
        observer = this;
        
        addBackground(new Background(arena.x,arena.y,speed, sprites.get("background")));
        menu = new GameMenu();
        //reader.load();
    }
    
    /*Functions for loading image resources*/
    private void loadSprites() {
        sprites.put("background", getSprite("Chapter07/Background.png"));
        sprites.put("wall", getSprite("Chapter07/wallblock.png"));
        
        sprites.put("start", getSprite("Chapter07/Button_start.png"));
        sprites.put("start2", getSprite("Chapter07/Button_start1.png"));
        sprites.put("load", getSprite("Chapter07/Button_load.png"));
        sprites.put("load2", getSprite("Chapter07/Button_load1.png"));
        sprites.put("help", getSprite("Chapter07/Button_help.png"));
        sprites.put("help2", getSprite("Chapter07/Button_help1.png"));
        sprites.put("helpPage", getSprite("Chapter07/Help.png"));
        sprites.put("quit", getSprite("Chapter07/Button_quit.png"));
        sprites.put("quit2", getSprite("Chapter07/Button_quit1.png"));
        sprites.put("title", getSprite("Chapter07/Title.png"));
        sprites.put("restart", getSprite("Chapter07/Restart.png"));
        

        sprites.put("explosion1_1", getSprite("Resources/explosion1_1.png"));
        sprites.put("explosion1_2", getSprite("Resources/explosion1_2.png"));
        sprites.put("explosion1_3", getSprite("Resources/explosion1_3.png"));
        sprites.put("explosion1_4", getSprite("Resources/explosion1_4.png"));
        sprites.put("explosion1_5", getSprite("Resources/explosion1_5.png"));
        sprites.put("explosion1_6", getSprite("Resources/explosion1_6.png"));
        sprites.put("explosion2_1", getSprite("Resources/explosion2_1.png"));
        sprites.put("explosion2_2", getSprite("Resources/explosion2_2.png"));
        sprites.put("explosion2_3", getSprite("Resources/explosion2_3.png"));
        sprites.put("explosion2_4", getSprite("Resources/explosion2_4.png"));
        sprites.put("explosion2_5", getSprite("Resources/explosion2_5.png"));
        sprites.put("explosion2_6", getSprite("Resources/explosion2_6.png"));
        sprites.put("explosion2_7", getSprite("Resources/explosion2_7.png"));
        
        sprites.put("exit", getSprite("Chapter07/Exit1.png"));
        sprites.put("rock", getSprite("Chapter07/rock.png"));
        sprites.put("tnt", getSprite("Chapter07/TNT.png"));
        sprites.put("saw", getSprite("Chapter07/saw.gif"));
        sprites.put("detonator", getSprite("Chapter07/detonator1.png"));
        sprites.put("detonatorGif", getSprite("Chapter07/detonator.gif"));
        sprites.put("switchLeft", getSprite("Chapter07/switch_blue1.png"));
        sprites.put("switchRight", getSprite("Chapter07/switch_blue2.png"));
        sprites.put("lock", getSprite("Chapter07/Lock_blue.png"));
        sprites.put("springGif", getSprite("Chapter08/springDown.gif"));
        sprites.put("spring", getSprite("Chapter08/spring_down2.png"));
        sprites.put("gameover", getSprite("Chapter07/gameover3.png"));
        sprites.put("youWin", getSprite("Chapter07/Congratulation.png"));
        sprites.put("black", getSprite("Chapter07/black.png"));
        sprites.put("powerup", getSprite("Resources/powerup.png"));
        sprites.put("rescued", getSprite("Chapter07/Rescued.png"));

        sprites.put("player1", getSprite("Chapter07/Koala_stand.png"));
        sprites.put("koalaRight", getSprite("Chapter07/koalaRight.gif"));
        sprites.put("koalaLeft", getSprite("Chapter07/koalaLeft.gif"));
        sprites.put("koalaDown", getSprite("Chapter07/koalaDown.gif"));
        sprites.put("koalaUp", getSprite("Chapter07/koalaUp.gif"));
        sprites.put("koalaRightStand", getSprite("Chapter07/koala_right3.png"));
        sprites.put("koalaLeftStand", getSprite("Chapter07/koala_left8.png"));
        sprites.put("koalaDownStand", getSprite("Chapter07/koala_down7.png"));
        sprites.put("koalaUpStand", getSprite("Chapter07/koala_up3.png"));
        sprites.put("koalaDead", getSprite("Chapter07/Koala_dead.png"));
        
    }
    
    public Image getSprite(String name) {
        URL url = KoalaWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
    
    
    
    /********************************
     * 	These functions GET things	*
     * 		from the game world		*
     ********************************/
    
    public int getFrameNumber(){
    	return clock.getFrame();
    }
    
    public int getTime(){
    	return clock.getTime();
    }
    
    public void removeClockObserver(Observer theObject){
    	clock.deleteObserver(theObject);
    }
    
    public ListIterator<BackgroundObject> getBackgroundObjects(){
    	return background.listIterator();
    }
    
    public ListIterator<PlayerShip> getPlayers(){
    	return players.listIterator();
    }
    
    public ListIterator<Ship> getRocks(){
        return rocks.listIterator();
    }
    
    
    public int countPlayers(){
    	return players.size();
    }
    
    public void setDimensions(int w, int h){
    	this.sizeX = w;
    	this.sizeY = h;
    }
    /*
    public ListIterator<Bullet> getBullets(){
    	return bullets.listIterator();
    }  
    */
    public static Point getSpeed(){
    	return new Point(KoalaWorld.speed);
    }  
    
    public static void setSpeed(Point speed){
    	KoalaWorld.speed = speed;
    }
    
    /********************************
     * 	These functions ADD things	*
     * 		to the game world		*
     ********************************/

    public void addPlayer(PlayerShip...newObjects){
    	for(PlayerShip player : newObjects){
    		players.add(player);
    		ui.add(new InfoBar(player,Integer.toString(players.size())));
    	}
    }
    
    // add background items (islands)
    public void addBackground(BackgroundObject...newObjects){
    	for(BackgroundObject object : newObjects){
    		background.add(object);
    	}
    }
    
    public void addTNT(Ship tnt){
        tnts.add(tnt);
    }
    
    public void addDetonator(Ship detonator){
        detonators.add(detonator);
    }
    
    public void addExit(Ship exit){
        exits.add(exit);
    }
    
    public void addRock(Ship rock){
        rocks.add(rock);
    }
    
    public void addSaw(Ship saw){
        saws.add(saw);
    }
    
    public void addSpring(Ship spring){
        springs.add(spring);
    }
    
    public void addLock(Ship lock){
        locks.add(lock);
    }
    
    public void addSwitch(Ship switches){
        this.switches.add(switches);
    }
    
    
    public void addClockObserver(Observer theObject){
    	clock.addObserver(theObject);
    }
    
    // this is the main function where game stuff happens!
    // each frame is also drawn here
    public void drawFrame(int w, int h, Graphics2D g2) {
        
 
        ListIterator<?> iterator = getBackgroundObjects();
        // iterate through all blocks
        while (iterator.hasNext()) {
            BackgroundObject obj = (BackgroundObject) iterator.next();
            obj.update(w, h);
            obj.draw(g2, this);
            
            if (obj instanceof BigExplosion || obj instanceof SmallExplosion) {
                if (!obj.show) {
                    iterator.remove();
                }
                continue;
            }
            
            // player-to-wall and player-to-rock collision detections
            ListIterator<PlayerShip> players = getPlayers();
            while (players.hasNext() && obj.show) {
                Koala player = (Koala) players.next();
                Rectangle location = obj.getLocation();
                Rectangle playerLocation = player.getLocation();
                if (obj.collision(player)) {
                    if (obj.img.equals(sprites.get("wall"))) {
                        if (playerLocation.y < location.y) {
                            player.move(0, -1);
                        }
                        if (playerLocation.y > location.y) {
                            player.move(0, 1);
                        }
                        if (playerLocation.x < location.x) {
                            player.move(-1, 0);
                        }
                        if (playerLocation.x > location.x) {
                            player.move(1, 0);
                        }
                    }
                    if (obj.img.equals(sprites.get("rock"))) {
                        sound.play("Chapter07/Rock.wav");
                        if (playerLocation.y < location.y) {
                            player.move(0, -1);
                        }
                        if (playerLocation.y > location.y) {
                            player.move(0, 1);
                        }
                        if (playerLocation.x < location.x) {
                            player.move(-1, 0);
                        }
                        if (playerLocation.x > location.x) {
                            player.move(1, 0);
                        }
                    }                    
                    
                }
            }
            
            //saw-to-wall collision detection
            ListIterator<Ship> saw = saws.listIterator();
            while (saw.hasNext() && obj.show) {
                Ship s = (Ship) saw.next();
                Rectangle location = obj.getLocation();
                Rectangle loc = s.getLocation();
                if (obj.collision(s)) {
                    if (obj.img.equals(sprites.get("wall"))) {
                        if (location.y < loc.y) {
                            s.speed = new Point(0,1);
                            
                        }
                        if (location.y > loc.y) {
                            s.speed = new Point(0,-1);
                        }
                    }                                    
                }
            }            
            
        }
        
        if (menu.isWaiting()) {
            menu.draw(g2, w, h);
        } else if (!gameFinished) {
            
            PlayerShip p1 = players.get(0);
            PlayerShip p2 = players.get(1);
            PlayerShip p3 = players.get(2);
            
                
                if ((p1.lives < 0 || p2.lives < 0 || p3.lives < 0)) {
                    gameOver = true;  
                }
                
                //loads level 2
                if ((p1.saves ==3) && reader.getLevel() == 0){
                    endLevel();

                    p1.setLocation(p1.resetPoint);
                    p2.setLocation(p2.resetPoint);
                    p3.setLocation(p3.resetPoint);
                    p1.saves = 0;                       
                } 
                //finishes game
                if((p1.saves == 3) && (reader.getLevel() == 1)){
                        game.finishGame();
                        isGameFinished();
                        gameWon = true;
                     
                }
            
            //player-to-tnt and player-to-detonator collsion detection
            ListIterator<?> it2 = detonators.listIterator();
            iterator = tnts.listIterator();
            while (iterator.hasNext() && it2.hasNext()) {
                Ship powerup = (Ship) iterator.next();
                Ship det = (Ship) it2.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext() && powerup.show) {
                    PlayerShip player = players.next();
                    if (powerup.collision(player)) {
                        player.die();
                        p1.setLocation(p1.resetPoint);
                        p2.setLocation(p2.resetPoint);
                        p3.setLocation(p3.resetPoint);
                        p1.saves = 0;
                        iterator.remove(); 
                    } 
                    if (det.collision(player)) {
                        det.detonate();
                        
                        powerup.die(); 
                        iterator.remove();
                        it2.remove();
                    }
                }
                powerup.draw(g2, this);
                det.draw(g2, this);
                
            }           
            
            //player-to-exit collsion detection
            iterator = exits.listIterator();
            while (iterator.hasNext()) {
                Ship exit = (Ship) iterator.next();
                
                    if (exit.collision(p1)) {
                        p1.save();
                        p1.saves+=1;
                        p1.setLocation(new Point(-10,-10));
                    }
                    if(exit.collision(p2)){
                        p2.save();
                        p1.saves+=1;
                        p2.setLocation(new Point(-10,-10));
                    }
                    if(exit.collision(p3)){
                        p3.save();
                        p1.saves+=1;
                        p3.setLocation(new Point(-10,-10));
                    }       
                exit.draw(g2, this);
            }
            
            
            //player-to-lock collision detection
            it2 = locks.listIterator();
            iterator = switches.listIterator();
            while (iterator.hasNext() && it2.hasNext()) {
                Ship switches = (Ship) iterator.next();
                Ship lock = (Ship) it2.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext() && switches.show) {
                    PlayerShip player = players.next();
                    if (switches.collision(player)) {
                        switches.show();
                        switches.setImage(sprites.get("switchRight"));
                        lock.die();
                        lock.setLocation(new Point(400,55));
                    } 
                    if (lock.collision(player)) {
                        Rectangle loc1 = player.getLocation();
                        Rectangle loc2 = lock.getLocation();
                        if (loc1.y < loc2.y) {
                            player.move(0, -1);
                        }
                        if (loc1.y > loc2.y) {
                            player.move(0, 1);
                        }
                        if (loc1.x < loc2.x) {
                            player.move(-1, 0);
                        }
                        if (loc1.x > loc2.x) {
                            player.move(1, 0);
                        } 
                    }
                }
                switches.draw(g2, this);
                lock.draw(g2, this);
                
            } 
            
            //player-to-saw collision detection
            iterator = saws.listIterator();
            while (iterator.hasNext()) {
                Ship saw = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext() && saw.show) {
                    PlayerShip player = players.next();
                    if (saw.collision(player)) {
                        sound.play("Chapter07/Saw.wav");
                        player.die();
                        p1.setLocation(p1.resetPoint);
                        p2.setLocation(p2.resetPoint);
                        p3.setLocation(p3.resetPoint);
                        p1.saves = 0;
                        //saw.detonate();
                        //powerup.
                        //iterator.remove();
                    } 
                    
                }
                saw.draw(g2, this);
                
            }            
            
            //player-to-spring collision detection
            iterator = springs.listIterator();
            while (iterator.hasNext()) {
                Ship spring = (Ship) iterator.next();
                ListIterator<PlayerShip> players = getPlayers();
                while (players.hasNext() && spring.show) {
                    PlayerShip player = players.next();
                    if (spring.collision(player)) {
                        sound.play("Chapter08/Spring.wav");
                        spring.setImage(sprites.get("springGif"));
                        Rectangle loc1 = player.getLocation();
                        Rectangle loc2 = spring.getLocation();
                        if (loc1.y < loc2.y) {
                            player.move(0, -50);
                        }
                        if (loc1.y > loc2.y) {
                            player.move(0, -50);
                        }
                        if (loc1.x < loc2.x) {
                            player.move(0, -50);
                        }
                        if (loc1.x > loc2.x) {
                            player.move(0, -50);
                        } 
                    } 
                    
                }
                spring.draw(g2, this);
                
            }            
            
            

            // player-to-player collisions
            p1.update(w, h);
          
            if (p1.collision(p2) || p1.collision(p3)) {
                Rectangle pLoc1 = p1.getLocation();
                Rectangle pLoc2 = p2.getLocation();
                Rectangle pLoc3 = p3.getLocation();
                if ((pLoc1.y < pLoc2.y) || (pLoc1.y < pLoc3.y)) {
                    p1.move(0, -1);
                }
                if ((pLoc1.y > pLoc2.y) || (pLoc1.y > pLoc3.y)) {
                    p1.move(0, 1);
                }
                if ((pLoc1.x < pLoc2.x) || (pLoc1.x < pLoc3.x)) {
                    p1.move(-1, 0);
                }
                if ((pLoc1.x > pLoc2.x) || (pLoc1.x > pLoc3.x)) {
                    p1.move(1, 0);
                }
            }         
            p2.update(w, h);
            if (p2.collision(p1) || p2.collision(p3)) {
                Rectangle pLoc1 = p1.getLocation();
                Rectangle pLoc2 = p2.getLocation();
                Rectangle pLoc3 = p3.getLocation();
                if ((pLoc2.y < pLoc1.y) || (pLoc2.y < pLoc3.y)) {
                    p2.move(0, -1);
                }
                if ((pLoc2.y > pLoc1.y) || (pLoc2.y > pLoc3.y)) {
                    p2.move(0, 1);
                }
                if ((pLoc2.x < pLoc1.x) || (pLoc2.x < pLoc3.x)) {
                    p2.move(-1, 0);
                }
                if ((pLoc2.x > pLoc1.x) || (pLoc2.x > pLoc3.x)) {
                    p2.move(1, 0);
                }
            }
            p3.update(w, h);
            if (p3.collision(p1) || p3.collision(p2)) {
                Rectangle pLoc1 = p1.getLocation();
                Rectangle pLoc2 = p2.getLocation();
                Rectangle pLoc3 = p3.getLocation();
                if ((pLoc3.y < pLoc1.y) || (pLoc3.y < pLoc2.y)) {
                    p3.move(0, -1);
                }
                if ((pLoc3.y > pLoc1.y) || (pLoc3.y > pLoc2.y)) {
                    p3.move(0, 1);
                }
                if ((pLoc3.x < pLoc1.x) || (pLoc3.x < pLoc2.x)) {
                    p3.move(-1, 0);
                }
                if ((pLoc3.x > pLoc1.x) || (pLoc3.x > pLoc2.x)) {
                    p3.move(1, 0);
                }
            }            
            p1.draw(g2, this);
            p2.draw(g2, this);
            p3.draw(g2, this);
        } // end game stuff
        else {
            if (gameOver == true) {
                addBackground(new Background(arena.x,arena.y,speed, sprites.get("black")));
                g2.setBackground(Color.BLACK);
                g2.drawImage(sprites.get("gameover"), 0, 0, 640, 480, null); 
            } else 
            
            if(isGameFinished()){
                addBackground(new Background(arena.x,arena.y,speed, sprites.get("black")));
                g2.drawImage(sprites.get("black"), 0 , 0, 640, 480, null);
                g2.setBackground(Color.BLACK);
                g2.drawImage(sprites.get("youWin"), 30, 40, 590, 430, null);
            }
            
        }

    }
    
    public void loadLevel(int level){
 		reader.setLevel(level);
		reader.load();       
    }
        
    /* End the game, and signal either a win or loss */
    public void endGame(boolean win){
    	this.gameOver = true;
    	this.gameWon = win;
    }
    
    public boolean isGameOver(){
    	return gameOver;
    }
    
    public boolean isLevelFinished(){
        return levelFinished;
    }
    
    public void endLevel(){
        this.levelFinished = true;
        game.loadLevel(1);
    }
    
    // signal that we can stop entering the game loop
    public void finishGame(){
    	gameFinished = true;
    }
    
    public boolean isGameFinished(){
        return gameFinished;
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    /* paint each frame */
    public void paint(Graphics g) {
        if(players.size()!=0)
        	clock.tick();
    	Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(windowSize.width,windowSize.height);
        drawFrame(windowSize.width,windowSize.height, g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
        
        // interface stuff
        ListIterator<InterfaceObject> objects = ui.listIterator();
        int offset = 0;
        while(objects.hasNext()){
        	InterfaceObject object = objects.next();
        	object.draw(g, offset, windowSize.height);
        	offset += 500;
        }        
    }
    

    /* start the game thread*/
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /* run the game */
    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
        	this.requestFocusInWindow();
            repaint();
          
          try {
                thread.sleep(23); // pause a little to slow things down
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }
    
    

    /*I use the 'read' function to have observables act on their observers.
     */
	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
        
	public static void main(String argv[]) {
	    final KoalaWorld game = KoalaWorld.getInstance();
	    JFrame f = new JFrame("Koala Game");
	    f.addWindowListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        game.requestFocusInWindow();
		    }
	    });
	    f.getContentPane().add("Center", game);
	    f.pack();
	    f.setSize(new Dimension(640, 500));
	    game.setDimensions(640, 480);
	    game.init();
	    f.setVisible(true);
	    f.setResizable(false);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    game.start();
	}        
}