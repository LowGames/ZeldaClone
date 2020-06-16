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
	private int speed = 4;
	public int dir = 3;
	private int right_dir = 0, left_dir = 1, top_dir = 2, down_dir = 3;
	private BufferedImage[] topPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightPlayer;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;
	private boolean moved;
	public double life = 100;
	public static final int MAXLIFE = 100;

	// walker down row 0
	// walker up row 1
	// walker right row 2
	// walker left row 3

	// cada row 19 y
	// cada column 20 x

	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);

		topPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		rightPlayer = new BufferedImage[3];

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
		if (dir == top_dir) {
			g.drawImage(topPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == down_dir) {
			g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		if (dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

	}

	public void tick() {
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
		
		Camera.x  = this.getX() - (Game.WIDTH/2);
		Camera.y  = this.getY() - (Game.HEIGHT/2);
		
		checkCollisionLifePack();
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
