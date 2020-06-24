package com.olasoumarcus.entities;

import java.awt.Color;
import java.awt.Graphics;

import com.olasoumarcus.main.Game;
import com.olasoumarcus.world.Camera;

public class BulletShot extends GameObject {

	private double dx;
	private double dy;
	private double speed = 4;
	private int life = 60, curlife = 0;

	public BulletShot(double x, double y, int width, int height, double dx , double dy) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		this.dy = dy;
		this.dx = dx;
	}
	
	public void tick() {
		x+=dx;
		y+=dy;
		curlife++;
		if (curlife == life) {
			Game.shots.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		if (Game.player.isRight()) {
			g.drawImage(GameObject.ARROW_RIGHT, this.getX() - Camera.x,  this.getY() - Camera.y - 5 , null);
		}
		else if (Game.player.isLeft())
		{ 
			g.drawImage(GameObject.ARROW_LEFT, this.getX() - Camera.x,  this.getY() - Camera.y - 5, null);			
		}
	}

}
