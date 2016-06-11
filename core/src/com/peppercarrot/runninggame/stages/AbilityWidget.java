package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.utils.Assets;

public class AbilityWidget extends ImageButton {

	public interface AbilityActivationListener {
		void activate(Ability ability);
	}

	//private final ProgressBar energy;

	public static int width = 388;
	public Ability ability;
	private int number;
	
	private Vector2 v1 = new Vector2(-width, 0);
	private Vector2 v2 = new Vector2(-width, 0);

	public AbilityActivationListener listener;

	public AbilityWidget(int n) {
		super(Assets.I.skin, "skill-button"+n);
		this.number = n;
		if (n == 0) setTouchable(Touchable.disabled);
		this.setTouchable(Touchable.enabled);
		/*
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (listener != null && ability != null &&
						isInsideSector(x,y)) {
					listener.activate(ability);
				}
				System.out.println(number);
				setChecked(false);
				return true;
			}
		});
		*/
		//energy = new ProgressBar(0, 0, 1, true, Assets.I.skin, "abilityEnergy");
		//energy.setValue(0);
		v2 = v2.rotate(-((n - 1)*30));
		v1 = v1.rotate(-(n*30));
		debug();
	}

	public void setAbilityActivationListener(AbilityActivationListener listener) {
		this.listener = listener;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;

		if (ability != null) {
			//energy.setRange(0, ability.getMaxEnergy());
			//energy.setValue(ability.getEnergy());
			//.width(180).height(130).left();
			switch(number) {
			case 0:
				setX(178);
				setY(1);
				break;
			case 1:
				setX(1);
				setY(1);
				break;
			case 2:
				setX(54);
				setY(102);
				break;
			case 3:
				setX(194);
				setY(176);
				break;
			default:
				break;
			}
			//add(energy).height(130).right().expandY();
		} else {
			//removeActor(button);
			//removeActor(energy);
		}
	}
	
	public boolean isInsideSector(float x, float y) {
		Vector2 point = new Vector2(x,  y);
	      //point = localToParentCoordinates(point);
	      point.x = point.x-width;
	      return (isInsideBoundaries(point, 200, width) &&
	    		  vectorsAreClockwise(v2, point) &&
	    		  ! vectorsAreClockwise(v1,point));
	}

	/**
	 * Point inside boundaries
	 * @param v
	 * @param r1
	 * @param r2
	 * @return
	 */
	public boolean isInsideBoundaries(Vector2 v, int r1, int r2) {
		return ((v.x*v.x + v.y*v.y < r2*r2) && (v.x*v.x + v.y*v.y > r1*r1) );
	}
	
	public boolean vectorsAreClockwise(Vector2 a, Vector2 b) {
		//a.set(-a.y, a.x); // perpendicular to a
		//return b.dot(a) > 0; // test length of projection
		return (-a.x*b.y + a.y*b.x > 0);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (ability != null) {
			//energy.setValue(ability.getEnergy());
			//button.setTouchable(ability.isRunning() ? Touchable.disabled : Touchable.enabled);
		}
	}

	public Ability getAbility() {
		return ability;
	}
}
