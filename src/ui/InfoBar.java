package ui;

import myGames.PlayerShip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;
import gameCore.KoalaWorld;


public class InfoBar extends InterfaceObject {
	PlayerShip player;
	String name;

	
	public InfoBar(PlayerShip player, String name){
		this.player = player;
		this.name = name;
	}
	
	public void draw(Graphics g2, int x, int y){
            KoalaWorld world = KoalaWorld.getInstance();
            Image life = KoalaWorld.sprites.get("player1");
            //life.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
            /*
            g2.setFont(new Font("Calibri", Font.PLAIN, 40));
            if (player.getHealth() > 40) {
                g2.setColor(Color.GREEN);
            } else if (player.getHealth() > 20) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.RED);
            }
            g2.fillRect(x + 2, y - 25, (int) Math.round(player.getHealth() * 1.1), 20);
                    */
            for (int i = 0; i < player.getSaves(); i++) {
                g2.drawImage(life, 140 + i * 60, 20, observer);
            }
            g2.setColor(Color.WHITE);
            g2.drawImage(world.sprites.get("rescued"), 10, y - 460, observer);
            //JPanel panel = new JPanel();
            //g2.drawString(Integer.toString(player.getSaves()), 150, y - 425);
	}

}
