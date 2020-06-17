package com.olasoumarcus.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.olasoumarcus.entities.Player;
import com.olasoumarcus.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(20, 20, 50, 20);
		g.setColor(Color.GREEN);
		g.fillRect(20, 20, (int)((Game.player.life/Player.MAXLIFE)*50), 20);
		g.setColor(Color.white);
		
		String text = (int)Game.player.life + "/" + Player.MAXLIFE;
		g.drawString(text,30, 50);
		g.drawString("Munição:"+ Game.player.ammo, 650, 40);
	}
}
