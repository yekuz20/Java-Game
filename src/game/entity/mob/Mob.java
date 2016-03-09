package game.entity.mob;

import game.Game;
import game.entity.Entity;
import game.graphics.Sprite;
import game.level.Level;
import game.level.tile.PartialTile;
import game.level.tile.Tile;

public abstract class Mob extends Entity {
	
	protected Sprite sprite;
	protected int dir = 0;
	protected boolean moving = false;
	public int health;
	public boolean whichLevel;
	
	public void move(int xChange, int yChange) {
		if (xChange > 0) dir = 1;
		if (xChange < 0) dir = 3;
		if (yChange > 0) dir = 2;
		if (yChange < 0) dir = 0;
//		if (!collision()) {
//			if (!collision(xChange, true)) {
//				if (!partialTileCollision(xChange, yChange)) {
//					x += xChange;
//				}
//			}
//		}
	}
	
	public void update() {
		
	}
	
	public boolean collision() {
		boolean solid = false;
		
		
		
		// checks if the player is in a wall, so they can't move in a wall.
		
		if (Game.level.getTile((x+Player.SIZE-2)/Tile.SIZE, (y+Player.SIZE-2)/Tile.SIZE, Level.whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+Player.SIZE-2)/Tile.SIZE, (y+1)/Tile.SIZE, Level.whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+1)/Tile.SIZE, (y+Player.SIZE-2)/Tile.SIZE, Level.whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+1)/Tile.SIZE, (y+1)/Tile.SIZE, Level.whichLevel).solid()) solid = true;
		
		return solid;
	}
	public boolean collision(boolean whichLevel) {
		boolean solid = false;
		
		
		
		// checks if the player is in a wall, so they can't move in a wall.
		
		if (Game.level.getTile((x+Player.SIZE-2)/Tile.SIZE, (y+Player.SIZE-2)/Tile.SIZE, whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+Player.SIZE-2)/Tile.SIZE, (y+1)/Tile.SIZE, whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+1)/Tile.SIZE, (y+Player.SIZE-2)/Tile.SIZE, whichLevel).solid()) solid = true;
		if (Game.level.getTile((x+1)/Tile.SIZE, (y+1)/Tile.SIZE, whichLevel).solid()) solid = true;
		
		return solid;
	}
	
	public boolean collision(int distance, boolean horizontal) {
		boolean solid = false;
		
		// if the player is moving horizontally, check if they will run into a block.
		// if the player is moving vertically, check if they are moving into a block.
		
		if (horizontal) {
			if (Game.level.getTile((x+distance+Player.SIZE-1)/Tile.SIZE, (y+Player.SIZE-1)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance+Player.SIZE-1)/Tile.SIZE, (y)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance)/Tile.SIZE, (y+Player.SIZE-1)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance)/Tile.SIZE, (y)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
		}
		
		if (!horizontal) {
			if (Game.level.getTile((x+0)/Tile.SIZE, (y+distance+0)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+Player.SIZE-1)/Tile.SIZE, (y+distance+0)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+0)/Tile.SIZE, (y+distance+Player.SIZE-1)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+Player.SIZE-1)/Tile.SIZE, (y+distance+Player.SIZE-1)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
		}

		return solid;
	}
	
	public boolean collisionGravity(int value) {
		// check if the player is on the ground. if he is, return true
		
		boolean solid = false;
		
		if (Game.level.getTile((x+0)/Tile.SIZE, (y+value+Player.SIZE)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}
		if (Game.level.getTile((x+Player.SIZE-1)/Tile.SIZE, (y+value+Player.SIZE)/Tile.SIZE, Level.whichLevel).solid()) { solid = true;}

		return solid;
	}
	
	public boolean partialTileCollision(int xMotion, int yMotion, boolean horizontal) {
		boolean solid = false;
		
		if (horizontal) {
			for (int pixelX = 0;pixelX < Player.SIZE;pixelX++) {
				for (int pixelY = 0; pixelY < Player.SIZE; pixelY++) {
					PartialTile tile = Game.level.getPartialTile((x+xMotion+pixelX)/Tile.SIZE, (y+pixelY)/Tile.SIZE, Level.whichLevel);
					if (tile.solid() &&
							(x+xMotion+pixelX)%Tile.SIZE >= tile.x0 && 
							(x+xMotion+pixelX)%Tile.SIZE <= tile.x1 &&
							(y+pixelY)%Tile.SIZE >= tile.y0 &&
							(y+pixelY)%Tile.SIZE <= tile.y1) {solid = true;}
				}
			}
		}
			
		else {
			for (int pixelX = 0;pixelX < Player.SIZE;pixelX++) {
				for (int pixelY = 0; pixelY < Player.SIZE; pixelY++) {
					PartialTile tile = Game.level.getPartialTile((x+pixelX)/Tile.SIZE, (y+yMotion+pixelY)/Tile.SIZE, Level.whichLevel);
					if (tile.solid() &&
							(x+pixelX)%Tile.SIZE >= tile.x0 && 
							(x+pixelX)%Tile.SIZE <= tile.x1 && 
							(y+yMotion+pixelY)%Tile.SIZE >= tile.y0 &&
							(y+yMotion+pixelY)%Tile.SIZE <= tile.y1) {solid = true;}
				}
			}
		}
		
		return solid;
	}
	
	public boolean partialTileCollision(boolean whichLevel) {
		boolean solid = false;
		
		for (int pixelX = 0;pixelX < Player.SIZE;pixelX++) {
			for (int pixelY = 0; pixelY < Player.SIZE; pixelY++) {
				PartialTile tile = level.getPartialTile((x+pixelX)/Tile.SIZE, (y+pixelY)/Tile.SIZE, whichLevel);
				if (tile.solid() &&
						(x+pixelX)%Tile.SIZE >= tile.x0 && 
						(x+pixelX)%Tile.SIZE <= tile.x1 &&
						(y+pixelY)%Tile.SIZE >= tile.y0 &&
						(y+pixelY)%Tile.SIZE <= tile.y1) {solid = true;}
			}
		}
		
		return solid;
	}
	
	public void render() {
		
	}
	
}
