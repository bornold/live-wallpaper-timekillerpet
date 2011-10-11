package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import chal.dat255.tkp.Varibles;
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
	// Screen middle
	private float mCenterX = 0;
	private float mCenterY = 0;

	// The Resources
	private Resources resource;

	// Current state
	private AState currentState = AState.Egg;

	// Sprite object
	private TKPSpriteView animation = new TKPSpriteView();

	// possition of the androitchi
	private RectF mPossRect;

	/**
	 * lastUpdateTimer controls how long between updates.
	 */
	private long lastUpdateTimer;

	/**
	 * Public constructor, must use.
	 */
	public TKPModel(Resources resource) {
		// resource must be set before setState.
		this.resource = resource;
		// Starting position must be set before state.
		moveSprite(mCenterX, mCenterY);
		setState(AState.Egg);
	}

	/**
	 * If the touch is on sprite, change state
	 */
	public void isTouched(int x, int y) {
		if ((mPossRect.contains(x, y))) {
			switch (currentState) {
			case Egg:
				setState(AState.Normal);
				break;

			case Normal:
				setState(AState.Thinking);
				break;

			case Thinking:
				setState(AState.Normal);
				break;

			default:
				break;
			}
		}
	}

	public void draw(Canvas c) {
		animation.draw(c, mPossRect);
	}


	public void setMid(int xMid, int yMid) {
		if (currentState != null) {
			moveSprite(xMid - (currentState.width / 2), yMid
					- (currentState.height / 2));
			// setCoordinateRectangle(xMid, yMid);
		}
	}

	public void update(long gameTime) {
		animation.update(gameTime);

		// Checks if any needs has gone up
		for (Need n : Need.values()) {
			if (gameTime > n.getLastUpdate() + n.getUpdateIntervall()) {
				int amount = (int) ((gameTime - n.getLastUpdate()) / n
						.getUpdateIntervall()); // amount of cycles that past
												// since last update
				n.increaseNeedLevel(amount);
			}
		}

		// Random movement
		if (gameTime > lastUpdateTimer + Varibles.fps) {
			lastUpdateTimer = gameTime;
			// TODO Movement, this is example temp movement. must find out how
			// TODO Movementpattern for each sprite.
	
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
//		moveSprite(mXPixels, mYPixels); //TODO this pan, find out more...
		}

	private void setState(AState s) {
		currentState = s;
		setAnimation();
	}

	private void setAnimation() {
		animation.initialize(
				BitmapFactory.decodeResource(resource, currentState.bitmap),
				currentState.height, currentState.width,
				currentState.frameCount);
	}

	private void moveSprite(float x, float y) {
		this.mPossRect = new RectF(x, y, x + currentState.width, y
				+ currentState.height);
	}

	public void onSurfaceChanged(float midX, float midY) {
		if(mCenterX == 0 && mCenterY == 0) {
			moveSprite(midX, midY);
		}
		mCenterX = midX;
		mCenterY = midY;
	}
}
