package utils;

import java.awt.geom.Rectangle2D;

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
	public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// pad / pod
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// jump
			return currentTile * Game.TILES_SIZE;

	}
	public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		//proverim donju levu i donju desnu ivicu pravougaonika da li udaraju u 'cvrsto'
		if(!isSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData) && !isSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height+1, lvlData)) {
			return false;
		}
		return true;
	}
	
	
}
