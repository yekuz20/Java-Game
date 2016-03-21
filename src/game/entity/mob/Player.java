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
		for (int i = 0; i < Level.exits.length; i++) {
			if (x >= Level.exits[i].x1 && x <= Level.exits[i].x2 && y >= Level.exits[i].y1 && y <= Level.exits[i].y2) {
				int newX = (Level.exits[i].x > 0) ? Level.exits[i].x : x ;
				int newY = (Level.exits[i].y > 0) ? Level.exits[i].y : y ;
				Game.level.loadLevel(Level.exits[i].id);
				setPlayerPos(newX, newY);
			}
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
