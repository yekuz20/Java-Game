package game.level.tile;

import game.graphics.Screen;
import game.graphics.Sprite;

public class PartialBackgroundTile extends PartialTile {
	
	public PartialBackgroundTile(Sprite sprite, int x0, int x1, int y0, int y1) {
		super(sprite, x0, x1, y0, y1);
	}
	
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x * Tile.SIZE, y * Tile.SIZE, this);
	}
	
	public boolean solid() {
		return false;
	}
	
}
