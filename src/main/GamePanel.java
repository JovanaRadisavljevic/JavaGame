package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HIGHT;
import static main.Game.GAME_WIDTH;;

public class GamePanel extends JPanel {
	private MouseInputs mouse;
	private Game game;
	public GamePanel(Game game) {
		mouse = new MouseInputs(this);
		this.game=game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	private void setPanelSize() {
		Dimension size=new Dimension(GAME_WIDTH,GAME_HIGHT);
		setPreferredSize(size);
		System.out.println("size: "+GAME_WIDTH+" H: "+GAME_HIGHT);
	}
	
	//paintComponent se poziva svakog puta kada pokrenemo repaint klasu
	//ne pozivamo je direktno
	//Graphics - nam omogucava da crtamo
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
	public void updateGame() {
		
	}
	public Game getGame() {
		return game;
	}
	
	
}
