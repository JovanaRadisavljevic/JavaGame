package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {
	public static final String playerAtlas="player_sprites.png";
	public static final String levelAtlas="outside_sprites.png";
	public static final String level_one_data="level_one_data.png";
	public static final String menu_buttons="button_atlas.png";
	public static final String menu_background="menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	
	public static BufferedImage getSpriteAtlas(String filename) {
		BufferedImage img=null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+filename);
		try {
			img=ImageIO.read(is);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	public static int[][] getLevelData(){
		int[][] lvldata=new int[Game.TILES_IN_HIGHT][Game.TILES_IN_WIDTH];
		BufferedImage img = getSpriteAtlas(level_one_data);
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value=color.getRed();
				if(value>=48)
					value=0;
				lvldata[j][i]=value;
			}
		}
		return lvldata; 
	}
}
