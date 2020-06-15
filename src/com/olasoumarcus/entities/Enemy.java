package com.olasoumarcus.entities;

import java.awt.Graphics;

import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.ItemTile;

public class Enemy extends GameObject {

	public Enemy(double x, double y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	
	
	public void render(Graphics g) {
		g.drawImage(GameObject.ENEMY_HORSE, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
}
