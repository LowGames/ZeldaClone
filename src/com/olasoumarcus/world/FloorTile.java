package com.olasoumarcus.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FloorTile extends Tile {

	public FloorTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		// TODO Auto-generated constructor stub
	}

	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
