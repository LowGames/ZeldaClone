package com.olasoumarcus.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.olasoumarcus.main.Game;
import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.World;

public class Enemy extends GameObject {

	private double speed = 0.4;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 2;
	private BufferedImage[] animations;
	private int life = 3;
	private boolean isDamaged = false;
	private int damageFrames = 0;
	
	public Enemy(double x, double y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		 animations = new BufferedImage[3];
		
		for (int i = 0; i < 3; i++) {
			animations[i] = Game.ENEMY_SPRITE.getSprite((4*16) + (16 * i), 16, 16, 16);
		}
	}
	
	
	public void render(Graphics g) {
		if (isDamaged) {
			g.drawImage(GameObject.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(animations[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		}
	}
	
	public void tick() {
		if (this.calculateDistance(this.getX(), this.getY(),Game.player.getX(), Game.player.getY()) < 200) {
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
					
					
					frames++;
					if (frames == maxFrames) {
						frames = 0;
						index++;

						if (index > maxIndex) {
							index = 0;
						}
					}
				}
			} else {
				
				if (Game.rand.nextInt(100) < 10) {
					Game.player.life--;	
					Game.player.isDamage = true;
				}
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
