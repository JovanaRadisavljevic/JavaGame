package main;
import java.awt.Graphics;

import entities.Player;
import levels.LevelManager;
public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET=120;
	private final int UPS_SET=200;
	
	public final static int TILES_DEFAULT_SIZE=32;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH=26;
	public final static int TILES_IN_HIGHT=14;
	public final static int TILES_SIZE=(int)(TILES_DEFAULT_SIZE*SCALE);//48
	public final static int GAME_WIDTH=TILES_SIZE*TILES_IN_WIDTH;
	public final static int GAME_HIGHT=TILES_SIZE*TILES_IN_HIGHT;
	
	private Player player;
	private LevelManager levelManger;
	
	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow=new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		
		startGameLoop();
	}
	private void initClasses() {
		levelManger=new LevelManager(this);
		player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE));
		player.loadLevelData(levelManger.getCurrentLevel().getLeveldata());
	}
	private void startGameLoop() {
		gameThread=new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();
			// izgubljeno vreme cuva i 
			//sabira ga da ne bih izgubila vreme
			//to vreme se prenosi sledecoj iteraciji tako da ne gubim vreme
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}
	private void update() {
		player.update();
		levelManger.update();
	}
	public void render(Graphics g) {
		player.render(g);
		levelManger.draw(g);
	}
	public Player getPlayer() {
		return player;
	}
	public void windowLostFocus() {
		player.resetDirBooleans();
	}
	
}
