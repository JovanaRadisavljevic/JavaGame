package entities;

import static utils.Constants.Directions.DOWN;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.Directions.UP;
import static utils.Constants.PlayerConstatns.IGRAC;
import static utils.Constants.PlayerConstatns.RUNNING;
import static utils.Constants.PlayerConstatns.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;
import utils.LoadSave;
import static utils.HelpMethods.canMoveHere;

	
public class Player extends Entity {
		private BufferedImage[][] igracAnimacija;
		private int aniTick, aniIndex, aniSpeed = 25;
		private int playerAction = IGRAC;
		private boolean moving = false, attacking = false;
		private boolean left, up, right, down;
		private float playerSpeed = 2.0f;
		private int[][] lvlData;
		private float xDrawOffset = 21*Game.SCALE;
		private float yDrawOffset = 4* Game.SCALE;

		public Player(float x, float y, int width, int height) {
			super(x, y, width, height);
			loadAniamtions();
			initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
		}

		public void update() {
			updatePos();
			updateAnimationTick();
			setAnimation();
		}

		public void render(Graphics g) {
			g.drawImage(igracAnimacija[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
			drawHitbox(g);;
		}

		private void updateAnimationTick() {
			aniTick++;
			if (aniTick >= aniSpeed) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= getSpriteAmount(playerAction)) {
					aniIndex = 0;
					attacking = false;
				}

			}

		}

		private void setAnimation() {
			int startAni = playerAction;

			if (moving)
				playerAction = RUNNING;
			else
				playerAction = IGRAC;

			if (attacking)
				playerAction = ATTACK_1;

			if (startAni != playerAction)
				resetAniTick();
		}

		private void resetAniTick() {
			aniTick = 0;
			aniIndex = 0;
		}

		private void updatePos() {
			moving = false;
			if(!left && !right && !up && !down) {
				return;
			}
			float xSpeed=0,ySpeed=0;
			
			if (left && !right) {
				xSpeed = - playerSpeed;
			} else if (right && !left) {
				xSpeed = playerSpeed;
			}

			if (up && !down) {
				ySpeed = - playerSpeed;
			} else if (down && !up) {
				ySpeed = playerSpeed;
			}
			if (canMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.x += xSpeed;
				hitbox.y += ySpeed;
				moving = true;
			}
		}

		private void loadAniamtions() {
			BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.playerAtlas);
				int rows = 9; // Broj redova u slici
			    int cols = 6; // Broj kolona u slici
			    int characterWidth = 64; // Širina lika
			    int characterHeight = 40; // Visina lika

			    igracAnimacija = new BufferedImage[rows][cols];

			    for (int j = 0; j < rows; j++) {
			        for (int i = 0; i < cols; i++) {
			            int x = i * characterWidth; // Početna X koordinata lika
			            int y = j * characterHeight; // Početna Y koordinata lika
			            igracAnimacija[j][i] = img.getSubimage(x, y, characterWidth, characterHeight);
			        }
			    }
			
		}
		public void loadLevelData(int[][] lvlData) {
			this.lvlData=lvlData;
		}

		public void resetDirBooleans() {
			left = false;
			right = false;
			up = false;
			down = false;
		}

		public void setAttacking(boolean attacking) {
			this.attacking = attacking;
		}

		public boolean isLeft() {
			return left;
		}

		public void setLeft(boolean left) {
			this.left = left;
		}

		public boolean isUp() {
			return up;
		}

		public void setUp(boolean up) {
			this.up = up;
		}

		public boolean isRight() {
			return right;
		}

		public void setRight(boolean right) {
			this.right = right;
		}

		public boolean isDown() {
			return down;
		}

		public void setDown(boolean down) {
			this.down = down;
		}

	}
	
	
