/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Point;
import gameCore.KoalaWorld;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Rock extends BackgroundObject {
	public Rock(int x, int y){
		super(new Point(x*32, y*32), new Point(0,0), KoalaWorld.sprites.get("rock"));
	} 
	
	public void damage(int damage){
		return;
	}
}
