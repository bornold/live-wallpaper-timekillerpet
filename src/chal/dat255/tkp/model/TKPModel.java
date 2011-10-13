package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.view.AState;
import chal.dat255.tkp.view.TKPSpriteView;

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
	private AState currentState = AState.Egg;

	// Sprite object
	private TKPSpriteView animation = new TKPSpriteView();

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
		
		setState(AState.Egg);
		possition.setXYPossition(width/2.0f, height/2.0f);
		
	}

	/**
	 * If the touch is on sprite, change state
	 */
	public void isTouched(int x, int y) {
		if ((possition.getmPossRect().contains(x, y))) {
			switch (currentState) {
			case Egg:
				setState(AState.Jump);
				break;

			case Jump:
				setState(AState.Toilet);
				break;
				
			case Toilet:
				setState(AState.FallAsleep);
				break;
				
			case FallAsleep:
				setState(AState.Eat);
				break;
				
			case Eat:
				setState(AState.WalkLeft);
				break;

			case WalkLeft:
				setState(AState.WalkRight);
				break;
				
			case WalkRight:
				setState(AState.WalkBack);
				break;

			case WalkBack:
				setState(AState.WalkForward); 
				break;

			case WalkForward:
				setState(AState.WalkLeft);
				break;

			default:
				break;
			}
		}
	}

	public void draw(Canvas c) {
		animation.draw(c, possition.getmPossRect());
	}

	public void update(long gameTime) {
		animation.update(gameTime);

		// Checks if any needs has gone up
		for (Need n : Need.values()) {
			if (gameTime > n.getLastUpdate() + n.getUpdateIntervall()) {
				int amount = (int) ((gameTime - n.getLastUpdate()) / n.getUpdateIntervall()); // amount of cycles that past
												// since last update
				n.increaseNeedLevel(amount);
			}
		}

		//TODO Update movement
		// movement
		if (gameTime > lastUpdateTimer + Varibles.fps) {
			lastUpdateTimer = gameTime;
			AState tempState = possition.updatePossition();
			if (tempState != null) {
				setState(tempState);
			}
	
		}
	}

	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		mXOffset = xOffset;
		mYOffset = yOffset;
		mXStep = xStep;
		mYStep = yStep;
		mXPixels = xPixels;
		mYPixels = yPixels;
		possition.changeXPossition(mXPixels, mXStep); //TODO Panning working, but bugs when tkp moving towards movement direction
		}

	private void setState(AState s) {
		currentState = s;
		possition.setState(s);
		setAnimation();
	}

	private void setAnimation() {
		animation.initialize(
				BitmapFactory.decodeResource(resource, currentState.bitmap),
				currentState.height, currentState.width,
				currentState.frameCount);
	}

	public void setSurfaceSize(int width, int height) {
		this.width = (int) (width*mXStep);
		this.height = (int) (height*mYStep);
		possition.setSurfaceSize(width, height);
		
	}
}
