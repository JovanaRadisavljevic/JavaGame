package utils;

import static utils.Constants.EnenmyConstants.CRABBY;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
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
		int maxWidth=lvldata[0].length*Game.TILES_SIZE;
		if(x<0 || x>=maxWidth) {
			return true;
		}
		if(y<0 || y>=Game.GAME_HIGHT) {
			return true;
		}
		float xindex=x/Game.TILES_SIZE;
		float yindex=y/Game.TILES_SIZE;
		return isTileSolid((int)xindex, (int)yindex, lvldata);
	}
	public static boolean isTileSolid(int xtile,int yTile,int[][] lvldata) {
		int value = lvldata[yTile][xtile];
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
		// Check the pixel below bottomleft and bottomright
		if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;

		return true;

	}
	public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed>0) {
			return isSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
		}else
			return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	public static boolean isAllTilesWalkable(int xStart,int xEnd,int y,int[][] lvlData) {
		for (int i = 0; i < xEnd-xStart; i++) {
			if(isTileSolid(xStart+i, y, lvlData)) {
				return false;
			}
							//y+1 proveravam jedno polje ispod
			if(!isTileSolid(xStart+i, y+1, lvlData)) {
				return false;
			}
		}
		return true;
	}
	public static boolean isSightClear(int[][] lvlData,Rectangle2D.Float firstHitbox,Rectangle2D.Float secondHitbox,int tileY) {
		int fistXTile =(int)(firstHitbox.x / Game.TILES_SIZE);
		int secondXTile =(int)(secondHitbox.x / Game.TILES_SIZE);
		
		if(fistXTile>secondXTile) {
			return isAllTilesWalkable(secondXTile, fistXTile, tileY, lvlData);
		}else {
			return isAllTilesWalkable(fistXTile, secondXTile, tileY, lvlData);
		}
	}
	
	public static int[][] GetLevelData(BufferedImage img) {
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}

	public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
		ArrayList<Crabby> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == CRABBY)
					list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}

	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}
}
