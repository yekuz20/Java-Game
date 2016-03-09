package game.entity;

import java.util.Random;

import game.graphics.Screen;
import game.level.Level;

public abstract class Entity {
	
	public int x, y;
	private boolean removed = false;
	public Level level;
	final protected Random random = new Random();
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		
	}
	
	public void remove() {
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void init(Level level) {
		this.level = level;
	}
}
