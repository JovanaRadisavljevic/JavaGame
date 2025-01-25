package entities;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnenmyConstants.*;
import static utils.HelpMethods.canMoveHere;
import static utils.HelpMethods.getEntityYPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.isEntityOnFloor;
import static utils.HelpMethods.isFloor;

import main.Game;
public class Crabby extends Enemy {

	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(x,y,(int)(22*Game.SCALE),(int)(19*Game.SCALE));
		
	}
	public void update(int[][] lvlData,Player player) {
		updateMove(lvlData,player);
		updateAnimationTick();

	}
	private void updateMove(int[][] lvlData, Player player) {
		if (firstUpdate) 
			firstUpdateCheck(lvlData);
		
		if (inAir) {
			updateInAir(lvlData);
		}
		else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if(canSeePlayer(lvlData, player))
					turnTowardsPlyer(player);
				if(isPlayerCloseForAttack(player))
					newState(ATTACK);
				move(lvlData);
				break;
			}
		}

	}
	


}
