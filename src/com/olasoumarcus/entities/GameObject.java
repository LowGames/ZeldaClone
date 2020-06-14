package com.olasoumarcus.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.olasoumarcus.graphics.SpriteSheet;

public class GameObject {

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected SpriteSheet sprite;
	
	public GameObject(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		sprite = new SpriteSheet("/sprites/gfx/Player.png");
	}
	
	public int getX() {
		return (int) this.x;
	}
	
	public void setX(int value) {
		this.x = value;
	}

	public int getY() {
		return (int) this.y;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}

	public void render(Graphics g) {
		g.drawImage(sprite.getSprite(0, 0, 0, 0), this.getX(), this.getY(), null);
	}
	
	public void tick() {
		
	}
}
