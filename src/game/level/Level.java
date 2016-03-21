package game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import java.io.*;

import game.Game;
import game.entity.mob.BasicEnemy;
import game.entity.mob.Player;
import game.graphics.Screen;
import game.level.tile.PartialTile;
import game.level.tile.Tile;

public class Level {
	protected int width, height;
	public static int levelWidth;
	public static int levelHeight;
	private int[] tiles;
	protected int[] tiles_a;
	protected int[] tiles_b;
	protected int[] specialTiles;
	public static int[] blueMapDetails;
	public static int[] greenMapDetails;
	public static int playerX;
	public static int playerY;
	public static int endX1 = 1000 * 16;
	public static int endX2 = 2000 * 16;
	public static int endY1 = 0 * 16;
	public static int endY2 = 1000 * 16;
	public static int backX1 = 1000 * 16;
	public static int backX2 = 2000 * 16;
	public static int backY1 = 0 * 16;
	public static int backY2 = 1000 * 16;
	public static Exit[] exits;
	
	public static boolean whichLevel = true; // true = blue, false = red;
	
	public Level(String path1, String path2) {
		loadLevel(1);
		levelWidth = width;
		levelHeight = height;
	}
	
	protected void generateLevel() {
		
	}
	
	
	public void render(int xScroll, Screen screen) {
		
		screen.setOfsset(xScroll);
		int x0 = (xScroll) / Tile.SIZE - 6 ;
		int x1 = (xScroll + screen.width + Tile.SIZE) / Tile.SIZE - 6;
		int y0 = 0;
		int y1 = screen.height / Tile.SIZE;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				renderTile(x, y, whichLevel, screen);
			}
		}
	}
	
	public void setPushableTile(int x, int y, int tile) {
		specialTiles[x+y*width] = tile;
	}
	
	public Tile getTile(int x, int y, boolean whichLevel) { // true = blue, false = green;
		if (x < 0 || y < 0  || x >= width || y >= height) return Tile.voidTile;
		
		
		if (whichLevel) {
			switch (tiles_a[x + y * width]) {
	 		case 0xff000000: return Tile.blueBricks;
	 		}
			switch (tiles_b[x + y * width]) {
	 		case 0xff000000: return Tile.greenBricks.faded;
	 		}
		}
		else {
			switch (tiles_b[x + y * width]) {
			case 0xff000000: return Tile.greenBricks;
	 		}
			switch (tiles_a[x + y * width]) {
	 		case 0xff000000: return Tile.blueBricks.faded;
	 		}
		}
		return Tile.voidTile;
	}
	
	public PartialTile getPartialTile(int x, int y, boolean whichLevel) {
		if (!(x < 0 || y < 0  || x >= width || y >= height)) {
			if (whichLevel) {
				switch (tiles_a[x + y * width]) {
		 		}
				switch (tiles_b[x + y * width]) {
		 		}
			}
			else {
				switch (tiles_b[x + y * width]) {
				case 0xff000001: return PartialTile.pipe;
				case 0xff000002: return PartialTile.metal;
		 		}
				switch (tiles_a[x + y * width]) {
		 		}
			}
		}
		return PartialTile.voidTile;
	}
	
	public void renderTile(int x, int y, boolean whichLevel, Screen screen) { // true = a, false = b;
		if (!(x < 0 || y < 0  || x >= width || y >= height)) {
			Tile.voidTile.render(x, y, screen);
			if (whichLevel) {
				switch (tiles_b[x + y * width]) {
		 		case 0xff000000: Tile.greenBricks.faded.render(x, y, screen); break;
		 		case 0xff000001: Tile.pipe.faded.render(x, y, screen); break;
				case 0xff000002: Tile.metal.faded.render(x, y, screen); break;
		 		}
				switch (tiles_a[x + y * width]) {
		 		case 0xff000000: Tile.blueBricks.render(x, y, screen); break;
		 		}
			}
			else {
				switch (tiles_a[x + y * width]) {
		 		case 0xff000000: Tile.blueBricks.faded.render(x, y, screen); break;
		 		}
				switch (tiles_b[x + y * width]) {
				case 0xff000000: Tile.greenBricks.render(x, y, screen); break;
				case 0xff000001: Tile.pipe.render(x, y, screen); break;
				case 0xff000002: Tile.metal.render(x, y, screen); break;
		 		}
			}
		}
	}
	
	public void loadLevel(int id) {
		try {
		File file = new File("res/levels/level_" + id + ".xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		int h = Integer.parseInt(document.getElementsByTagName("height").item(0).getTextContent());
		int w = Integer.parseInt(document.getElementsByTagName("width").item(0).getTextContent());
		
		width = w;
		height = h;
		
		String path1 =  document.getElementsByTagName("image1").item(0).getTextContent();
		String path2 =  document.getElementsByTagName("image2").item(0).getTextContent();
		
		BufferedImage image1 = ImageIO.read(Level.class.getResource("/levelImages/" + path1));
		BufferedImage image2 = ImageIO.read(Level.class.getResource("/levelImages/" + path2));
		
		tiles_a = new int[w * h];
		tiles_b = new int[w * h];
		
		image1.getRGB(0, 0, w, h, tiles_a, 0, w);
		image2.getRGB(0, 0, w, h, tiles_b, 0, w);
		
		exits = new Exit[document.getElementsByTagName("exit").getLength()];
		
		for (int i = 0; i < exits.length; i++) {
			NodeList nl = document.getElementsByTagName("exit").item(i).getChildNodes();
			
			exits[i] = new Exit(Integer.parseInt(nl.item(1).getTextContent()) * Tile.SIZE, 
								Integer.parseInt(nl.item(3).getTextContent()) * Tile.SIZE, 
								Integer.parseInt(nl.item(5).getTextContent()) * Tile.SIZE, 
								Integer.parseInt(nl.item(7).getTextContent()) * Tile.SIZE, 
								Integer.parseInt(nl.item(9).getTextContent()),
								Integer.parseInt(nl.item(11).getTextContent()) * Tile.SIZE,
								Integer.parseInt(nl.item(13).getTextContent()) * Tile.SIZE);
		}
		
		Element player = (Element) document.getElementsByTagName("player").item(0);
		
		playerX = Integer.parseInt(player.getAttribute("x")) * Tile.SIZE;
		playerY = Integer.parseInt(player.getAttribute("y")) * Tile.SIZE;
		
		Game.basicEnemies = new BasicEnemy[document.getElementsByTagName("enemy").getLength()];
		
		for (int i = 0; i < Game.basicEnemies.length; i++) {
			NodeList nl = document.getElementsByTagName("enemy").item(i).getChildNodes();
			Game.basicEnemies[i] = new BasicEnemy(Integer.parseInt(nl.item(1).getTextContent()) * Tile.SIZE, 
												  Integer.parseInt(nl.item(3).getTextContent()) * Tile.SIZE, 
												  Integer.parseInt(nl.item(5).getTextContent()), 
												  Boolean.valueOf(nl.item(7).getTextContent()));
		}
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	public void loadLevelOld(String path1, String path2) {
//		endX1 = 1000 * 16;
//		endX2 = 2000 * 16;
//		endY1 = 0 * 16;
//		endY2 = 1000 * 16;
//		backX1 = 1000 * 16;
//		backX2 = 2000 * 16;
//		backY1 = 0 * 16;
//		backY2 = 1000 * 16;
//		boolean hasFoundEnd = false;
//		Game.basicEnemies = new BasicEnemy[100];
//		try {
//			BufferedImage image1 = ImageIO.read(Level.class.getResource(path1));
//			BufferedImage image2 = ImageIO.read(Level.class.getResource(path2));
//			int w = image1.getWidth();
//			int h = image1.getHeight();
//			width = w;
//			height = h;
//			tiles_a = new int[w * h];
//			tiles_b = new int[w * h];
//			blueMapDetails = new int[w * h];
//			greenMapDetails = new int[w * h];
//			specialTiles = new int[w * h];
//			tiles = new int[w * h];
//			image1.getRGB(0, 0, w, h, tiles, 0, w);
//			for (int i = 0; i < tiles.length; i++) {
//				if (tiles[i] == 0xffaaaaaa) {
//					playerX = (i % width) * Tile.SIZE;
//					playerY = i / width * Tile.SIZE;
//				}
//				if (tiles[i] == 0xffbbbbbb) {
//					if (!hasFoundEnd) {
//						endX1 = (i % width) * Tile.SIZE;
//						endY1 = i / width * Tile.SIZE;
//						hasFoundEnd = true;
//					} else {
//						endX2 = (i % width + 1) * Tile.SIZE;
//						if (endX1 > endX2) {
//							endX2 = endX1 + Tile.SIZE;
//							endX1 = (i % width) * Tile.SIZE;
//						}
//						endY2 = (i / width + 1) * Tile.SIZE;
//						if (endY1 > endY2) {
//							endY2 = endY1 + Tile.SIZE;
//							endY1 = i / width * Tile.SIZE;
//						}
//					}
//				}
//				if (tiles[i] == 0xffbbbbbc) {
//					if (!hasFoundEnd) {
//						backX1 = (i % width) * Tile.SIZE;
//						backY1 = i / width * Tile.SIZE;
//						hasFoundEnd = true;
//					} else {
//						backX2 = (i % width + 1) * Tile.SIZE;
//						if (backX1 > backX2) {
//							backX2 = backX1 + Tile.SIZE;
//							backX1 = (i % width) * Tile.SIZE;
//						}
//						backY2 = (i / width + 1) * Tile.SIZE;
//						if (backY1 > backY2) {
//							backY2 = backY1 + Tile.SIZE;
//							backY1 = i / width * Tile.SIZE;
//						}
//					}
//				}
//				if (tiles[i] == 0xffcccc01) {
//					Game.createBasicEnemy((i % width) * Tile.SIZE, i / width * Tile.SIZE, 3, true);
//				}
//				tiles_a[i] = tiles[i];
//			}
//			image2.getRGB(0, 0, w, h, tiles, 0, w);
//			for (int i = 0; i < tiles.length; i++) {
//				if (tiles[i] == 0xffcccc01) {
//					Game.createBasicEnemy((i % width) * Tile.SIZE, i / width * Tile.SIZE, 3, false);
//				}
//				tiles_b[i] = tiles[i];
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Level files could not load.");
//		}
//	}
}
