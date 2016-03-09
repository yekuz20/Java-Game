package game.level.tile;

import game.graphics.Screen;
import game.graphics.Sprite;

public class BackgroundTile extends Tile {
	
	public BackgroundTile(Sprite sprite, boolean faded) {
		super(sprite, faded);
	}
	
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x * Tile.SIZE, y * Tile.SIZE, this);
	}
	
	public boolean solid() {
		return false;
	}
	
}
