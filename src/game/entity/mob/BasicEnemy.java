package game.entity.mob;

import game.Game;
import game.graphics.Screen;
import game.graphics.Sprite;
import game.level.Level;
import game.level.tile.PartialTile;
import game.level.tile.Tile;

public class BasicEnemy extends Mob {
	double motionY = 0;
	double gravityStartValue = 0.2;
	double gravity;
	public static double motionX = 0;
	public final static int SIZE = 16;
	private int state;
	private int tempState;
	private boolean whichLevel;
	
	// 0 = not moving, looking left
	// 1 = not moving, looking right
	// 2 = moving left
	// 3 = moving right
	
	
	
	public BasicEnemy (int x, int y, int state, boolean whichLevel) {
		this.x = x;
		this.y = y;
		this.state = state;
		this.whichLevel = whichLevel;
		this.tempState = state;
	}
	
	public void update() {
		
		if ((!Game.level.getTile((int)(x+motionX+SIZE)/Tile.SIZE, (y+SIZE)/Tile.SIZE, whichLevel).solid() ||
				Game.level.getTile((int)(x+motionX+SIZE)/Tile.SIZE, (y+SIZE/2)/Tile.SIZE, whichLevel).solid()) && state == 3) { state = 2; }
		
		if ((!Game.level.getTile((int)(x+motionX)/Tile.SIZE, (y+SIZE)/Tile.SIZE, whichLevel).solid() ||
				Game.level.getTile((int)(x+motionX-1)/Tile.SIZE, (y+SIZE/2)/Tile.SIZE, whichLevel).solid()) && state == 2) { state = 3; }
		
		
		switch (state) {
		case 2: motionX -= 0.5;break;
		case 3: motionX += 0.5;break;
		}
		
		
		
		if (!collision(whichLevel)) {
			if (!collision((int) motionY, false, whichLevel) && !partialTileCollision((int) motionX, (int) motionY, false)) {
				y += (int) motionY; 
			}
			else {
				motionY = 1;
			}
			
			if (!collision((int) motionX, true, whichLevel) && !partialTileCollision((int) motionX, (int) motionY, true)) {
				x += (int) motionX;
			}
			else {
				motionX = 0;
			}
		}
		
		if (motionX < 0.5 && motionX > -0.5) motionX = 0;
		else motionX = (collisionGravity(1) || partialTileCollision(0, 1, false)) ? motionX * 0.8 : motionX * 0.95;
		
		
	}
	
	public boolean partialTileCollision(int xMotion, int yMotion, boolean horizontal) {
		boolean solid = false;
		
		if (horizontal) {
			for (int pixelX = 0;pixelX < SIZE;pixelX++) {
				for (int pixelY = 0; pixelY < SIZE; pixelY++) {
					PartialTile tile = Game.level.getPartialTile((x+xMotion+pixelX)/Tile.SIZE, (y+pixelY)/Tile.SIZE, whichLevel);
					if (tile.solid() &&
							(x+xMotion+pixelX)%Tile.SIZE >= tile.x0 && 
							(x+xMotion+pixelX)%Tile.SIZE <= tile.x1 &&
							(y+pixelY)%Tile.SIZE >= tile.y0 &&
							(y+pixelY)%Tile.SIZE <= tile.y1) {solid = true;}
				}
			}
		}
			
		else {
			for (int pixelX = 0;pixelX < SIZE;pixelX++) {
				for (int pixelY = 0; pixelY < SIZE; pixelY++) {
					PartialTile tile = Game.level.getPartialTile((x+pixelX)/Tile.SIZE, (y+yMotion+pixelY)/Tile.SIZE, whichLevel);
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
	
	public boolean collision(int distance, boolean horizontal, boolean whichLevel) {
		boolean solid = false;
		
		// if the player is moving horizontally, check if they will run into a block.
		// if the player is moving vertically, check if they are moving into a block.
		
		if (horizontal) {
			if (Game.level.getTile((x+distance+SIZE-1)/Tile.SIZE, (y+SIZE-1)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance+SIZE-1)/Tile.SIZE, (y)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance)/Tile.SIZE, (y+SIZE-1)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+distance)/Tile.SIZE, (y)/Tile.SIZE, whichLevel).solid()) { solid = true;}
		}
		
		if (!horizontal) {
			if (Game.level.getTile((x+0)/Tile.SIZE, (y+distance+0)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+SIZE-1)/Tile.SIZE, (y+distance+0)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+0)/Tile.SIZE, (y+distance+SIZE-1)/Tile.SIZE, whichLevel).solid()) { solid = true;}
			if (Game.level.getTile((x+SIZE-1)/Tile.SIZE, (y+distance+SIZE-1)/Tile.SIZE, whichLevel).solid()) { solid = true;}
		}

		return solid;
	}
	
	public void render(Screen screen) {
		if (whichLevel == Level.whichLevel) screen.renderEntity(x, y, Sprite.basicEnemy);
		else screen.renderEntity(x, y, Sprite.basicEnemy.faded);
	}
}
