package game.level.tile;

import game.graphics.Screen;
import game.graphics.Sprite;

public class PartialTile {
	public int x, y, x0, x1, y0, y1;
	public Sprite sprite;
	
	public static PartialTile pipe = new PartialSolidTile(Sprite.pipe, 1, 13, 0, 16);
	public static PartialTile metal = new PartialSolidTile(Sprite.metal, 0, 16, 0, 7);
	
	public static PartialTile voidTile = new PartialBackgroundTile(Sprite.voidTile, 0, 15, 0 , 15);
	
	
	public PartialTile(Sprite sprite, int x0, int x1, int y0, int y1) {
		this.sprite = sprite;
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}
	
	public void render(int x, int y, Screen screen) {
		
	}
	
	public boolean solid() {
		return false;
	}
}
