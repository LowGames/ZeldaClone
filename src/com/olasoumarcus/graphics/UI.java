package com.olasoumarcus.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import com.olasoumarcus.entities.Player;
import com.olasoumarcus.main.Game;

public class UI {
	
	public void render(Graphics g) {
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		g.setColor(Color.RED);
		g.fillRect((int)(width*0.7/ 100), 10, 50, 10);
		g.setColor(Color.GREEN);
		g.fillRect((int)(width*0.7/ 100), 10, (int)((Game.player.life/Player.MAXLIFE)*50), 10);
		g.setColor(Color.white);

		String text = (int)Game.player.life + "/" + Player.MAXLIFE;
		g.drawString(text,(int)(width*0.7/ 100),30);
		
		String textEnemies = "Inimigos vivos:" + Game.enemies.size();
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString(textEnemies,(int)(width*7/ 100),40);
		g.drawString("Level:"+ Game.CUR_LEVEL,(int)(width*9/ 100),20);
		
		g.drawString("Munição:"+ Game.player.ammo, (int)(width*9/ 100), 30);
	}
}
