package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.utils.Assets;

public class AbilityWidget extends ImageButton {

	public interface AbilityActivationListener {
		void activate(Ability ability);
	}

	public Image energy;

	public static int width = 373; //Width of the whole ability-UI-element
	public Ability ability;
	private int number; //number of the skill-button  
	
	private Vector2 v1 = new Vector2(-width, 0); //Start of the segment
	private Vector2 v2 = new Vector2(-width, 0); //End of the segment

	public AbilityActivationListener listener;

	public AbilityWidget(int n) {
		super(Assets.I.skin, "skill-button"+n);
		this.number = n;
		if (number > 0) { //No energy for "0" ability
			energy = new Image(Assets.I.atlas.findRegion("skill_energy"+number));
			energy.setHeight(energy.getHeight()/2);
			energy.setWidth(energy.getWidth()/2);
			energy.setOrigin(energy.getWidth(), 0);
			this.addActor(energy);
		}
		this.setTouchable(Touchable.enabled);
		//3 abilities in a quarter circle
		//--> each is 30 degrees
		v2 = v2.rotate(-((n - 1)*30));
		v1 = v1.rotate(-(n*30));
	}

	public void setAbilityActivationListener(AbilityActivationListener listener) {
		this.listener = listener;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;

		if (ability != null) {
			//placement of the elements
			//numbers are measured from picture of whole UI-element
			switch(number) {
			case 0:
				setX(width);
				setY(0);
				break;
			case 1:
				setX(0);
				setY(0);
				energy.setX(width-energy.getWidth());
				energy.setY(0);
				break;
			case 2:
				setX(50);
				setY(100);
				energy.setX(width-energy.getWidth()-getX());
				energy.setY(-getY());
				break;
			case 3:
				setX(186);
				setY(173);
				energy.setX(width-energy.getWidth()-getX());
				energy.setY(-getY());
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Test if a point is inside a sector.
	 * Coordinates are relative to the whole UI-element.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInsideSector(float x, float y) {
		Vector2 point = new Vector2(x,  y);
	      //point = localToParentCoordinates(point);
	      point.x = point.x-width;
	      return (isInsideBoundaries(point, 200, width) &&
	    		  vectorsAreClockwise(v2, point) &&
	    		  ! vectorsAreClockwise(v1,point));
	}

	/**
	 * Test if point lies inside a ring.
	 * r1 should be smaller than r2.
	 * @param v given point
	 * @param r1 inner radius
	 * @param r2 outer radius
	 * @return
	 */
	public boolean isInsideBoundaries(Vector2 v, int r1, int r2) {
		return ((v.x*v.x + v.y*v.y < r2*r2) && (v.x*v.x + v.y*v.y > r1*r1) );
	}

	/**
	 * Test if vector a lies clockwise to vector b.
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean vectorsAreClockwise(Vector2 a, Vector2 b) {
		//a.set(-a.y, a.x); // perpendicular to a
		//return b.dot(a) > 0; // test length of projection
		return (-a.x*b.y + a.y*b.x > 0);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (ability != null) {
			if(ability.getMaxEnergy() != 0 && number > 0) {
			energy.setScale(1+((float)ability.getEnergy()/ability.getMaxEnergy()));
			}
		}
	}

	public Ability getAbility() {
		return ability;
	}
}
