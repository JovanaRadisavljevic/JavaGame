package entities;

import static utils.Constants.Directions.RIGHT;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.*;
import static utils.Constants.Directions.UP;
import static utils.Constants.PlayerConstatns.RUNNING;
import static utils.Constants.PlayerConstatns.*;
import static utils.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import gameStates.Playing;
import main.Game;
import utils.LoadSave;
import static utils.HelpMethods.canMoveHere;

	
public class Player extends Entity {
		private BufferedImage[][] igracAnimacija;
		
		private boolean moving = false, attacking = false;
		private boolean left, right, jump;
		
		private int[][] lvlData;
		private float xDrawOffset = 21*Game.SCALE;
		private float yDrawOffset = 4* Game.SCALE;
		//jumping/gravity
		private float jumpSpeed = -2.25f * Game.SCALE;
		private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
		
		//status bar UI
		private BufferedImage statusBarImg;

		private int statusBarWidth = (int) (192 * Game.SCALE);
		private int statusBarHeight = (int) (58 * Game.SCALE);
		private int statusBarX = (int) (10 * Game.SCALE);
		private int statusBarY = (int) (10 * Game.SCALE);

		private int healthBarWidth = (int) (150 * Game.SCALE);
		private int healthBarHeight = (int) (4 * Game.SCALE);
		private int healthBarXStart = (int) (34 * Game.SCALE);
		private int healthBarYStart = (int) (14 * Game.SCALE);

		private int healthWidth = healthBarWidth;
		
		private int flipX=0;
		private int flipW=1;
		private boolean attackChecked;
		private Playing playing;

		public Player(float x, float y, int width, int height,Playing playing) {
			super(x, y, width, height);
			this.playing=playing;
			this.state=IDLE;
			this.maxHealth=100;
			this.currentHealth=maxHealth;
			this.walkSpeed=Game.SCALE*1.0f;
			loadAniamtions();
			initHitbox(20, 27);
			initAttackBox();
		}
		public void setSpawn(Point spawn) {
			this.x = spawn.x;
			this.y = spawn.y;
			hitbox.x = x;
			hitbox.y = y;
		}
		private void initAttackBox() {
			attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));			
		}

		public void update() {
			updateHealthBar();
			if(currentHealth<=0) {
				playing.setGameOver(true);
				return;
			}
			
			updateAttackBox();
			updatePos();
			if(attacking) {
				checkAttack();
			}
			updateAnimationTick();
			setAnimation();
		}

		private void checkAttack() {
			if(attackChecked || aniIndex==1) {
				return;
			}
			attackChecked=true;
			playing.checkEnemy(attackBox);
		}

		private void updateAttackBox() {
			if (right)
				attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
			else if (left)
				attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);

			attackBox.y = hitbox.y + (Game.SCALE * 10);
		}

		private void updateHealthBar() {
			healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);

		}

		public void render(Graphics g,int lvlOffset) {
			g.drawImage(igracAnimacija[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
			//drawHitbox(g,xLvlOffset);
			//drawAttackBox(g,lvlOffset);
			drawUI(g);
		}

		private void drawUI(Graphics g) {
			g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
			g.setColor(Color.red);
			g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		}

		private void updateAnimationTick() {
			aniTick++;
			if (aniTick >= ANI_SPEED) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= getSpriteAmount(state)) {
					aniIndex = 0;
					attacking = false;
					attackChecked=false;
				}

			}

		}

		private void setAnimation() {
			int startAni = state;
			
			if (moving)
				state = RUNNING;
			else
				state = IDLE;
			if(inAir) {
				if(airSpeed<0)//up
					state=JUMP;
				else
					state=FALLING;
			}

			if (attacking) {
				state = ATTACK;
				if(startAni!=ATTACK) {
					aniIndex=1;
					aniTick=0;
					return;
				}
			}

			if (startAni != state)
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
			if(!inAir)
				if((!left && !right) || (left && right))
					return;
			float xSpeed = 0;

			if (left) {
				xSpeed -= walkSpeed;
				flipX=width;
				flipW=-1;
			}
			if (right) {
				xSpeed += walkSpeed;
				flipX=0;
				flipW=1;
			}
			if (!inAir)
				if (!isEntityOnFloor(hitbox, lvlData))
					inAir = true;

			if (inAir) {
				if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
					hitbox.y += airSpeed;
					airSpeed += GRAVITY;
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
		public void changeHealth(int value) {
			currentHealth +=value;
			if(currentHealth<=0) {
				currentHealth=0;
				//gameOver();
			}else if(currentHealth>=maxHealth){
				currentHealth=maxHealth;
			}
		}
		private void loadAniamtions() {
			BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.playerAtlas);
				int rows = 7; // Broj redova u slici
			    int cols = 8; // Broj kolona u slici
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
			    statusBarImg=LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
			
		}
		public void loadLvlData(int[][] lvlData) {
			this.lvlData = lvlData;
			if (!isEntityOnFloor(hitbox, lvlData))
				inAir = true;
		}

		public void resetDirBooleans() {
			left = false;
			right = false;
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

		public boolean isRight() {
			return right;
		}

		public void setRight(boolean right) {
			this.right = right;
		}

		public void setJump(boolean jump) {
			this.jump = jump;
		}

		public void resetAll() {
			resetDirBooleans();
			inAir=false;
			attacking=false;
			moving=false;
			state=IDLE;
			currentHealth=maxHealth;
			//reset position
			hitbox.x=x;
			hitbox.y=y;
			if(!isEntityOnFloor(hitbox, lvlData)) {
				inAir=true;
			}
		}
	}
	
	
