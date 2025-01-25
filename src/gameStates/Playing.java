package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Environment.*;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utils.LoadSave;

public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManger;
	private EnemyManager enemyManager;
	
	private boolean paused=false;
	private PauseOverlay pauseOverlay;
	//pomeranje ekrana levela
	private int xLvlOffset;
	private int leftBorder=(int)(0.2*Game.GAME_WIDTH);
	private int rightBorder = (int)(0.8*Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.getLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide-Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX=maxTilesOffset*Game.TILES_SIZE;
	//background
	private BufferedImage backgroundImg,bigCloud,smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();
	
	public Playing(Game game) {
		super(game);
		initClasses();
		
		backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos=new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++) {
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
		}
		
	}
	
	private void initClasses() {
		levelManger=new LevelManager(game);
		enemyManager= new EnemyManager(this);
		player = new Player(200, 200, (int) (64 * game.SCALE), (int) (40 * game.SCALE));
		player.loadLevelData(levelManger.getCurrentLevel().getLeveldata());
		pauseOverlay=new PauseOverlay(this);
	}
	

	@Override
	public void update() {
		if(!paused) {
			levelManger.update();
			player.update();
			enemyManager.update(levelManger.getCurrentLevel().getLeveldata(),player);
			checkCloseToBorder();
		}else {
			pauseOverlay.update();
		}
		
	}

	private void checkCloseToBorder() {
		int playerX=(int)player.getHitbox().x;
		int diff = playerX-xLvlOffset;
		//ako je iznad desne granice treba da pomerim ekran napred (desno)
		//ako je blizu leve granice onda treba da pomerim ka levo ekran
		if(diff>rightBorder) {
			xLvlOffset+=diff-rightBorder;
		}
		//Px=30
		//off=15
		//diff=30-15=15
		//left 20
		//15<20
		//30+=15-20 => 30-5=25
		else if(diff<leftBorder) {
			xLvlOffset+=diff-leftBorder;
		}
		if(xLvlOffset>maxLvlOffsetX)
			xLvlOffset=maxLvlOffsetX;
		else if(xLvlOffset<0)
			xLvlOffset=0;
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HIGHT, null);
		
		drawClouds(g);
		
		levelManger.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g,xLvlOffset);
		
		if (paused) {
			g.setColor(new Color(0,0,0,150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HIGHT);
			pauseOverlay.draw(g);
			
		}
		
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++) {
			g.drawImage(bigCloud, i*BIG_CLOUD_WIDTH-(int)(xLvlOffset*0.3), (int)(204*Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		}
		for (int i = 0; i < smallCloudsPos.length; i++) {
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(paused)
			pauseOverlay.mousePressed(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseReleased(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseMoved(e);
		
	}
	public void mouseDragged(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseDragged(e);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
			break;
		case KeyEvent.VK_ESCAPE:
			paused=!paused;
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
			break;
		}
		
	}
	public void unpauseGame() {
		paused=false;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void windowLostFocus() {
		player.resetDirBooleans();
	}
	
	
	
}
