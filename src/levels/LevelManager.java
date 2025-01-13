package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game=game;
		importOutSideSprites();
		levelOne=new Level(LoadSave.getLevelData());
	}
	private void importOutSideSprites() {
		BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.levelAtlas);
		levelSprite=new BufferedImage[48];
		for (int j = 0; j < 4; j++) {//heigth
			for (int i = 0; i < 12; i++) {//width
				int index=j*12+i;
				levelSprite[index]=img.getSubimage(i*32, j*32, 32, 32);
			}
		}
	}
	public void draw(Graphics g) {
		for (int j = 0; j < Game.TILES_IN_HIGHT; j++) {
			for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
				int index= levelOne.getSpritesIndex(i, j);
				g.drawImage(levelSprite[index], i*game.TILES_SIZE, j*game.TILES_SIZE,game.TILES_SIZE,game.TILES_SIZE, null);
			}
		}
		g.drawImage(levelSprite[2], 0, 0, null);
		
	}
	public void update() {
		
	}
	public Level getCurrentLevel() {
		return levelOne;
	}
}
