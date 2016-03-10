package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.entity.mob.BasicEnemy;
import game.entity.mob.Player;
import game.graphics.Screen;
import game.input.Keyboard;
import game.level.Level;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
		
	public static int totalTicks = 0;
	
	public static int width = 512;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	public static Level level;
	private Player player;
	private boolean running = false;
	
	public static BasicEnemy[] basicEnemies = new BasicEnemy[100];
	
	
	private Screen screen;
	
	private BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size); 
		
		frame = new JFrame();
		screen = new Screen(width, height);
		key = new Keyboard();
		
		level = new Level("/startLevelBlue.png", "/startLevelRed.png");
		Level.populateLevels();
		int x = Level.playerX;
		int y = Level.playerY;
		player = new Player(x, y, key);
		player.init(level);
		addKeyListener(key);
		
	}
	
	
	public synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
					update();
					updates++;
					totalTicks++;
					delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle("game | " + updates + " ups, " + frames + " fps");
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	
	public void update() {
		key.update();
		player.update();
		for (int i = 0; i < basicEnemies.length; i++) {
			if (basicEnemies[i] != null) {
				basicEnemies[i].update();
			}
			else {
				break;
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();	
		
		level.render(player.x, screen);
		for (int i = 0; i < basicEnemies.length; i++) {
			if (basicEnemies[i] != null) {
				basicEnemies[i].render(screen);
			}
		}
		
		player.render(screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
			
		}
		
		Graphics g = bs.getDrawGraphics();
//		all graphics code between here and g.dispose();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show(); // show bullshit	
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("game");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
	
	public static void createBasicEnemy(int x, int y, int state, boolean whichLevel) {
		for (int j = 0; j < basicEnemies.length; j++) {
			if (basicEnemies[j]==null) {
				basicEnemies[j] = new BasicEnemy(x, y, state, whichLevel);
				basicEnemies[j].init(level);
				break;
			}
		}
	}

}
