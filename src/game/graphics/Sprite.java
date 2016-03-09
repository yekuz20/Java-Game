package game.graphics;

import game.entity.mob.Player;
import game.level.tile.Tile;

public class Sprite {
	
	public final int SIZE;
	private int x, y;
	public int[] pixels;
	public SpriteSheet sheet;
	private final int FADED_VALUE = 2;
	public Sprite faded;
	
	public static Sprite blueBricks = new Sprite(Tile.SIZE, 0, 0, SpriteSheet.tiles, false);
	public static Sprite greenBricks = new Sprite(Tile.SIZE, 0, 1, SpriteSheet.tiles, false);
	
	public static Sprite pipe = new Sprite(Tile.SIZE, 0, 2, SpriteSheet.tiles, false);
	public static Sprite metal = new Sprite(Tile.SIZE, 0, 3, SpriteSheet.tiles, false);
	
	public static Sprite voidTile = new Sprite(Tile.SIZE, 10, 10, SpriteSheet.tiles, false);
	
	public static Sprite player = new Sprite(Player.SIZE, 2, 0, SpriteSheet.tiles, false);
	
	public static Sprite basicEnemy = new Sprite(Player.SIZE, 2, 2, SpriteSheet.tiles, false);
	
	public Sprite(int size, int x, int y, SpriteSheet sheet, boolean faded) {
		SIZE = size;
		pixels = new int[size * size];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		this.load(faded);
		if (!faded) {
			this.faded = new Sprite(size, x, y, sheet, true);
		}
		
	}
	
	private void load(boolean faded) {
		if (!faded) {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					pixels[x+y*SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
				}
			}
		}
		else {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					int color = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE] - 0xff000000;
					if (color != 0xff00ff) {
						int b = (color - (color / 256) * 256) / FADED_VALUE;
						color = color / 256;
						
						int g = (color - (color / 256) * 256) / FADED_VALUE;
						color = color / 256;
						
						int r = (color - (color / 256) * 256) / FADED_VALUE;
					
					pixels[x+y*SIZE] = 0xff000000 + r * 256 * 256 + g * 256 + b;
					}
					else {
						pixels[x+y*SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
					}
				}
			}
		}
	}
	
	
}
