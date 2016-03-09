package game.level.tile;

import game.graphics.Screen;
import game.graphics.Sprite;

public class Tile {
	public int x, y;
	public Sprite sprite;
	public Tile faded;
	
	public static final int SIZE = 16;
	
	public static Tile blueBricks = new SolidTile(Sprite.blueBricks, true);
	public static Tile greenBricks = new SolidTile(Sprite.greenBricks, true);
	
	public static Tile pipe = new SolidTile(Sprite.pipe, true);
	public static Tile metal = new SolidTile(Sprite.metal, true);
	
//	public static Tile fadedBlueBricks = new BackgroundTile(Sprite.blueBricks.faded);
//	public static Tile fadedGreenBricks = new BackgroundTile(Sprite.greenBricks.faded);
//	
//	public static Tile fadedPipe = new BackgroundTile(Sprite.pipe.faded);
//	public static Tile fadedMetal = new BackgroundTile(Sprite.metal.faded);
	
	public static Tile voidTile = new voidtile(Sprite.voidTile, true);
	
	
	public Tile(Sprite sprite, boolean faded) {
		this.sprite = sprite;
		if (faded) {
			this.faded = new BackgroundTile(sprite.faded, false);
		}
	}
	
	public void render(int x, int y, Screen screen) {
		
	}
	
	public boolean solid() {
		return false;
	}
}
