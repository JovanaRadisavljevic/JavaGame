package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import static utils.Constants.EnenmyConstants.*;
import javax.imageio.ImageIO;

import entities.Crabby;
import main.Game;

public class LoadSave {
	public static final String playerAtlas="player_sprites.png";
	public static final String levelAtlas="outside_sprites.png";
	public static final String menu_buttons="button_atlas.png";
	public static final String menu_background="menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "backgroundImageToBack.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String CRABBY_SPRITE="crabby_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMAGE= "completed_sprite.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	
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
	
	public static BufferedImage[] getAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;
		try {
			file=new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File[] files = file.listFiles();//ovde su sve slike sad
		
		File[] filesSorted  = new File[files.length];
		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];

			}
		//prvo trazim koja od tri slike (j) ima 1.png (i) pa kad nadjem dodam ga u sortiran niz
		//sledece za indeks 2(i) trazim 2.jpg
		BufferedImage[] imgs = new BufferedImage[filesSorted.length];
		
		for (int i = 0; i < imgs.length; i++) {
			try {
				imgs[i]=ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imgs;
	}
	
	
	
}
