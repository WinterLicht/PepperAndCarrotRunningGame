package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Playable character class.
 * The runner is only able to move horizontally, other entities are
 * moving towards player. Runner can jump and doublejump.
 * TODO: attacks ...
 * @author WinterLicht
 *
 */
public class Runner extends Image {
	public State currState = State.RUNNING;
	int speedY = 0; /** Vertical speed in pixel. */
	int maxJumpSpeed = 24; /** Maximum speed when jumping in pixel */

	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage attackingAnim;
	
	Level level;

	public SweepAtt ability1; /** simple attack. */

	/**
	 * Possible states.
	 */
	enum State{
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING,
		//TODO: for this states may be an other animation
		ATTACK_RUNNING, ATTACK_FALLING, ATTACK_JUMPING, ATTACK_DOUBLEJUMPING,
		DYING;
	}

	public Runner(Level l){
		super(new TextureRegion(Assets.I.atlas.findRegion("run")));
		ability1 = new SweepAtt(this, l);
		level = l;
		//Runner is always placed with some offset
		setOrigin(Align.center);
		setX(Constants.OFFSET_TO_EDGE);
		setY(Constants.OFFSET_TO_GROUND);
		//Load Animations
		runningAnim = new AnimatedImage(new Animation(0.099f, Assets.I.getRegions("run"), Animation.PlayMode.LOOP));
		runningAnim.setOrigin(Align.center);
		runningAnim.start();
		jumpingAnim = new AnimatedImage(new Animation(0.17f, Assets.I.getRegions("jump"), Animation.PlayMode.LOOP));
		jumpingAnim.setOrigin(Align.center);
		jumpingAnim.start();
		attackingAnim = new AnimatedImage(new Animation(ability1.durationMax/8, Assets.I.getRegions("attack"), Animation.PlayMode.NORMAL));
		attackingAnim.setOrigin(Align.center);
		attackingAnim.start();
	}

	public void jump(){
		if (isRunnig()){
			setJumping();
			speedY = maxJumpSpeed;
			return;
		}
		if (isJumping()){
			setDoubleJumping();
			speedY = maxJumpSpeed;
			return;
		}
	}

	public void activateAbility(int i) {
		if (i == 1){
			ability1.activate();
		}
	}

	/**
	 * Check collision between player and platforms
	 */
	private void checkCollision(){
		Array<Integer> platformTops = level.getWallsYPosNearPlayer();
		//Players position
		float posY = getY() - Constants.OFFSET_TO_GROUND;
		//Offset in pixel
		int offsetTop = 12;
		int offsetBottom = 10;
		for (int i = 0; i < platformTops.size; i++){
			Integer tileTop = platformTops.get(i);
			//Player lands if he is near platforms top
			if( tileTop - offsetBottom < posY && posY < tileTop + offsetTop) {
				land(tileTop);
			}
		}
	}

	/**
	 * Collision between enemies and potions.
	 */
	private void checkEntityCollision(){
		Array<Actor> enemies;
		Array<Actor> potions;
		if (level.activeMap == 1) {
			enemies = level.getEntitiesInRadius(200, level.enemies1);
			potions = level.getEntitiesInRadius(200, level.potions1);
		} else {
			enemies = level.getEntitiesInRadius(200, level.enemies2);
			potions = level.getEntitiesInRadius(200, level.potions2);
		}
		Rectangle hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
		for (Actor e : enemies) {
			Enemy enemy = (Enemy) e;
			Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
			if (hitbox.overlaps(enemyRect) && enemy.isAlive()) {
				//enemy.die();//TODO Player dies!!
				System.out.println("Enemy collision");
			}
		}
		for (Actor p : potions) {
			Rectangle potionRect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
			if (p.isVisible() && hitbox.overlaps(potionRect)){
				p.setVisible(false); //TODO various potions
				ability1.currentEnergy += 1;
			}
		}
	}

	/**
	 * Land on given y-coordinate position.
	 * @param y
	 */
	public void land(float y){
		//Player lands only if his speed is small enough
		int speedOffset = 8;
		if (speedY < speedOffset){
			setY(y+Constants.OFFSET_TO_GROUND);
			speedY = 0;
			setRunnig();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		checkCollision();
		checkEntityCollision();
		//Update all attacks
		ability1.update(delta);
		//Decide which animation is displayed
		switch (currState) {
		case DOUBLEJUMPING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		case DYING:
			break;
		case JUMPING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		case RUNNING:
			runningAnim.act(delta);
			setDrawable(runningAnim.getDrawable());
			break;
		case FALLING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		case ATTACK_RUNNING:
			attackingAnim.act(delta);
			setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_JUMPING:
			attackingAnim.act(delta);
			setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_DOUBLEJUMPING:
			attackingAnim.act(delta);
			setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_FALLING:
			attackingAnim.act(delta);
			setDrawable(attackingAnim.getDrawable());
			break;
		default: //Should not be reached
			break;
		}
		//Gravity is 1 pixel
		speedY -= 1;
		//Move
		float oldYPos = getY();
		setY(getY() + speedY);
		//Player can't fall under/below the ground
		if (getY() < Constants.OFFSET_TO_GROUND) { //land
			setRunnig();
			setY(Constants.OFFSET_TO_GROUND);
		}
		if (getY() < oldYPos && isRunnig()) {
			//Player is falling, if his y-position is lowered
			//and he was previously running.
			setFalling();
		}
	}

	//Helper methods for states
	public void setRunnig(){
		if (isAttacking()) currState = State.ATTACK_RUNNING;
		else currState = State.RUNNING;
	}
	public void setFalling(){
		if (isAttacking()) currState = State.ATTACK_FALLING;
		else currState = State.FALLING;
	}
	public void setJumping(){
		if (isAttacking()) currState = State.ATTACK_JUMPING;
		else currState = State.JUMPING;
	}
	public void setDoubleJumping(){
		if (isAttacking()) currState = State.ATTACK_DOUBLEJUMPING;
		else currState = State.DOUBLEJUMPING;
	}
	/**
	 * resets also attacking animation.
	 */
	public void setAttacking(){
		attackingAnim.start();
		switch (currState) {
		case DOUBLEJUMPING:
			currState = State.ATTACK_DOUBLEJUMPING;
			break;
		case FALLING:
			currState = State.ATTACK_FALLING;
			break;
		case JUMPING:
			currState = State.ATTACK_JUMPING;
			break;
		case RUNNING:
			currState = State.ATTACK_RUNNING;
			break;
		default:
			break;
		}
	}
	public boolean isAttacking(){
		return (currState == State.ATTACK_DOUBLEJUMPING || currState == State.ATTACK_RUNNING ||
				currState == State.ATTACK_FALLING || currState == State.ATTACK_JUMPING);
	}
	public boolean isRunnig(){
		return (currState == State.RUNNING || currState == State.ATTACK_RUNNING);
	}
	public boolean isFalling(){
		return (currState == State.FALLING || currState == State.ATTACK_FALLING);
	}
	public boolean isJumping(){
		return (currState == State.JUMPING || currState == State.ATTACK_JUMPING);
	}
	public boolean isDoubleJumping(){
		return (currState == State.DOUBLEJUMPING || currState == State.ATTACK_DOUBLEJUMPING);
	}
}
