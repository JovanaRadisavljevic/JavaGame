package utils;

import main.Game;

public class HelpMethods {
	public static boolean canMoveHere(float x,float y, float width,float height,int[][] lvldata) {
		if(!isSolid(x, y, lvldata)) {
			if(!isSolid(x+width, y+height, lvldata)) {
				if(!isSolid(x+width, y, lvldata)) {
					if(!isSolid(x, y+height, lvldata)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private static boolean isSolid(float x,float y,int[][] lvldata) {
		if(x<0 || x>=Game.GAME_WIDTH) {
			return true;
		}
		if(y<0 || y>=Game.GAME_HIGHT) {
			return true;
		}
		float xindex=x/Game.TILES_SIZE;
		float yindex=y/Game.TILES_SIZE;
		int value = lvldata[(int)yindex][(int)xindex];
		if(value>=48 || value<0 || value!=11) {
			return true;
		}else {
			return false;
		}
		
	}
}
