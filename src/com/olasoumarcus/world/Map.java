package com.olasoumarcus.world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.olasoumarcus.entities.Bullet;
import com.olasoumarcus.entities.Enemy;
import com.olasoumarcus.entities.EnumEnemies;
import com.olasoumarcus.entities.GameObject;
import com.olasoumarcus.entities.LifePack;
import com.olasoumarcus.entities.Player;
import com.olasoumarcus.entities.Weapon;
import com.olasoumarcus.main.Game;

public class Map {

	public static Tile[] tiles;
	public int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	private static int[] pixels;
	private static BufferedImage image;
		
	public Map(String path, EnumEnemies typeEnemy) {
		try {
			Game.gameObjects = new ArrayList<GameObject>();
			Game.enemies = new ArrayList<GameObject>();
			Game.player = new Player(80,80,100,100);
			Game.gameObjects.add(Game.player);
			image = ImageIO.read(getClass().getResource(path));
			pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			WIDTH = image.getWidth();
			HEIGHT = image.getHeight();
			tiles = new Tile[WIDTH * HEIGHT];
			for (int xx = 0; xx < image.getWidth(); xx++) {
				for (int yy = 0; yy < image.getHeight(); yy++) {
					int pixel = pixels[xx + (yy * image.getWidth())];					
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
					case Elements.Ammo: {
						//System.out.println("Ammo");
						Bullet bl = new Bullet(xx*16, yy*16, 16,16);
						Game.gameObjects.add(bl);
						break;
					}
					case Elements.Enemy: {						
						Enemy en = new Enemy(xx*16, yy*16, 16, 16, typeEnemy);
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
	
	public Tile[] getTiles() {
		return this.tiles;
	}; 
	
	public int[] getPixels() {
		return pixels;
	}; 
	
	public BufferedImage getImage() {
		return image;
	}; 
}
