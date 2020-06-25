package com.olasoumarcus.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.olasoumarcus.main.Game;
import com.olasoumarcus.main.Sound;
import com.olasoumarcus.world.AStart;
import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.Vector2I;
import com.olasoumarcus.world.World;

public class Enemy extends GameObject {

	private double speed = 0.4;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 3;
	private BufferedImage[] animations_right;
	private BufferedImage[] animations_left;
	private int life = 3;
	private boolean isDamaged = false;
	private int damageFrames = 0;
	
	public Enemy(double x, double y, int width, int height, EnumEnemies typeEnemy) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		animations_right = new BufferedImage[4];
		animations_left = new BufferedImage[4];
		
		if (typeEnemy == EnumEnemies.globin) {
			
			// y 16 esquerda e 32 direita 
			for (int i = 0; i <= 3; i++) {
				animations_right[i] = Game.DUNGEON_SPRITE.getSprite((16*i),32, 16, 16);
				animations_left[i] = Game.DUNGEON_SPRITE.getSprite((16*i), 16, 16, 16);
			}
		}
		else if (typeEnemy == EnumEnemies.smile) {
			//System.out.print("smile");
			// y 48 esquerda e 64 direita			
			for (int i = 0; i <= 3; i++) {
				animations_left[i] = Game.DUNGEON_SPRITE.getSprite((16 * i), 48, 16, 16);
				animations_right[i] = Game.DUNGEON_SPRITE.getSprite((16 * i), 64, 16, 16);
			}
		}
		else if (typeEnemy == EnumEnemies.soldier) {
			//System.out.print("soldier");
			// y 80 esquerda e 96 direita
			for (int i = 0; i <= 3; i++) {
				animations_right[i] = Game.DUNGEON_SPRITE.getSprite((16 * i), 96, 16, 16);
				animations_left[i] = Game.DUNGEON_SPRITE.getSprite((16 * i), 80, 16, 16);
			}
		}
		 
		 
		 
/*		for (int i = 0; i < 3; i++) {
			animations[i] = Game.ENEMY_SPRITE.getSprite((4*16) + (16 * i), 16, 16, 16);
		}*/
		
	}
	
	
	public void render(Graphics g) {	
		if (isright) {
			g.drawImage(animations_right[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(animations_left[index], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}			
	}
	
	public void tick() {
		depth = 0;
		/* if (this.calculateDistance(this.getX(), this.getY(),Game.player.getX(), Game.player.getY()) < 200) {
			if (!isCollingWithPlayer()) {
				if (Game.rand.nextInt(100) < 50) {
					if ((int)x < Game.player.getX() && !IsColliding((int)(x+speed), this.getY()) && World.isFree((int)(x+speed),(int)y)) {
						x+=speed;
					}
					else if ((int)x > Game.player.getX() && !IsColliding((int)(x-speed), this.getY()) && World.isFree((int)(x-speed),(int)y)) {
						x-=speed;
					}
					
					else if ((int)y < Game.player.getY() && !IsColliding(this.getX(), (int) (y+speed)) && World.isFree((int)x,(int)(y+speed))) {
						y+=speed;
					}
					
					else if ((int)y > Game.player.getY() && !IsColliding(this.getX(), (int) (y-speed)) && World.isFree((int)x,(int)(y-speed))) {
						y-=speed;
					}
				}
			} else {
				
				if (Game.rand.nextInt(100) < 10) {
					Game.player.life--;	
					Game.player.isDamage = true;
				}
			}
		}*/

		if (!isCollingWithPlayer()) {
			if (path == null || path.size() == 0) {
				Vector2I start = new Vector2I((int)(x/16), (int)(y/16));
				Vector2I end = new Vector2I((int)(Game.player.x/16),(int) (Game.player.y/16));
				path = AStart.findPath(Game.world, start, end);
			}
		}
		else  {
			if (Game.rand.nextInt(100) < 10) {
				/*Sound.hurt.Play();
				Game.player.life--;	
				Game.player.isDamage = true; */
			}
		}
		
		FollowPath(path);
		
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;

			if (index > maxIndex) {
				index = 0;
			}
		}
		
				
		IsCollidingWithBulletShoot();
		
		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 30) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
	}
	
	public boolean IsColliding(int xnext, int ynext) {
		Rectangle enemycurrent = new Rectangle(xnext, ynext,World.TILE_SIZE, World.TILE_SIZE);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e  = (Enemy) Game.enemies.get(i);
			if (e == this) continue;

			Rectangle targetcurrent = new Rectangle(e.getX(),e.getY(),World.TILE_SIZE, World.TILE_SIZE);
			
			if (enemycurrent.intersects(targetcurrent)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCollingWithPlayer() {
		Rectangle enemycurrent = new Rectangle(this.getX(), this.getY(),World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY() ,World.TILE_SIZE, World.TILE_SIZE);
		
		return enemycurrent.intersects(player);
	}
	
	public void IsCollidingWithBulletShoot() {
		Rectangle enemycurrent = new Rectangle(this.getX(), this.getY(),World.TILE_SIZE, World.TILE_SIZE);
		for (int i = 0; i < Game.shots.size(); i++) {
			BulletShot e  = Game.shots.get(i);
			Rectangle targetcurrent = new Rectangle(e.getX(),e.getY(),World.TILE_SIZE, World.TILE_SIZE);
			
			if (enemycurrent.intersects(targetcurrent) && !Game.toDelete.contains(e)) {
				isDamaged = true;
				life--;

				System.out.println("inimigo perdendo vida..");
				Game.shots.remove(e);
				if (life <= 0) {
					System.out.println("Morreu");
					Game.toDelete.add(this);
					Game.enemies.remove(this);
					break;
				}
			}
		}
	}
}
