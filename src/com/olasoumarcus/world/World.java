package com.olasoumarcus.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.olasoumarcus.entities.*;
import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static SpriteSheet PIECES_WORLD_SPRITE;
	public static final int TILE_SIZE = 16;
	private static int[] pixels;
	public static Map mapCurrent;

	public World(Map map) {
		System.out.print("passou pleo construtor.");
		this.mapCurrent = map;
		tiles = this.mapCurrent.getTiles();
		WIDTH = map.WIDTH;
		HEIGHT = map.HEIGHT;
		pixels = map.getPixels();
	}

	public void render(Graphics g){
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				int index = xx + (yy*mapCurrent.WIDTH);
				
				if(xx < 0 || yy < 0 || xx >= mapCurrent.WIDTH || yy >= mapCurrent.HEIGHT || index < 0)
					continue;

				Tile tile = World.mapCurrent.tiles[index];
				tile.render(g);
			}
		}
	}
	
	public static void renderMiniMap()
	{
		for (int i= 0; i < Game.miniMapPixels.length; i++)
		{
			Game.miniMapPixels[i] = 0;
		}
		
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {

				int index = xx + (yy*WIDTH);
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT || index < 0)
					continue;
				
				if (tiles[index] instanceof WallTile) {
					int pixel = pixels[xx + (yy * WIDTH)];

					if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT || index < 0)
						continue;

					Game.miniMapPixels[xx + (yy*WIDTH)] = pixel;
				}
			}
		}
		
		for (GameObject enemy : Game.enemies) {
			int xEnemy = enemy.getX() / 16;
			int yEnemy = enemy.getY() / 16;
			
			Game.miniMapPixels[xEnemy + (yEnemy*WIDTH)] = 0x0ffff;
		}
		
		
		int xPlayer = Game.player.getX() / 16;
		int yPlayer = Game.player.getY() / 16;
		
		Game.miniMapPixels[xPlayer + (yPlayer*WIDTH)] = 0x0000ff;
	}
	
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-2) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-2) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-2) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-2) / TILE_SIZE;
	
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) &&
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) &&
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) &&
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(Map map) {
		Game.world = new World(map);
		Game.minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Game.miniMapPixels = ((DataBufferInt) Game.minimap.getRaster().getDataBuffer()).getData();		
	}
}
