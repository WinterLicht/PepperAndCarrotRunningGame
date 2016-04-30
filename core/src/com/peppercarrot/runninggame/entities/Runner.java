package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Playable character class.
 * The runner is only able to move horizontally, other entities are
 * moving towards player. Runner can jump and doublejump.
 * @author WinterLicht
 *
 */
public class Runner extends Image {
	String name;
	public State currState = State.RUNNING;
	int speedY = 0; /** Vertical speed in pixel. */
	int maxJumpSpeed = 24; /** Maximum speed when jumping in pixel */

	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	AnimatedImage attackingAnim; //TODO: various animations for various attacks.
	
	Level level;

	public SweepAtt ability1; /** Simple attack. */
	public BlackHole ability2; /** Remove game entities. */
	public TimeDistortion ability3; /** Slow down level scroll speed. */

	/**
	 * Possible states.
	 */
	enum State{
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING,
		//TODO: for this states may be an other animation
		ATTACK_RUNNING, ATTACK_FALLING, ATTACK_JUMPING, ATTACK_DOUBLEJUMPING,
		DYING;
	}

	public Runner(Level l, String name){
		super(new TextureRegion(Assets.I.atlas.findRegion(name+"_run")));
		this.name = name;
		ability1 = new SweepAtt(this, l);
		ability2 = new BlackHole(this, l);
		ability3 = new TimeDistortion(this, l);
		level = l;
		//Runner is always placed with some offset
		setOrigin(Align.center);
		setX(Constants.OFFSET_TO_EDGE);
		setY(Constants.OFFSET_TO_GROUND);
		//Load Animations
		runningAnim = new AnimatedImage(new AnimatedDrawable(new Animation(0.079f, Assets.I.getRegions(name+"_run"), Animation.PlayMode.LOOP)));
		runningAnim.setOrigin(Align.center);
		jumpingAnim = new AnimatedImage(new AnimatedDrawable(new Animation(0.144f, Assets.I.getRegions(name+"_jump"), Animation.PlayMode.LOOP_PINGPONG)));
		jumpingAnim.setOrigin(Align.center);
		doubleJumpingAnim = new AnimatedImage(new AnimatedDrawable(new Animation(0.144f, Assets.I.getRegions(name+"_doublejump"), Animation.PlayMode.LOOP_PINGPONG)));
		doubleJumpingAnim.setOrigin(Align.center);
		fallingAnim = new AnimatedImage(new AnimatedDrawable(new Animation(0.14f, Assets.I.getRegions(name+"_fall"), Animation.PlayMode.LOOP_PINGPONG)));
		fallingAnim.setOrigin(Align.center);
		attackingAnim = new AnimatedImage(new AnimatedDrawable(new Animation(ability1.durationMax/8, Assets.I.getRegions(name+"_attack"), Animation.PlayMode.NORMAL)));
		attackingAnim.setOrigin(Align.center);
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
		switch (i) {
		case 1:
			ability1.activate();
			break;
		case 2:
			ability2.activate();
			break;
		case 3:
			ability3.activate();
			break;
		default:
			break;
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
		Array<Enemy> enemies = level.getEnemiesInRadius(400);
		Array<Potion> potions = level.getPotionsInRadius(400);
		Rectangle hitbox = getHitBox();
		for (Enemy enemy : enemies) {
			Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
			if (hitbox.overlaps(enemyRect) && enemy.isAlive()) {
				//enemy.die();//TODO Player dies!!
				System.out.println("Enemy collision");
			}
		}
		for (Potion p : potions) {
			Rectangle potionRect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
			if (p.isVisible() && hitbox.overlaps(potionRect)){
				p.collected();
				//TODO various potions
				ability1.increaseEnergy(1);
				ability2.increaseEnergy(1);
				ability3.increaseEnergy(1);
			}
		}
	}

	public Rectangle getHitBox() {
		int offset = 30; //slightly smaller hitbox of the player as his sprite.
		Rectangle hitBox = new Rectangle(getX()+offset, getY()+offset, getWidth()-offset*2, getHeight()-offset*2);
		return hitBox;
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
		//Update all attacks
		ability1.update(delta);
		ability2.update(delta);
		ability3.update(delta);
		//Decide which animation is displayed
		switch (currState) {
		case DOUBLEJUMPING:
			doubleJumpingAnim.act(delta);
			setDrawable(doubleJumpingAnim.getDrawable());
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
			fallingAnim.act(delta);
			setDrawable(fallingAnim.getDrawable());
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
		if (getY() < oldYPos && !isAttacking()) {
			//Player is falling, if his y-position is lowered
			//and he was previously running.
			setFalling();
		}
		//Player can't fall under/below the ground
		if (getY() < Constants.OFFSET_TO_GROUND) { //land
			setRunnig();
			setY(Constants.OFFSET_TO_GROUND);
		}
		checkCollision();
		checkEntityCollision();
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
		attackingAnim.reset();
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
