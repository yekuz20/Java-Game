package game.entity.mob;

import game.Game;
import game.graphics.Screen;
import game.graphics.Sprite;
import game.input.Keyboard;
import game.level.Level;

public class Player extends Mob {
	double motionY = 0;
	double gravityStartValue = 0.2;
	double gravity;
	public static double motionX = 0;
	public final static int SIZE = 16;
	
	
	public void setPlayerPos(int xNew, int yNew) {
		x = xNew;
		y = yNew;
	}
	
	boolean canJump = false;
	
	private Keyboard input;
	
	public Player(Keyboard input) {
		this.input = input;
	}
	
	public Player (int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
	}
	
	public void update() {
		
		if (x > Level.endX1 && x < Level.endX2 && y > Level.endY1 && y < Level.endY2) {
			if (Level.levels[Level.currentLevel*2+2] != null) 
			Level.currentLevel++;
			Game.level.loadLevel(Level.levels[Level.currentLevel*2], Level.levels[Level.currentLevel*2+1]);
			setPlayerPos(Level.playerX, y);
		}
		
		if (x > Level.backX1 && x < Level.backX2 && y > Level.backY1 && y < Level.backY2) {
			if (Level.currentLevel > 0) {
				if (Level.levels[Level.currentLevel*2-2] != null) 
				Level.currentLevel--;
			}
			Game.level.loadLevel(Level.levels[Level.currentLevel*2], Level.levels[Level.currentLevel*2+1]);
			setPlayerPos(Level.endX1, y);
		}
		
		if (y > 1000) {
//			setPlayerPos(Level.playerX, Level.playerY);
		}
		
		if (!input.wasSpacePressed && input.space && !collision(!Level.whichLevel) && !partialTileCollision(!Level.whichLevel)) {
			Level.whichLevel = (Level.whichLevel) ? false : true;input.wasSpacePressed = true;
		}
		if (!input.space) input.wasSpacePressed = false;
		
		if (input.left && x > 0 && (collisionGravity(1) || partialTileCollision(0, 1, false))) motionX -= 1;
		else if (input.left && x > 0 && !collisionGravity(1)) motionX = (motionX > -3)? motionX - 0.5: motionX - 0.2;
		
		if (input.right && (collisionGravity(1) || partialTileCollision(0, 1, false))) motionX += 1;
		else if (input.right && !collisionGravity(1)) motionX = (motionX < 3)? motionX + 0.5: motionX + 0.2;
		
		gravity = (input.down)? gravityStartValue * 2 : gravityStartValue;
		
		if (collisionGravity(1) || partialTileCollision(0, 1, false)) {
			canJump = true;
			
		}
		else if (!collision()) {
			canJump = false;
			motionY += gravity;
		}
		else {
			canJump = false;
		}
		
		if (collision()) {
			motionY = 0.5;
		}
		
		if (motionY < -2 && !input.up) {
			motionY = -2;
		}
		
		if (canJump && input.up) {
			motionY = -5;
		}
		
		if (!collision()) {
			if (!collision((int) motionY, false) && !partialTileCollision((int) motionX, (int) motionY, false)) {
				y += (int) motionY; 
			}
			else {
				motionY = 1;
			}
			
			if (!collision((int) motionX, true) && !partialTileCollision((int) motionX, (int) motionY, true)) {
				x += (int) motionX;
			}
			else {
				motionX = 0;
			}
		}
		
		if (motionX < 0.5 && motionX > -0.5) motionX = 0;
		else motionX = (collisionGravity(1) || partialTileCollision(0, 1, false)) ? motionX * 0.8 : motionX * 0.95;
		
		
	}
	
	
	public void render(Screen screen) {
		screen.renderPlayer(x, y, Sprite.player);
	}
}
