package com.olasoumarcus.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	public static BufferedImage FLOOR = World.PIECES_WORLD_SPRITE.getSprite(0, 0, 16, 16);
	public static BufferedImage WALL = World.PIECES_WORLD_SPRITE.getSprite(304, 208, 16, 16);
	public static BufferedImage WallWolrd = World.PIECES_WORLD_SPRITE.getSprite(304, 208, 16, 16);

	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
