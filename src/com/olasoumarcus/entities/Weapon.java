package com.olasoumarcus.entities;

import java.awt.Graphics;

import com.olasoumarcus.world.Camera;

public class Weapon extends GameObject {

	public Weapon(double x, double y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	public void render(Graphics g) {
		g.drawImage(GameObject.WEAPON, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
