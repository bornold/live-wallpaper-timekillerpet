package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.view.TKPState;
import chal.dat255.tkp.view.TKPSpriteView;

/**
 * Model of androitchi, contains State and possition
 * 
 * @author Jonas
 * 
 */

public class TKPModel {
	// Screen size
	private int width = 0;
	private int height = 0;

	// The Resources
	private Resources resource;

	// Current state
	private TKPState currentState = TKPState.Egg;
	private TKPState lastState = TKPState.Egg;

	// Sprite object
	private TKPSpriteView animation = new TKPSpriteView();

	// Thougt bubble views /!/!/!/!//!/FIX AS ABSTRACT!!!!!/!/!/!/!//!
	private FoodNeed foodTB = new FoodNeed();
	private SleepNeed sleepTB = new SleepNeed();

	// possition of the androitchi
	private TPKMovementModel possition = new TPKMovementModel();

	// lastUpdateTimer controls how long between updates.
	private long lastUpdateTimer;
	private int updateTicker = 0;

	/**
	 * Public constructor, must use.
	 */
	public TKPModel(Resources resource) {
		// resource must be set before setState.
		this.resource = resource;

		setState(TKPState.Egg);
		possition.setXYPossition(width, height);

		foodTB.setTB(resource);
		sleepTB.setTB(resource);

	}

	/**
	 * If the touch is on sprite, change state
	 */
	public void isTouched(int x, int y) {
		if ((possition.getmPossRect().contains(x, y))) {
			switch (currentState) {
			case Egg:
				setState(TKPState.Jump);
				break;
			default:
				setState(TKPState.Thinking);
				break;
			}
		} else if (currentState == TKPState.Thinking) {
			if ((possition.getRightTBPoss().contains(x, y))) {
				setState(TKPState.Eat);
			} else if ((possition.getLeftTBPoss().contains(x, y))) {
				setState(TKPState.FallAsleep);
			} else {
				setState(lastState);
			}
		} else {
			switch (currentState) {
			case Jump:
				setState(TKPState.WalkLeft);
				break;

			case WalkLeft:
				setState(TKPState.WalkRight);
				break;

			case WalkRight:
				setState(TKPState.WalkBack);
				break;

			case WalkBack:
				setState(TKPState.WalkForward);
				break;

			case WalkForward:
				setState(TKPState.WalkLeft);
				break;

			case Eat:
				setState(TKPState.Jump);
				break;

			case FallAsleep:
				setState(TKPState.Jump);
				break;

			default:
				break;
			}
		}
	}

	public void draw(Canvas c) {
		animation.draw(c, possition.getmPossRect());
		if (currentState == TKPState.Thinking) {
			foodTB.draw(c, possition.getRightTBPoss());
			sleepTB.draw(c, possition.getLeftTBPoss());
		}
	}

	public void update(long gameTime) {
		animation.update(gameTime);
		if (gameTime > lastUpdateTimer + Varibles.updateIntervallMillis) {
			lastUpdateTimer = gameTime;

			// /////// THIS IS UGLY!!!! FIX AS ABSTRACT!
			if (currentState == TKPState.Eat) { // if eating
				if (gameTime > (foodTB.getLastUpdate()
						+ Varibles.updateIntervallMillis
						* (TKPState.Eat.frameCount) + 1)) {
					foodTB.decreaseNeedLevel(20);
					foodTB.setLastUpdate(gameTime);
					foodTB.setTB(resource);
					setState(TKPState.Jump);

				}
			} else { // if not eating increase the hunger
				if (gameTime > foodTB.getLastUpdate()
						+ foodTB.getUpdateIntervall()) { // if not eating and
															// time past since
															// last update,
															// increase hunger.
					int amount = (int) ((gameTime - foodTB.getLastUpdate()) / foodTB
							.getUpdateIntervall()); // amount of cycles that
													// past since last update
					foodTB.increaseNeedLevel(amount);
					foodTB.setTB(resource);
					foodTB.setLastUpdate(gameTime);
				}
			}
			if (false) { // if sleeping decrease the sleepiness
				
			} else if (true) { // is not sleeping increase the sleepiness
				if (gameTime > sleepTB.getLastUpdate()
						+ sleepTB.getUpdateIntervall()) {
					int amount = (int) ((gameTime - sleepTB.getLastUpdate()) / sleepTB
							.getUpdateIntervall()); // amount of cycles that
													// past
					// since last update
					sleepTB.increaseNeedLevel(amount);
					sleepTB.setTB(resource);
				}
			}
			if (currentState == TKPState.Jump) {
				updateTicker++;
				if (updateTicker == TKPState.Jump.frameCount){
					updateTicker = 0;
					setState(TKPState.WalkForward);
				}
			} else if (currentState == TKPState.FallAsleep) {
				
				
			}
			// ///////////////////////////////////////////////////////////////
			// WARARWAW!

			// movement updater
			TKPState tempState = possition.updatePossition();
			if (tempState != null) {
				setState(tempState);
			}
		}
	}

	// just push it forward to MovementModel
	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		possition.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels,
				yPixels);
	}

	private void setState(TKPState s) {
		lastState = currentState;
		currentState = s;
		possition.setState(currentState);
		setAnimation();
	}

	private void setAnimation() {
		animation.initialize(
				BitmapFactory.decodeResource(resource, currentState.bitmap),
				currentState.height, currentState.width,
				currentState.frameCount);
	}

	public void setSurfaceSize(int width, int height) {
		this.width = (int) (width);
		this.height = (int) (height);
		possition.setSurfaceSize(width, height);
	}
}
