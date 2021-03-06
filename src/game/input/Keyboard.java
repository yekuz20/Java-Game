package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.entity.mob.Player;
import game.level.Level;

public class Keyboard implements KeyListener {
	
	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right, space, wasSpacePressed, q, e;
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
		q = keys[KeyEvent.VK_Q];
		e = keys[KeyEvent.VK_E];
		
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
