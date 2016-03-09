package game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	protected int[] blueTiles;
	protected int[] greenTiles;
	protected int[] specialTiles;
	public static int[] blueMapDetails;
	public static int[] greenMapDetails;
	public static int playerX = 8 * 16;
	public static int playerY = 8 * 16;
	public static int endX = 1000 * 16;
	
	public static boolean whichLevel = true; // true = blue, false = red;
	
	public Level(String path1, String path2) {
		loadLevel(path1, path2);
		levelWidth = width;
		levelHeight = height;
	}
	
	protected void generateLevel() {
		
	}
	
	
	public void update(Player player) {
		if (player.x >= endX) {
			loadLevel("/aLevelBlue.png", "/aLevelRed.png");
			player.x = playerX;
			player.y = playerY;
		}
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
			switch (blueTiles[x + y * width]) {
	 		case 0xff000000: return Tile.blueBricks;
	 		}
			switch (greenTiles[x + y * width]) {
	 		case 0xff000000: return Tile.greenBricks.faded;
	 		}
		}
		else {
			switch (greenTiles[x + y * width]) {
			case 0xff000000: return Tile.greenBricks;
	 		}
			switch (blueTiles[x + y * width]) {
	 		case 0xff000000: return Tile.blueBricks.faded;
	 		}
		}
		return Tile.voidTile;
	}
	
	public PartialTile getPartialTile(int x, int y, boolean whichLevel) {
		if (!(x < 0 || y < 0  || x >= width || y >= height)) {
			if (whichLevel) {
				switch (blueTiles[x + y * width]) {
		 		}
				switch (greenTiles[x + y * width]) {
		 		}
			}
			else {
				switch (greenTiles[x + y * width]) {
				case 0xff000001: return PartialTile.pipe;
				case 0xff000002: return PartialTile.metal;
		 		}
				switch (blueTiles[x + y * width]) {
		 		}
			}
		}
		return PartialTile.voidTile;
	}
	
	public void renderTile(int x, int y, boolean whichLevel, Screen screen) { // true = blue, false = green;
		if (!(x < 0 || y < 0  || x >= width || y >= height)) {
			Tile.voidTile.render(x, y, screen);
			if (whichLevel) {
				switch (greenTiles[x + y * width]) {
		 		case 0xff000000: Tile.greenBricks.faded.render(x, y, screen); break;
		 		case 0xff000001: Tile.pipe.faded.render(x, y, screen); break;
				case 0xff000002: Tile.metal.faded.render(x, y, screen); break;
		 		}
				switch (blueTiles[x + y * width]) {
		 		case 0xff000000: Tile.blueBricks.render(x, y, screen); break;
		 		}
			}
			else {
				switch (blueTiles[x + y * width]) {
		 		case 0xff000000: Tile.blueBricks.faded.render(x, y, screen); break;
		 		}
				switch (greenTiles[x + y * width]) {
				case 0xff000000: Tile.greenBricks.render(x, y, screen); break;
				case 0xff000001: Tile.pipe.render(x, y, screen); break;
				case 0xff000002: Tile.metal.render(x, y, screen); break;
		 		}
			}
		}
	}
	
	
	public void loadLevel(String path1, String path2) {
		Game.basicEnemies = new BasicEnemy[100];
		endX = 1000 * 16;
		try {
			BufferedImage image1 = ImageIO.read(Level.class.getResource(path1));
			BufferedImage image2 = ImageIO.read(Level.class.getResource(path2));
			int w = image1.getWidth();
			int h = image1.getHeight();
			width = w;
			height = h;
			blueTiles = new int[w * h];
			greenTiles = new int[w * h];
			blueMapDetails = new int[w * h];
			greenMapDetails = new int[w * h];
			specialTiles = new int[w * h];
			tiles = new int[w * h];
			image1.getRGB(0, 0, w, h, tiles, 0, w);
			for (int i = 0; i < tiles.length; i++) {
				if (tiles[i] == 0xffaaaaaa) {
					playerX = (i % width) * Tile.SIZE;
					playerY = i / width * Tile.SIZE;
				}
				if (tiles[i] == 0xffbbbbbb) {
					endX = (i % width) * Tile.SIZE;
				}
				if (tiles[i] == 0xffcccc01) {
					Game.createBasicEnemy((i % width) * Tile.SIZE, i / width * Tile.SIZE, 3, true);
				}
				blueTiles[i] = tiles[i];
			}
			image2.getRGB(0, 0, w, h, tiles, 0, w);
			for (int i = 0; i < tiles.length; i++) {
				greenTiles[i] = tiles[i];
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Level files could not load.");
		}
	}
}
