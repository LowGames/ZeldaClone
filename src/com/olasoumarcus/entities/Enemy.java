package com.olasoumarcus.entities;

import java.awt.Graphics;import com.olasoumarcus.main.Game;
import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.ItemTile;

public class Enemy extends GameObject {

	private double speed = 0.6;
	
	public Enemy(double x, double y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	
	
	public void render(Graphics g) {
		g.drawImage(GameObject.ENEMY_HORSE, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void tick() {
		if ((int)x < Game.player.getX()) {
			x+=speed;
		}
		else if ((int)x > Game.player.getX()) {
			x-=speed;
		}
		
		else if ((int)y < Game.player.getY()) {
			y+=speed;
		}
		
		else if ((int)y > Game.player.getY()) {
			y-=speed;
		}
	}
	
}
