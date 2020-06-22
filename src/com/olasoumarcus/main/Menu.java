package com.olasoumarcus.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.olasoumarcus.graphics.SpriteSheet;
import com.olasoumarcus.world.World;

public class Menu {

	public String firstOption = "novo jogo";
	public String[] options = {firstOption, "carregar jogo", "sair"};
	public int currentOption = 0;
	public int maxOptions = options.length - 1;
	public boolean up = false, down = false, enter = false;
	public BufferedImage background;
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	public static boolean pause = false;

	public Menu() {
       background = new SpriteSheet("/sprites/gfx/fundoMenu.jpg").getSprite(0, 0, 1080, 720);
	}
	
	
	public void tick() {
		File file = new File("/save.txt");
		if (file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
		
		if (up) {
			currentOption--;
			if (currentOption < 0) {
				currentOption = 0;
			}
			up = false;
		}
		
		if (down) {
			currentOption++;
			if (currentOption > maxOptions) {
				currentOption = 0;
			}
			down = false;
		}
		
		if (enter) {
			System.out.print(options[currentOption]);
			if (options[currentOption] == "novo jogo") {
				Game.state = "normal";
				file = new File("save.txt");
				file.delete();
			}
			
			else if (options[currentOption] == "continuar") {
				Game.state = "normal";
				firstOption = "novo jogo";
				pause = false;
				file = new File("save.txt");
				file.delete();
			}
			
			else if (options[currentOption] == "carregar jogo") {
				file = new File("save.txt");
				if (file.exists()) {
					System.out.print("existe arquivo");
					String saver = loadGame(10);
					applySave(saver);
				}
			}

			enter = false;
			
			if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
		
		if (pause) {
			firstOption = "continuar";
			options[0] = firstOption;
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
    	g2.setColor(Color.BLACK);
    	g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
    	g.drawImage(background, 0, 0, null);
    	g2.setColor(Color.white);
    	g.setFont(new Font("arial", Font.BOLD,28));
    	g2.drawString(">Curso na DankiCode<", (Game.WIDTH * Game.SCALE)/2  - 110 , (Game.HEIGHT * Game.SCALE) /2 - 30);
    	g.setFont(new Font("arial", Font.BOLD,20));
    	int heightcurrent = (Game.HEIGHT * Game.SCALE) /2;
    	for (String option : options) {
    		g2.drawString(option, (Game.WIDTH * Game.SCALE)/2  - 35 , heightcurrent);
    		    		
    		if (options[currentOption] == option) {
    			g2.drawString(">", (Game.WIDTH * Game.SCALE)/2  - 50 , heightcurrent);
    		}
    		
    		heightcurrent+=30;
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader(file));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] contents =  singleLine.split(":");
						char[] val = contents[1].toCharArray();
						contents[1] = "";
						
						for (int i = 0; i < val.length; i++) {
							val[i]-=encode;
							contents[1] += val[i]; 
						}
						line+=contents[0];
						line+=":";
						line+=contents[1];
						line+="/";
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return line;
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] result = spl[i].split(":");
			if (result[0] == "level") {
				String newWorld = "mapLevel"+result[1]+".png";
				World.restartGame(newWorld);
				Game.state = "normal";
				pause = true;
			}
			
			if (result[0] == "life") {
				Game.player.life = Integer.parseInt(result[1]);
			}
		}
		
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		for (int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value =  Integer.toString(val2[i]).toCharArray();
			
			for (int j = 0; j < value.length; j++) {
				value[j]+=encode;
				current+=value[j];
			}
			
			try {
				write.write(current);
				write.newLine();
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		
		try {
			write.flush();
			write.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
