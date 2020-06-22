package com.olasoumarcus.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.olasoumarcus.main.Game;
import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.World;

public class Player extends GameObject {

	public boolean right, up, left, down;
	private int speed = 2;
	public int dir = 3;
	private int right_dir = 0, left_dir = 1, top_dir = 2, down_dir = 3;
	private BufferedImage[] topPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightPlayer;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2 , jumpFrames = 50, jumpCur = 0;
	private boolean moved;
	public double life = 1;
	public int ammo = 1000;
	public static final int MAXLIFE = 100;
	private BufferedImage playerDamage;
	public boolean isDamage = false;
	private int damageFrames = 0;
	private boolean hasGun;
	public static boolean shoot = false, mouseshoot = false , jump = false , isjumping = false , jumpup = false, jumpdown=false;
	public int mx, my;
	public int eixoz = 0;
	// walker down row 0
	// walker up row 1
	// walker right row 2
	// walker left row 3

	// cada row 19 y
	// cada column 20 x

	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);
		life = 100;
		topPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		rightPlayer = new BufferedImage[3];
		
		playerDamage = Game.PLAYER_SPRITE.getSprite(20, 40, 20, 20);

		for (int i = 0; i < 3; i++) {
			downPlayer[i] = Game.PLAYER_SPRITE.getSprite(0 + (20 * i), 1, 20, 20);
		}

		for (int i = 0; i < 3; i++) {
			topPlayer[i] = Game.PLAYER_SPRITE.getSprite((20 * i), 20, 20, 20);
		}

		for (int i = 0; i < 3; i++) {
			leftPlayer[i] = Game.PLAYER_SPRITE.getSprite((20 * i), 40, 20, 20);
		}

		for (int i = 0; i < 3; i++) {
			rightPlayer[i] = Game.PLAYER_SPRITE.getSprite((20 * i), 60, 20, 20);
		}
	}

	public void render(Graphics g) {
		if (!isDamage) {
			if (dir == top_dir) {
				g.drawImage(topPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - eixoz, null);
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - eixoz, null);
			}

			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - eixoz , null);
				if (hasGun) {
					g.drawImage(Game.SWORD_SPRITE.getSprite(0, 0, 16, 16), this.getX() - Camera.x + 11, this.getY() - Camera.y + 3 - eixoz, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - eixoz, null);
				if (hasGun) {
					g.drawImage(Game.SWORD_SPRITE.getSprite(16, 0, 16, 16), this.getX() - Camera.x - 14 , this.getY() - Camera.y + 3 - eixoz, null);
				}
			}			
		} else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - eixoz, null);
		}
	}

	public void tick() {
		if (jump) {
			System.out.print("pulou");
			if (isjumping == false) {
				jump = false;
				isjumping = true;
				jumpup = true;
			}
		}
		
		if (isjumping == true) {
			System.out.println("jumpinggg");
				if (jumpup) {
					jumpCur++;
				}
				
				else if (jumpdown) {
					jumpCur--;
					
					if (jumpCur <= 0) {
						isjumping = false;
						jumpdown = false;
						jumpup = true;
					}
				}
				
				eixoz = jumpCur;
				if (jumpCur >= jumpFrames) {
					jumpup= false;
					jumpdown= true;
				}
	    }
		
		moved = false;
		if (right && World.isFree((int)x+speed, (int)y)) {
			moved = true;
			this.x += speed;
			dir = right_dir;
		}

		if (left && World.isFree((int)x-speed, (int)y)) {
			moved = true;
			this.x -= speed;
			dir = left_dir;
		}

		if (up && World.isFree((int)x, (int)y+speed)) {
			moved = true;
			dir = top_dir;
			this.y -= speed;
		} else if (down && World.isFree((int)x, (int)y-speed)) {
			moved = true;
			dir = down_dir;
			this.y += speed;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;

				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if (isDamage) {
			this.damageFrames++;
			if (this.damageFrames == 30) {
				this.damageFrames = 0;
				isDamage = false;
			}
		}

		if (life <= 0) {
			life = 0;
			Game.state = "gameover";
		    return;
		}
		
		if (shoot && hasGun && ammo > 0) {
			ammo--;
			shoot = false;
			System.out.println("Atirando...");
			int dx = 0;
			int px = 0;
			int py = 8;
			if (dir == right_dir) {
				px = 16;
				dx = 1;
			} else {
				px = 4;
				dx = -1;
			}
			
			BulletShot shot = new BulletShot(this.getX() + px, this.getY() + py, 3, 3, dx, 0);
			Game.shots.add(shot);
		}
		
		if (mouseshoot && hasGun && ammo > 0) {
			ammo--;
			mouseshoot = false;
			System.out.println("Atirando...");

			// Recuperar o ângulo
			double angle = Math.toDegrees(Math.atan2(my - this.getY() - Camera.y, mx - (this.getX() - Camera.x)));
			System.out.println("ângulo:"+ angle);
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			int px = 8;
			int py = 8;
			
			BulletShot shot = new BulletShot(this.getX() + px, this.getY() + py, Game.SCALE, Game.SCALE, dx, dy);
			Game.shots.add(shot);
		}
		
		checkCollisionLifePack();
		checkCollisionBullet();
		checkCollisionWeapon();
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.gameObjects.size(); i++) {
			GameObject gm = Game.gameObjects.get(i);
			if (gm == this) continue;
			
			if (gm instanceof LifePack) {
			   if (IsColliding(gm.getX(), gm.getY())) {
				   System.out.println("Aumentou life");
				   life += 10;
				   
				   if (life > 100) {
					   life = 100;  
				   }
				   Game.toDelete.add(gm);
			   }		
			}
		}
	}
	
	public void checkCollisionBullet() {
		for (int i = 0; i < Game.gameObjects.size(); i++) {
			GameObject gm = Game.gameObjects.get(i);
			if (gm == this) continue;
			
			if (gm instanceof Bullet) {
			   if (IsColliding(gm.getX(), gm.getY())) {
				   System.out.println("Aumentou bullet");
				   ammo += 10;				   
				   Game.toDelete.add(gm);
			   }		
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for (int i = 0; i < Game.gameObjects.size(); i++) {
			GameObject gm = Game.gameObjects.get(i);
			if (gm == this) continue;
			
			if (gm instanceof Weapon) {
			   if (IsColliding(gm.getX(), gm.getY())) {
				   System.out.println("Pegou a arma");
				   hasGun = true;
				   Game.toDelete.add(gm);
			   }		
			}
		}
	}
	
	public boolean IsColliding(int xnext, int ynext) {
		Rectangle targetcurrent = new Rectangle(xnext, ynext,World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(this.getX(), this.getY(),World.TILE_SIZE, World.TILE_SIZE);

		return player.intersects(targetcurrent);
	}
}
