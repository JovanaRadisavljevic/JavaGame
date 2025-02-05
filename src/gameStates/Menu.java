package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

public class Menu extends State implements Statemethods{
	private MenuButton[] buttons= new MenuButton[3];
	private BufferedImage backgroundImgMenu;
	private BufferedImage backgroundImgBack;
	private int menuX,menuY,menuWidth,menuHight;
	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgBack=LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	}

	private void loadBackground() {
		backgroundImgMenu=LoadSave.getSpriteAtlas(LoadSave.menu_background);
		menuWidth=(int)(backgroundImgMenu.getWidth()*Game.SCALE);
		menuHight=(int)(backgroundImgMenu.getHeight()*game.SCALE);
		menuX=Game.GAME_WIDTH/2  - menuWidth/2;
		menuY=(int)(45*Game.SCALE);
	}

	private void loadButtons() {     //u centru
		buttons[0]=new MenuButton(Game.GAME_WIDTH/2, (int)(150*Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1]=new MenuButton(Game.GAME_WIDTH/2, (int)(220*Game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2]=new MenuButton(Game.GAME_WIDTH/2, (int)(290*Game.SCALE), 2, Gamestate.QUIT);
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons) {
			mb.update();
		}
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImgBack, 0, 0, Game.GAME_WIDTH, Game.GAME_HIGHT, null);
		g.drawImage(backgroundImgMenu, menuX, menuY, menuWidth, menuHight,null);
		for (MenuButton mb : buttons) {
			mb.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				if(mb.isMousePressed()) {
					mb.applyGameState();
					break;
				}
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons) {
			mb.resetBools();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) {
			mb.setMouseOver(false);
		}
		for (MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			Gamestate.state=Gamestate.PLAYING;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
