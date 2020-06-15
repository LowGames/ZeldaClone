package com.olasoumarcus.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.olasoumarcus.entities.*;
import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.main.Game;

public class World {

	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static SpriteSheet PIECES_WORLD_SPRITE;
	
	public World(String path) {
		PIECES_WORLD_SPRITE = new SpriteSheet("/sprites/gfx/Overworld.png");		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			this.tiles = new Tile[WIDTH * HEIGHT];
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixel = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.FLOOR);

					switch (pixel) {
					case Elements.Wall: {
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.WALL);
						break;
					}
					case Elements.WallWorld: {
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.WallWolrd);
						break;
					}					
					case Elements.Life: {
						LifePack lf = new LifePack(xx*16, yy*16, 16,16);
						Game.gameObjects.add(lf);
						break;
					}
					case Elements.Weapon: {
						Weapon wp = new Weapon(xx*16, yy*16, 16,16);
						Game.gameObjects.add(wp);
						break;
					}
					case Elements.Enemy: {						
						Enemy en = new Enemy(xx*16, yy*16, 16, 16);
						Game.gameObjects.add(en);
						break;
					}
					case Elements.Player: {
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						break;
					}
					default:
						//throw new IllegalArgumentException("Unexpected value: " + pixel);
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				int index = xx + (yy*WIDTH);
				Tile tile = tiles[index];
				if (tile == null) continue;
				tile.render(g);
			}
		}
	}
}
