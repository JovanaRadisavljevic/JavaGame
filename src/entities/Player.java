package entities;

import static utils.Constants.Directions.RIGHT;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.Directions.UP;
import static utils.Constants.PlayerConstatns.IGRAC;
import static utils.Constants.PlayerConstatns.RUNNING;
import static utils.Constants.PlayerConstatns.*;
import static utils.HelpMethods.*;
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
		private boolean left, up, right, down, jump;
		private float playerSpeed = 1.0f*Game.SCALE;
		private int[][] lvlData;
		private float xDrawOffset = 21*Game.SCALE;
		private float yDrawOffset = 4* Game.SCALE;
		//jumping/gravity
		private float airSpeed = 0f;
		private float gravity = 0.04f * Game.SCALE;
		private float jumpSpeed = -2.25f * Game.SCALE;
		private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
		private boolean inAir = false;
		

		public Player(float x, float y, int width, int height) {
			super(x, y, width, height);
			loadAniamtions();
			initHitbox(x, y,(int)( 20 * Game.SCALE),(int)( 27 * Game.SCALE));
		}

		public void update() {
			updatePos();
			updateAnimationTick();
			setAnimation();
		}

		public void render(Graphics g,int lvlOffset) {
			g.drawImage(igracAnimacija[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
			//drawHitbox(g,xLvlOffset);
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
			if(inAir) {
				if(airSpeed<0)//up
					playerAction=JUMP;
				else
					playerAction=FALLING;
			}

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

			if (jump)
				jump();
			//if (!left && !right && !inAir)
			//	return;
			if(!inAir)
				if((!left && !right) || (left && right))
					return;
			float xSpeed = 0;

			if (left)
				xSpeed -= playerSpeed;
			if (right)
				xSpeed += playerSpeed;

			if (!inAir)
				if (!isEntityOnFloor(hitbox, lvlData))
					inAir = true;

			if (inAir) {
				if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
					hitbox.y += airSpeed;
					airSpeed += gravity;
					updateXPos(xSpeed);
				} else {
					hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
					if (airSpeed > 0)
						resetInAir();
					else
						airSpeed = fallSpeedAfterCollision;
					updateXPos(xSpeed);
				}

			} else
				updateXPos(xSpeed);
			moving = true;
		}

		private void jump() {
			if (inAir)
				return;
			inAir = true;
			airSpeed = jumpSpeed;

		}

		private void resetInAir() {
			inAir = false;
			airSpeed = 0;

		}

		private void updateXPos(float xSpeed) {
			if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
				hitbox.x += xSpeed;
			} else {
				hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
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
			if(!isEntityOnFloor(hitbox, lvlData)) {
				inAir=true;
			}
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
		public void setJump(boolean jump) {
			this.jump = jump;
		}
	}
	
	
