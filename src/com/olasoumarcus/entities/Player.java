package com.olasoumarcus.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

	public boolean right,up,left,down;
	private int speed = 4;
	public int dir = 3;
	private int right_dir = 0, left_dir = 1, top_dir =2, down_dir = 3;
	private BufferedImage[] topPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightPlayer;
	private int frames = 0, maxFrames =5, index =0, maxIndex =2;
	private boolean moved;
	
	// walker down row 0
	// walker up row 1
	// walker right row 2
	// walker left row 3
	
	//cada row 19 y
	//cada column 20 x
	
	
	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);
		
		topPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		rightPlayer = new BufferedImage[3];
		
		for (int i = 0; i < 3; i++) {
			downPlayer[i] = this.sprite.getSprite(0 +(20*i) ,1, 20, 20);
		}
		
		for (int i = 0; i < 3; i++) {
			topPlayer[i] = this.sprite.getSprite((20*i) ,20, 20, 20);
		}
		
		for (int i = 0; i < 3; i++) {
			leftPlayer[i] = this.sprite.getSprite((20*i) ,40, 20, 20);
		}
		
		for (int i = 0; i < 3; i++) {
			rightPlayer[i] = this.sprite.getSprite((20*i) ,60, 20, 20);
		}
	}
	
	public void render(Graphics g) {
		if (dir == top_dir) {
			g.drawImage(topPlayer[index], this.getX(), this.getY(), null);	
		}
		else if (dir == down_dir) {
			g.drawImage(downPlayer[index], this.getX(), this.getY(), null);
		}
		
		if (dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);	
		}
		else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
		}
		
	}
	
	public void tick() {
		moved = false;
		if (right) {
			moved =true;
			this.x += speed;
			dir = right_dir;
		}
		
		if (left) {
			moved =true;
			this.x -= speed;
			dir = left_dir;
		}
		
		if (up) {
			moved =true;
			dir = top_dir;
			this.y -= speed;
		}
		else if (down) {
			moved =true;
			dir = down_dir;
			this.y += speed;
		}
		
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
	}
}
