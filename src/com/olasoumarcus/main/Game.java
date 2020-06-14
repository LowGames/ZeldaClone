package com.olasoumarcus.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.olasoumarcus.entities.GameObject;
import com.olasoumarcus.entities.Player;
import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.world.World;

import jdk.jshell.spi.SPIResolutionException;

public class Game extends Canvas implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private final int WIDTH = 160;
	private final int HEIGHT = 120;
	private final int SCALE = 4;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image;
	
	public List<GameObject> gameObjects;
	private Player player;
	public static World world;

	public Game() {
		world = new World("/sprites/gfx/Map.png");
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		gameObjects = new ArrayList<GameObject>();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		player = new Player(80,80,100,100);
		gameObjects.add(player);
		requestFocus();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			isRunning = false;
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initFrame() {
		frame = new JFrame();
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle("Curso de desenvolvimento de jogos");
	}
	
	public void tick() {
		for (GameObject gameObject : gameObjects) {
			gameObject.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
		   this.createBufferStrategy(3);
		   return;
		}

		Graphics g = image.getGraphics();
		
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		
		world.render(g);
		for (GameObject gameObject : gameObjects) {
			gameObject.render(g);
		}

		bs.show();
	}	

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer+=1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();

		if (keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_D) {
			player.right = true;
		}
		else if (keycode == KeyEvent.VK_LEFT || keycode == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W) {
			player.up= true;
		}
		else if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
			player.down = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode = e.getKeyCode();

		if (keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_D) {
			player.right = false;
		}
		else if (keycode == KeyEvent.VK_LEFT || keycode == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W) {
			player.up= false;
		}
		else if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

}
