package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.model.TKPNeedModel.Level;
import chal.dat255.tkp.view.TKPState;
import chal.dat255.tkp.view.TKPSpriteView;
import chal.dat255.tkp.view.TKPThougtBView;
import chal.dat255.tkp.view.ThoughBubbles;

/**
 * Model of androitchi, contains State and possition
 * 
 * @author Jonas
 * 
 */

public class TKPModel {

	// Screen Offsets
	private float mXOffset = 0, mYOffset = 0, mXStep = 0, mYStep = 0,
			mXPixels = 0, mYPixels = 0;
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

	// Thougt bubble views
	private TKPThougtBView foodThoughtBubble = new TKPThougtBView();
	private TKPThougtBView sleepThoughtBubble = new TKPThougtBView();

	// possition of the androitchi
	private TPKMovementModel possition = new TPKMovementModel();

	// lastUpdateTimer controls how long between updates.
	private long lastUpdateTimer;

	/**
	 * Public constructor, must use.
	 */
	public TKPModel(Resources resource) {
		// resource must be set before setState.
		this.resource = resource;

		setState(TKPState.Egg);
		possition.setXYPossition(width, height);
		setThoughtBubble(TKPNeedModel.Hunger);
		setThoughtBubble(TKPNeedModel.Sleep);

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
			foodThoughtBubble.draw(c, possition.getRightTBPoss());
			sleepThoughtBubble.draw(c, possition.getLeftTBPoss());
		}
	}

	public void update(long gameTime) {
		animation.update(gameTime);

		// Checks if any needs has gone up
		for (TKPNeedModel n : TKPNeedModel.values()) {
			if (gameTime > n.getLastUpdate() + n.getUpdateIntervall()) {
				int amount = (int) ((gameTime - n.getLastUpdate()) / n
						.getUpdateIntervall()); // amount of cycles that past
				// since last update
				n.increaseNeedLevel(amount);
				setThoughtBubble(n);
			}
		}

		// movement updater
		if (gameTime > lastUpdateTimer + Varibles.fps) {
			lastUpdateTimer = gameTime;
			TKPState tempState = possition.updatePossition();
			if (tempState != null) {
				setState(tempState);
			}
		}
	}

	private void setThoughtBubble(TKPNeedModel n) {
		if (n == TKPNeedModel.Hunger) {
			switch (n.level) {
			case None:
				setFoodTB(ThoughBubbles.HungerNone);
				break;
			case More:
				setFoodTB(ThoughBubbles.HungerMore);
				break;
			case Very:
				setFoodTB(ThoughBubbles.HungerVery);
				break;
			case Critical:
				setFoodTB(ThoughBubbles.HungerCritical);
				break;
			default:
				break;
			}
		} else if (n == TKPNeedModel.Sleep) {
			switch (n.level) {
			case None:
				setSleepTB(ThoughBubbles.SleepNone);
				break;
			case More:
				setSleepTB(ThoughBubbles.SleepMore);
				break;
			case Very:
				setSleepTB(ThoughBubbles.SleepVery);
				break;
			case Critical:
				setSleepTB(ThoughBubbles.SleepCritical);
				break;
			default:
				break;
			}
		}
	}

	private void setSleepTB(ThoughBubbles thought) {
		sleepThoughtBubble.initialize(BitmapFactory.decodeResource(resource, thought.bitmap), thought.height, thought.width);	
	}

	private void setFoodTB(ThoughBubbles thought) {
		foodThoughtBubble.initialize(BitmapFactory.decodeResource(resource, thought.bitmap), thought.height, thought.width);		
	}

	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		mXOffset = xOffset;
		mYOffset = yOffset;
		mXStep = xStep;
		mYStep = yStep;
		mXPixels = xPixels;
		mYPixels = yPixels;
		possition.changeXPossition(mXPixels, mXStep); // TODO Panning working,
														// but bugs when tkp
														// moving towards
														// movement direction
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
