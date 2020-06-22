package com.olasoumarcus.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.main.Game;
import com.olasoumarcus.world.Node;
import com.olasoumarcus.world.Vector2I;
import com.olasoumarcus.world.World;

public class GameObject {
	public static BufferedImage LIFE = Game.OBJECT_SPRITES.getSprite(80, 0, 16, 16);
	public static BufferedImage WEAPON = Game.OBJECT_SPRITES.getSprite(3*16, 4*16, 16, 16);
	public static BufferedImage ENEMY_HORSE = Game.ENEMY_SPRITE.getSprite(4*16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.ENEMY_SPRITE.getSprite(2*16, 16, 16, 16);

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	private SpriteSheet sprite;
	protected List<Node> path;
	
	public GameObject(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return (int) this.x;
	}
	
	public void setX(int value) {
		this.x = value;
	}
	
	public void setY(int value) {
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

	public double calculateDistance(int x1, int y1, int x2, int y2) {
		// calculando distancia entre dois pontos em ambiente 2d , caso de 3d seria: (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) + (z1-z2)*(z1-z2) 
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	public void FollowPath(List<Node> path) {
		if (path != null) {
			if (path.size() > 0) {
				Vector2I target = path.get(path.size() - 1).tile;
				
				if (x < target.x * 16) {
					x++; 
				}
				else if (x > target.x * 16) {
					x--;
				}
				
				if (y < target.y * 16) {
					y++;
				} 
				else if (y > target.y * 16) {
					y--;
				}
				
				if (x == target.x * 16 && target.y * 16  == y) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
}
