package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.model.FoodNeed.FLevel;
import chal.dat255.tkp.model.SleepNeed.SLevel;
import chal.dat255.tkp.view.TKPState;
import chal.dat255.tkp.view.TKPSpriteView;

/**
 * Model of androitchi, contains State and behaivor
 * 
 * @author Jonas
 * 
 */

public class TKPModel {

	// The Resources
	private Resources resource;

	// Current state
	private TKPState currentState = TKPState.Egg;
	private TKPState lastState = TKPState.Egg;

	// Sprite object
	private TKPSpriteView animation = new TKPSpriteView();

	// Thougt bubble views 
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
		possition.setXYPossition(120, 280);

		foodTB.setTB(resource);
		sleepTB.setTB(resource);

	}

	/**
	 * If the touch is on sprite, change state
	 */
	public void isTouched(int x, int y) {
		if ((possition.getmPossRect().contains(x, y))) {
			if (currentState == TKPState.Egg) {
				setState(TKPState.Hatch);
			} else if (currentState == TKPState.Thinking){
				setState(lastState);
			} else if ((foodTB.getLevel() != FLevel.None) || (sleepTB.getLevel() != SLevel.None)){
				setState(TKPState.Thinking);
			} 
			else {
				setState(TKPState.Jump);
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
			possition.goTo(x, y);
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
			if (foodTB.getLevel() != FLevel.None){
				foodTB.draw(c, possition.getRightTBPoss());
			}
			if (sleepTB.getLevel() != SLevel.None) {
				sleepTB.draw(c, possition.getLeftTBPoss());	
			}
		}
	}

	public void update(long gameTime) {
		//Check if amount of time has passed since last update, update interval is 1000 / fps
		if (gameTime > lastUpdateTimer + Varibles.updateIntervallMillis) {
			//First updates the animation so the right frame is set for the Androiditchi
			animation.update();
			
			if (currentState != TKPState.Eat) { // if not eating might increase the hunger
				//Same as above but with hunger
				if (gameTime > foodTB.getLastUpdate() + foodTB.getUpdateIntervall()) {
					//Updates it can go long between updates, so we for each update intervall
					//We take the whole division of the of cycle past since last update
					// and increase the need
					int amount = (int) ((gameTime - foodTB.getLastUpdate()) / foodTB
							.getUpdateIntervall());
					foodTB.increaseNeedLevel(amount);
					//update the sprite
					foodTB.setTB(resource);
					//set the new last update time, minus the modolus of the whole division
					int mod = (int) ((gameTime - foodTB.getLastUpdate()) % foodTB
							.getUpdateIntervall());
					foodTB.setLastUpdate(gameTime - mod);
				}
			}
			
			// if sleeping decrease the sleepiness
			if (currentState == TKPState.Sleep) { 
				//If still sleepy decrease sleep
				if (sleepTB.getNeedLevel() > 0) {
					//if right time has passed
					if (gameTime > (sleepTB.getLastUpdate() + sleepTB.getUpdateIntervall())) {
						//decrease with amount of sleep
						int amount = (int) ((gameTime - sleepTB.getLastUpdate()) / sleepTB
								.getUpdateIntervall());
							// sleep is regenerated 3 times as fast as it is you get sleepy
							sleepTB.decreaseNeedLevel(amount*3);
							//update last update
							int mod = (int) ((gameTime - sleepTB.getLastUpdate()) % sleepTB
									.getUpdateIntervall());
							sleepTB.setLastUpdate(gameTime - mod);
							// set Sprite
							sleepTB.setTB(resource);
					}
				// if not sleepy, change state to wake
				} else {
					sleepTB.setTB(resource);
					setState(TKPState.Jump);
				}
			// is not sleeping increase the sleepiness
			} else if (currentState != TKPState.Sleep) {
				//if time has passed since last update
				if (gameTime > sleepTB.getLastUpdate()
						+ sleepTB.getUpdateIntervall()) {
					// amount of cycles that past since last update
					int amount = 
							(int) ((gameTime - sleepTB.getLastUpdate())
									/ sleepTB.getUpdateIntervall());
					//increase sleepiness with amount
					sleepTB.increaseNeedLevel(amount);
					//set last update
					int mod = (int) ((gameTime - sleepTB.getLastUpdate())
									% sleepTB.getUpdateIntervall());
					sleepTB.setLastUpdate(gameTime - mod);
					//update sprite
					sleepTB.setTB(resource);
				}
			}
			// Switch case for time limited updates on different states
			// these state should only go one loop before ending
			// there for updateTicker tracks how many times they have been
			// updated.
			switch (currentState) {
			case Jump:
				updateTicker++;
				if (updateTicker >= TKPState.Jump.frameCount) {
					updateTicker = 0;
					setState(TKPState.WalkForward);
				}
				break;
			case FallAsleep:
				updateTicker++;
				if (updateTicker >= TKPState.FallAsleep.frameCount) {
					updateTicker = 0;
					setState(TKPState.Sleep);
				}
				break;

			case Hatch:
				updateTicker++;
				if (updateTicker >= TKPState.Hatch.frameCount) {
					updateTicker = 0;
					setState(TKPState.Jump);
				}
				break;

			case Eat:
				updateTicker++;
				if (updateTicker >= TKPState.Eat.frameCount) {
					updateTicker = 0;
					foodTB.decreaseNeedLevel(20);
					foodTB.setLastUpdate(gameTime);
					foodTB.setTB(resource);
					setState(TKPState.Jump);
				}
				break;

			default:
				break;
			}

			// movement updater
			// can change state by returning something that is not null
			// it will do so if it cant walk any futher. End of screen.
			TKPState tempState = possition.updatePossition();
			if (tempState != null) {
				setState(tempState);
			}
			
			//Lastly set the last update to new time
			lastUpdateTimer = gameTime;
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
		possition.setSurfaceSize(width, height);
	}
}
