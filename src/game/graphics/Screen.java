package game.graphics;

import java.util.Random;

import game.Game;
import game.entity.mob.Player;
import game.level.Level;
import game.level.tile.PartialTile;
import game.level.tile.Tile;

public class Screen {
	
	public int width, height;
	public int[] pixels;
	public int[] tiles = new int[64 * 64];
	
	public int xOffset;
	
	private Random random = new Random();
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	
	
	public void renderTile(int xp, int yp, Tile tile) {
		xp -= xOffset;
		
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = yp + y;
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = xp + x;
				if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) { break; }
				xa = (xa < 0) ? 0 : xa;
				if (tile.sprite.pixels[x+y*tile.sprite.SIZE] != 0xffff00ff) {
					pixels[xa+ya*width] = tile.sprite.pixels[x+y*tile.sprite.SIZE];
				}
			}
		}
	}
	
	public void renderPlayer(int xPos, int yPos, Sprite sprite) {
		xPos -= xOffset;
		for (int y = 0; y < Player.SIZE; y++) {
			int yChange = yPos + y;
			for (int x = 0; x < Player.SIZE; x++) {
				int xChange = xPos + x;
				if (xChange < -Player.SIZE || xChange >= width || yChange < 0 || yChange >= height) { break; }
				xChange = (xChange < 0) ? 0 : xChange;
				if (sprite.pixels[x+y*Player.SIZE] != 0xFFFF00FF) {
					pixels[xChange+yChange*width] = sprite.pixels[x+y*Player.SIZE];
				}
			}
		}
	}
	
	public void renderEntity(int xPos, int yPos, Sprite sprite) {
		xPos -= xOffset;
		for (int y = 0; y < sprite.SIZE; y++) {
			int yChange = yPos + y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xChange = xPos + x;
				if (xChange < -sprite.SIZE || xChange >= width || yChange < 0 || yChange >= height) { break; }
				xChange = (xChange < 0) ? 0 : xChange;
				if (sprite.pixels[x+y*sprite.SIZE] != 0xFFFF00FF) {
					pixels[xChange+yChange*width] = sprite.pixels[x+y*Player.SIZE];
				}
			}
		}
	}
	
	public void renderEntityBehindWall(int xPos, int yPos, Sprite sprite, boolean level) {
		xPos -= xOffset;
		for (int y = 0; y < sprite.SIZE; y++) {
			int yChange = yPos + y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xChange = xPos + x;
				if (xChange < -sprite.SIZE || xChange >= width || yChange < 0 || yChange >= height) { break; }
				xChange = (xChange < 0) ? 0 : xChange;
				if (sprite.pixels[x+y*sprite.SIZE] != 0xFFFF00FF) {
					PartialTile tile = Game.level.getPartialTile((x+xPos+xOffset)/Tile.SIZE, (y+yPos)/Tile.SIZE, level);
					if (!(tile.solid() &&
							(x+xPos+xOffset)%Tile.SIZE >= tile.x0 && 
							(x+xPos+xOffset)%Tile.SIZE <= tile.x1 &&
							(y+yPos)%Tile.SIZE >= tile.y0 &&
							(y+yPos)%Tile.SIZE <= tile.y1)) {
						if (!Game.level.getTile((x+xPos+xOffset)/Tile.SIZE, (y+yPos)/Tile.SIZE, level).solid())
						pixels[xChange+yChange*width] = sprite.pixels[x+y*Player.SIZE];
					}
				}
			}
		}
	}
	
	
	public void setOfsset(int xOffset) {
		this.xOffset = xOffset - 6 * Player.SIZE;
	}

	public void renderTile(int xp, int yp, PartialTile tile) {
		xp -= xOffset;
		
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = yp + y;
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = xp + x;
				if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) { break; }
				xa = (xa < 0) ? 0 : xa;
				if (tile.sprite.pixels[x+y*tile.sprite.SIZE] != 0xffff00ff) {
					pixels[xa+ya*width] = tile.sprite.pixels[x+y*tile.sprite.SIZE];
				}
			}
		}
	}
	
	
}
