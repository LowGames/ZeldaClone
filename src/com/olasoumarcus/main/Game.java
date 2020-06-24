package com.olasoumarcus.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.olasoumarcus.entities.BulletShot;
import com.olasoumarcus.entities.GameObject;
import com.olasoumarcus.entities.Player;
import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.graphics.UI;
import com.olasoumarcus.world.Camera;
import com.olasoumarcus.world.World;

import jdk.jshell.spi.SPIResolutionException;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static final int WIDTH = 360;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image;
	public static BufferedImage minimap;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixel.ttf");
	public static Font font;
	
	public static List<GameObject> gameObjects;
	public static List<GameObject> enemies;
	public static List<BulletShot> shots;
	public static List<GameObject> toDelete;
	public static Player player;
	public static World world;
	public static SpriteSheet OBJECT_SPRITES;
	public static SpriteSheet ENEMY_SPRITE;
	public static SpriteSheet DUNGEON_SPRITE;
	public static SpriteSheet PLAYER_SPRITE;
	public static SpriteSheet WEAPON_SPRITE;
	public static Random rand;
	public static UI ui;
	public static Menu menu;
	public static int CUR_LEVEL = 1, MAX_LEVEL = 2;
	public static String state = "menu";
	private int maxRender = 30, render =0;
	private boolean restartgame = false, saveGame;
	public int mx, my;
	public int[] pixels;
	public BufferedImage lightmap;
	public int[] lightMapPixels;
	public static int[] miniMapPixels;

	public Game() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(16f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		rand = new Random();
		loadSprites();
		gameObjects = new ArrayList<GameObject>();
		enemies = new ArrayList<GameObject>();
		shots = new ArrayList<BulletShot>();
		toDelete = new ArrayList<GameObject>();
		player = new Player(80,80,100,100);
		gameObjects.add(player);
		world = new World("/sprites/gfx/MapLevel1.png");
		minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		miniMapPixels = ((DataBufferInt) minimap.getRaster().getDataBuffer()).getData();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/sprites/gfx/LightMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth()*lightmap.getHeight()];
		lightmap.getRGB(0,0, lightmap.getWidth(), lightmap.getHeight(),lightMapPixels,0,lightmap.getWidth());
		ui = new UI();
		menu = new Menu();
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
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
		if (Game.state == "normal") {
			if (this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {CUR_LEVEL};
				Menu.saveGame(opt1, opt2, 10);
				System.out.print("jogo salvo");
			}
			
			for (GameObject gameObject : gameObjects) {
				gameObject.tick();
			}
			
			for (int i = 0; i < shots.size(); i++) {
				BulletShot shot = shots.get(i);
				shot.tick();
			}
			
			for (GameObject gameObject : toDelete) {
				gameObjects.remove(gameObject);
			}

			toDelete.clear();
			
			if (enemies.isEmpty()) {
				System.out.println("Passou de Fase..");
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "mapLevel"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		} else if (state == "menu") {
			menu.tick();
		}
		
		if (restartgame) {
			Game.state = "normal";
			CUR_LEVEL = 1;
			String newWorld = "mapLevel"+CUR_LEVEL+".png";
			World.restartGame(newWorld);
			restartgame = false;
		}
		
	}
	
	public void applyLight() {
	 /*	for (int x = 0; x < Game.WIDTH; x++) {
			for (int y = 0; y < Game.HEIGHT; y++) {
				int index = x +(y* Game.WIDTH);
				if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT || index < 0)
					continue;

				if (lightMapPixels[index] != null && lightMapPixels[index] == 0xffffffff) {
					pixels[x +(y* Game.WIDTH)] = 0;
				}
			}
		}
		*/
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

		world.render(g);
		Collections.sort(gameObjects, GameObject.depthSorter);
		for (GameObject gameObject : gameObjects) {
			gameObject.render(g);
		}
		
		for (int i = 0; i < shots.size(); i++) {
			BulletShot shot = shots.get(i);
			shot.render(g);
		}

		applyLight();
		ui.render(g);
	    g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
	    if (Game.state == "gameover") {
	    	Graphics2D g2 = (Graphics2D) g;
	    	g2.setColor(new Color(0,0,0,100));
	    	g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("arial", Font.BOLD,28));
	    	g.drawString("Game Over", (WIDTH * SCALE) / 2 - 30, (HEIGHT * SCALE) / 2);
	    	g.setFont(new Font("arial", Font.BOLD,18));
	    	
	    	if (render < maxRender) {
	    		render++;
		    	g.setColor(Color.green);
	    	}
	    	
	    	if (render >= maxRender) {
	    		render = 0;
	    		g.setColor(Color.white);
	    	}

	    	g.drawString("> Pressione Enter pra reiniciar", (WIDTH * SCALE) / 2 - 80, (HEIGHT * SCALE) / 2 + 40);
	    	
	    } else if (Game.state == "menu") {
	    	menu.render(g);
	    }
	    
	    // Criando rotação de objetos.
	    /*
	    Graphics2D g2 = (Graphics2D) g;
	    double angleMouse = Math.atan2(225-my,225-mx);
	    g2.rotate(angleMouse, 200+25,200+25);
	    g.setColor(Color.red);
	    g.fillRect(200, 200, 50, 50);
	    */
	    
	    World.renderMiniMap();
	    g.drawImage(minimap, 900,600, World.WIDTH*5, World.HEIGHT*5, null);
		bs.show();
	}
	
	private void loadSprites() {
		OBJECT_SPRITES = new SpriteSheet("/sprites/gfx/objects.png");
		ENEMY_SPRITE = new SpriteSheet("/sprites/gfx/Enemy.png");
		PLAYER_SPRITE = new SpriteSheet("/sprites/gfx/Player.png");
		WEAPON_SPRITE = new SpriteSheet("/sprites/gfx/weaponspritesheet.png");
		DUNGEON_SPRITE = new SpriteSheet("/sprites/gfx/DungeonSpriteSheet.png");
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
				//System.out.println("FPS:" + frames);
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
		
		if (keycode == KeyEvent.VK_SPACE) {
			player.shoot = true;
		}
		
		if (keycode == KeyEvent.VK_ENTER && Game.state == "gameover")
		{
			this.restartgame = true;
		}
		
		if (keycode == KeyEvent.VK_ENTER && Game.state == "menu") {
			menu.enter = true;
		}
		
		if (keycode == KeyEvent.VK_ESCAPE) {
			Game.state = "menu";
			menu.pause = true;
			
		}
		
		if (keycode == KeyEvent.VK_Q && Game.state == "normal") {
			this.saveGame = true;
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
			
			if (Game.state == "menu") {
				menu.up = true;
			}
		}
		else if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
			player.down = false;
			
			if (Game.state == "menu") {
				menu.down = true;
			}
		}
		
		if (keycode == KeyEvent.VK_SPACE) {
			player.shoot = false;
		}
		
		if (keycode == KeyEvent.VK_P) {
			player.jump = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseshoot = true;
		
		// pegando eventos do mouse
		player.mx = (e.getX() / SCALE);
		player.my = (e.getY() / SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		this.mx = e.getX();
		this.my = e.getY();
	}

}
