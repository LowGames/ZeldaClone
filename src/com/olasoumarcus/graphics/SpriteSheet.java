package com.olasoumarcus.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private BufferedImage sprite;
	
	public SpriteSheet(String path)
	{
		try {
			sprite = ImageIO.read(getClass().getResource(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return sprite.getSubimage(x, y, width, height);
	}
}
