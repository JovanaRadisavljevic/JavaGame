package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.VolumeButtons.*;
import gameStates.Gamestate;
import gameStates.Playing;

import static utils.Constants.UI.PauseButtons.*;
import main.Game;
import utils.Constants.UI.URMButtons;
import utils.LoadSave;
import static utils.Constants.UI.URMButtons.*;
public class PauseOverlay {
	private BufferedImage backgroundImage ;
	private int bgX,bgY,bgHeight,bgWidth;
	private SoundButton musicButton,sfxButton;
	private UrmButton menuB,replayB,unpauseB;
	private Playing playing;
	private VolumeButton volumeButton;
	
	public PauseOverlay(Playing playing) {
		this.playing=playing;
		loadBackground();
		createSoundButtons();
		createUrmButtons();
		createVolumeButton();
	}
	private void createVolumeButton() {
		int vX = (int) (309 * Game.SCALE);
		int vY = (int) (278 * Game.SCALE);
		volumeButton=new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
		
	}
	private void createUrmButtons() {
		int menuX = (int) (313 * Game.SCALE);
		int replayX = (int) (387 * Game.SCALE);
		int unpauseX = (int) (462 * Game.SCALE);
		int bY=(int)(325*Game.SCALE);
		
		menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE,2);
		replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE,1);
		unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE,0);
	}
	private void createSoundButtons() {
		int soundX = (int) (450 * Game.SCALE);
		int musicY = (int) (140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}
	private void loadBackground() {
		// TODO Auto-generated method stub
		backgroundImage=LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgWidth=(int)(backgroundImage.getWidth()*Game.SCALE);
		bgHeight=(int)(backgroundImage.getHeight()*Game.SCALE);
		bgX=Game.GAME_WIDTH/2-bgWidth/2;
		bgY= (int)(25*Game.SCALE);
	}
	public void update() {
		musicButton.update();
		sfxButton.update();
		menuB.update();
		replayB.update();
		unpauseB.update();
		volumeButton.update();
	}
	public void draw(Graphics g) {
		//bg
		g.drawImage(backgroundImage, bgX, bgY, bgWidth, bgHeight, null);
		//sound btn
		musicButton.draw(g);
		sfxButton.draw(g);
		//urm btns
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);
		//volume slider
		volumeButton.draw(g);
	}
	public void mouseDragged(MouseEvent e) {
		if(volumeButton.isMousePressed()) {
			volumeButton.changeX(e.getX());
		}
		
	}
	public void mousePressed(MouseEvent e) {
		if(isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e, menuB))
			menuB.setMousePressed(true);
		else if(isIn(e, replayB))
			replayB.setMousePressed(true);
		else if(isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if(isIn(e, musicButton)) {
			if(musicButton.isMousePressed())
				musicButton.setMuted(!musicButton.isMuted());
		}
		else if(isIn(e, sfxButton)) {
			if(sfxButton.isMousePressed())
				sfxButton.setMuted(!sfxButton.isMuted());
		}
		else if(isIn(e, menuB)) {
			if(menuB.isMousePressed()) {
				Gamestate.state=Gamestate.MENU;
				playing.unpauseGame();
			}
		}
		else if(isIn(e, replayB)) {
			if(replayB.isMousePressed()){
				playing.resetAll();
				playing.unpauseGame();
			}
		}
		else if(isIn(e, unpauseB)) {
			if(unpauseB.isMousePressed())
				playing.unpauseGame();
		}
		musicButton.resetBools();
		sfxButton.resetBools();
		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		volumeButton.setMouseOver(false);
		
		if(isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e, menuB))
			menuB.setMouseOver(true);
		else if(isIn(e, replayB))
			replayB.setMouseOver(true);
		else if(isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
	}
	private boolean isIn(MouseEvent e,PausedButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
}
