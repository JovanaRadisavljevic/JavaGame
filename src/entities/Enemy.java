package entities;
import static utils.Constants.EnenmyConstants.*;
import static utils.HelpMethods.*;

import java.awt.geom.Rectangle2D.Float;

import static utils.Constants.*;
import static utils.Constants.Directions.*;
import main.Game;

public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance=Game.TILES_SIZE;
	protected boolean active= true;
	protected boolean attackChecked;
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		this.walkSpeed=Game.SCALE*0.35f;
		maxHealth = getMaxHealth(enemyType);
		currentHealth=maxHealth;
		
	}
	protected void firstUpdateCheck(int[][] lvlData) {
		if (!isEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}
	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (isFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}
	protected void turnTowardsPlyer(Player player) {
		if(player.hitbox.x>hitbox.x) 
			walkDir=RIGHT;
		else
			walkDir=LEFT;
	}
	protected boolean canSeePlayer(int[][] lvlData,Player player) {
		int playerTileY=(int)(player.getHitbox().y / Game.TILES_SIZE);
		if(tileY==playerTileY) {
			if(isPlayerInRange(player)) {
				if(isSightClear(lvlData,hitbox,player.hitbox,tileY)) {
					return true;
				}
			}
		}
		return false;
	}
	protected boolean isPlayerInRange(Player player) {
		int absValue=(int)Math.abs(player.hitbox.x-hitbox.x);
		return absValue<=attackDistance*5;
	}
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue=(int)Math.abs(player.hitbox.x-hitbox.x);
		return absValue<=attackDistance;
	}
	protected void newState(int enemyState) {
		this.state=enemyState;
		aniTick=0;
		aniIndex=0;
	}
	protected void updateInAir(int[][] lvlData) {
		if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY=(int)(hitbox.y / Game.TILES_SIZE);
		}
	}
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(enemyType, state)) {
				aniIndex = 0;
				if(state==ATTACK || state==HIT)
					state=IDLE;
				else if(state==DEAD)
					active=false;
			}
		}
	}
	
	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	public boolean isActive() {
		return active;
	}
	public void hurt(int amount) {
		currentHealth-=amount;
		if(currentHealth<=0) {
			newState(DEAD);
		}else {
			newState(HIT);
		}
	}
	protected void checkEnemyHit(Player player, Float attackBox) {
		if(attackBox.intersects(player.hitbox)) {
			player.changeHealth(-getEnemyDamage(enemyType));
		}
		attackChecked=true;
		
	}
	public void resetEnemy() {
		hitbox.x=x;
		hitbox.y=y;
		firstUpdate=true;
		currentHealth=maxHealth;
		newState(IDLE);
		active=true;
		airSpeed=0;
	}
}
