package com.olasoumarcus.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.olasoumarcus.entities.*;
import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.main.Game;

public class World {

	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static SpriteSheet PIECES_WORLD_SPRITE;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		PIECES_WORLD_SPRITE = new SpriteSheet("/sprites/gfx/Overworld.png");		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[WIDTH * HEIGHT];
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixel = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.FLOOR);

					switch (pixel) {
					case Elements.Wall: {
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.WALL);
						break;
					}
					case Elements.WallWorld: {
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.WallWolrd);
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
						Game.enemies.add(en);
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
	
	public static boolean isFree(int xnext, int ynext) {
		
		return true;
		/* int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE -1)  / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext/ TILE_SIZE;
		int y3 = (ynext + TILE_SIZE -1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE -1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE -1)  / TILE_SIZE;
		
		return !(
				tiles[x1 + (y1*World.WIDTH)] instanceof WallTile &&
				tiles[x2 + (y2*World.WIDTH)] instanceof WallTile &&
				tiles[x3 + (y3*World.WIDTH)] instanceof WallTile &&
				tiles[x4 + (y4*World.WIDTH)] instanceof WallTile);*/
	}	
}
