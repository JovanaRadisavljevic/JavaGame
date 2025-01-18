package gameStates;

import java.awt.event.MouseEvent;

import main.Game;
import ui.MenuButton;

public class State {
	protected Game game;
	
	public State(Game game) {
		this.game=game;
	}
	public Game getGame() {
		return game;
	}
	public boolean isIn(MouseEvent e,MenuButton mb) {//da li je mis unutar pravougaonika dugmeta
		return mb.getBounds().contains(e.getX(),e.getY());
	}
}
