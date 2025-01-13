package levels;

public class Level {
	private int[][] leveldata;
	public Level(int[][] leveldata) {
		this.leveldata=leveldata;
	}
	public int getSpritesIndex(int x,int y) {
		return leveldata[y][x];
	}
	public int[][] getLeveldata() {
		return leveldata;
	}
}
