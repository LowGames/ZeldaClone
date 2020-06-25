package com.olasoumarcus.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.olasoumarcus.entities.Player;
import com.olasoumarcus.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(10, 10, 50, 10);
		g.setColor(Color.GREEN);
		g.fillRect(10, 10, (int)((Game.player.life/Player.MAXLIFE)*50), 10);
		g.setColor(Color.white);
		
		String text = (int)Game.player.life + "/" + Player.MAXLIFE;
		g.drawString(text,10, 30);
		g.drawString("Munição:"+ Game.player.ammo, 180, 30);
	}
}
