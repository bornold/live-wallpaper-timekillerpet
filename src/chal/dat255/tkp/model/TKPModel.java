package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.view.TKPState;
import chal.dat255.tkp.view.TKPSpriteView;

/**
 * Model of Androitchi, contains State and behavior.
 * Also Contains state machine. 
 * 
 * @author Jonas Bornold
 * 
 */

public class TKPModel {

	/**
	 *  The Resources is used for BitmapFactory 
	 */
	private Resources resource;

	/**
	 * Current state is the state andrioditchi is in, central to everything
	 */
	private TKPState currentState = TKPState.Egg;
	/**
	 * last state is saved in case we need to switch back
	 */
	private TKPState lastState = TKPState.Egg;

	/**
	 *  The Sprite object
	 */
	private TKPSpriteView animation = new TKPSpriteView();

	/**
	 *  ThoughtBubble view and model for Food need
	 */
	private FoodNeed foodTB;
	/**
	 * ThoughtBubble view and model for Sleep need
	 */
	private SleepNeed sleepTB;

	/**
	 *  Position of the androitchi
	 */
	private TPKMovementModel position = new TPKMovementModel();

	/**
	 *  lastUpdateTimer controls how long between updates.
	 */
	private long lastUpdateTimer;
	/**
	 * updateTicker is used for state that do not repeat (example fallAsleep state)
	 */
	private int updateTicker = 0;

	/**
	 * Public constructor, must use.
	 */
	public TKPModel(Resources resource) {
		// resource must be set before setState.
		this.resource = resource;

		setState(TKPState.Egg);
		//dont know how to find screen before
		//onSurfaceChanged is called, working on it...
		position.setXYPossition(120, 280);
		
		// is gives the views the factory so that they can change sprite by them self
		foodTB = new FoodNeed(resource);
		sleepTB = new SleepNeed(resource);

	}

	/**
	 * Called from engine when the screen is touched.
	 * Andoiditchi response differently if depending on its state
	 * If it has its touch on the androiditchi it shows the need thought bubble
	 * or jumps happily if its satisfied
	 */
	public void isTouched(int x, int y) {
		//first if the sprite (andoiritchi) is touched
		if ((position.getmPossRect().contains(x, y))) {
			//if its an egg change state to hatch
			if (currentState == TKPState.Egg) {
				setState(TKPState.Hatch);
			// if Androiditchi is in stage thinking change back stage
			} else if (currentState == TKPState.Thinking){
				setState(lastState);
			// if not thinking and need levels have gone up a bit, set state thinking
			} else if (foodTB.getLevel() != FoodNeed.FLevel.None 
					|| sleepTB.getLevel() != SleepNeed.SLevel.None){
				setState(TKPState.Thinking);
			} 
			// if none of the above, androiditchi is happy and jumps
			else {
				setState(TKPState.Jump);
			}
			// if not touched on sprite but is in state thinking
		} else if (currentState == TKPState.Thinking) {
			// if food tought bubble is touched, set state eat
			if ((position.getRightTBPoss().contains(x, y))) {
				setState(TKPState.Eat);
			// if food tought bubble is touched, set state fallasleep
			} else if ((position.getLeftTBPoss().contains(x, y))) {
				setState(TKPState.FallAsleep);
			}
		} else if (currentState != TKPState.Egg && currentState != TKPState.Sleep) {
			position.goTo(x, y);
		}
	}

	/**
	 * This method uses to draw the sprite on the canvas,
	 * It also draws thought bubbles if state is thinking.
	 * @param c the canvavs to be drawn on
	 */
	public void draw(Canvas c) {
		animation.draw(c, position.getmPossRect());
		if (currentState == TKPState.Thinking) {
			if (foodTB.getLevel() != FoodNeed.FLevel.None) {
				foodTB.draw(c, position.getRightTBPoss());
			}
			if (sleepTB.getLevel() != SleepNeed.SLevel.None) {
				sleepTB.draw(c, position.getLeftTBPoss());
			}
		}
	}

	/**
	 * This is the main method for the model.
	 * It is called repetedly from the Thread loop to update the model
	 * 
	 * @param gameTime the current time.
	 */
	public void update(long gameTime) {
		//Check if amount of time has passed since last update, update interval is 1000 / fps
		if (gameTime > lastUpdateTimer + Varibles.updateIntervallMillis) {
			//First updates the animation so the right frame is set for the Androiditchi
			animation.update();
			//don't update anything if its not hatched
			if (currentState != TKPState.Egg) {
				if (currentState != TKPState.Eat) { // if not eating might increase the hunger
					//Same as above but with hunger
					if (gameTime > foodTB.getLastUpdate() + foodTB.getUpdateInterval()) {
						// Updates it can go long between updates, 
						// so we for each update interval
						// We take the whole division of the of cycle past since last update
						// and increase the need
						int amount = (int) ((gameTime - foodTB.getLastUpdate()) / foodTB
								.getUpdateInterval());
						foodTB.increaseNeedLevel(amount);
						//set the new last update time, minus the modolus of the whole division
						int mod = (int) ((gameTime - foodTB.getLastUpdate()) % foodTB
								.getUpdateInterval());
						foodTB.setLastUpdate(gameTime - mod);
						if (foodTB.getNeedLevel() > 90) {
							setState(TKPState.Thinking);
						}
					} // end if (gameTime > foodTB.getLastUpdate() + foodTB.getUpdateIntervall()) {
				} // end if (currentState != TKPState.Eat) 
				
				// if sleeping decrease the sleepiness
				if (currentState == TKPState.Sleep || currentState == TKPState.FallAsleep) { 
					//If still sleepy decrease sleep
					if (sleepTB.getNeedLevel() > 0) {
						//if right time has passed
						if (gameTime > (sleepTB.getLastUpdate() + sleepTB.getUpdateInterval())) {
							//decrease with amount of sleep
							int amount = (int) ((gameTime - sleepTB.getLastUpdate()) / sleepTB
									.getUpdateInterval());
								// sleep is regenerated 3 times as fast as it is you get sleepy
								sleepTB.decreaseNeedLevel(amount*Varibles.sleepRegenerationMultiplier);
								//update last update
								int mod = (int) ((gameTime - sleepTB.getLastUpdate()) % sleepTB
										.getUpdateInterval());
								sleepTB.setLastUpdate(gameTime - mod);
						}
					// if not sleepy, change state to wake
					} else {
						setState(TKPState.Jump);
					}
				// is not sleeping increase the sleepiness
				} else if (currentState != TKPState.Sleep) {
					//if time has passed since last update
					if (gameTime > sleepTB.getLastUpdate()
							+ sleepTB.getUpdateInterval()) {
						// amount of cycles that past since last update
						int amount = 
								(int) ((gameTime - sleepTB.getLastUpdate())
										/ sleepTB.getUpdateInterval());
						//increase sleepiness with amount
						sleepTB.increaseNeedLevel(amount);
						//set last update
						int mod = (int) ((gameTime - sleepTB.getLastUpdate())
										% sleepTB.getUpdateInterval());
						sleepTB.setLastUpdate(gameTime - mod);
						//if really tiered, fall asleep, dont fall asleep if thinking or eating
						if (sleepTB.getNeedLevel() == 100 
								&& currentState != TKPState.Thinking 
								&& currentState != TKPState.Eat) {
							setState(TKPState.FallAsleep);
						}
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
						foodTB.resetNeedLevel();
						sleepTB.resetNeedLevel();
						setState(TKPState.Jump);
					}
					break;
	
				case Eat:
					updateTicker++;
					if (updateTicker >= TKPState.Eat.frameCount) {
						updateTicker = 0;
						foodTB.decreaseNeedLevel(Varibles.mealGeneration);
						foodTB.setLastUpdate(gameTime);
						setState(TKPState.Jump);
					}
					break;
	
				default:
					break;
				}
	
				// movement updater
				// can change state by returning something that is not null
				// it will do so if it can't walk any futher = End of screen.
				// will not check if sleeping or falling asleep
				if (currentState != TKPState.Sleep && currentState != TKPState.FallAsleep) {
					TKPState tempState = position.updatePossition();
					if (tempState != null) {
						setState(tempState);
					}
				}
			} // end if (currentState != TKPState.Egg)
			//Lastly set the last update to new time
			lastUpdateTimer = gameTime;
			
		} // end if (gameTime > lastUpdateTimer + Varibles.updateIntervallMillis)
	}

	// just push it forward to MovementModel
	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		position.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels,
				yPixels);
	}

	/**
	 * Change the current state to a new state,
	 * tells the position model of the new state and updates the animation
	 * Saves the last state
	 * @param s the sate to be set
	 */
	private void setState(TKPState s) {
		lastState = currentState;
		currentState = s;
		position.setState(currentState);
		updateTicker = 0;
		setAnimation();
	}

	/**
	 * Just calls the animation initialize to set the new sprite
	 */
	private void setAnimation() {
		animation.initialize(
				BitmapFactory.decodeResource(resource, currentState.bitmap),
				currentState.height, currentState.width,
				currentState.frameCount);
	}

	/**
	 * Sets the new surface size
	 * @param width the width in pixels of the screen
	 * @param height the height in pixels of the screen
	 */
	public void setSurfaceSize(int width, int height) {
		position.setSurfaceSize(width, height);
	}
}
