package chal.dat255.tkp.model;

import chal.dat255.tkp.view.TKPState;
import android.graphics.RectF;

public class TPKMovementModel {
	// possition of the androitchi
	private RectF mPossRect;
	
	//possition of thought bubbles
	private RectF rightThougtBubble;
	private RectF leftThougtBubble;
	
	// Screen Offsets
	private float mXOffset = 0, mYOffset = 0, mXStep = 0, mYStep = 0,
			mXPixels = 0, mYPixels = 0;
	//Screen size
	private int width;
	private int height;
	
	//current state
	private TKPState state;

	public TPKMovementModel() {
		mPossRect = new RectF();
		rightThougtBubble = new RectF();
		leftThougtBubble= new RectF();
	}
	
	public RectF getmPossRect() {
		return mPossRect;
	}

	public void setmPossRect(RectF mPossRect) {
		this.mPossRect = mPossRect;
	}

	public void setState(TKPState s) {
		this.state = s;		
	}

	//Set Possitions
	public void setXYPossition(float mCenterX, float mCenterY) {
		mPossRect.set(mCenterX, mCenterY, mCenterX + state.width, mCenterY + state.height);
	}
	public void setXPossition(float mCenterX) {
		mPossRect.set(mCenterX, mPossRect.top, mCenterX+state.width, mPossRect.bottom);
	}
	public void setYPossition(float mCenterY) {
		mPossRect.set(mPossRect.left, mCenterY, mPossRect.right, mCenterY + state.height);
	}
	
	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		mXOffset = xOffset;
		mYOffset = yOffset;
		mXStep = xStep;
		mYStep = yStep;
		mXPixels = xPixels;
		mYPixels = yPixels;
		changeXPossition(mXPixels, mXStep); // TODO Panning working,
		// but bugs when tkp
		// moving towards
		// movement direction
	}
	
	//CHANGE possitions
	public void changeXYPossition(float xChange, float yChange, float xStep, float yStep) {
		float xChangeTot = xChange+width*xStep;
		float yChangeTot = yChange+height*yStep;
		mPossRect.set(xChangeTot, yChangeTot, xChangeTot+state.width, yChangeTot + state.height);
	}
	public void changeXPossition(float xChange, float xStep) {
		float xChangeTot = xChange+width*xStep;
		mPossRect.set(xChangeTot, mPossRect.top, xChangeTot+state.width, mPossRect.bottom);
	}
	public void changeYPossition(float yChange, float yStep) {
		float yChangeTot = yChange+height*yStep;
		mPossRect.set(mPossRect.left, yChangeTot, mPossRect.right, yChangeTot + state.height);
	}
	
	
	public void setSurfaceSize(int width, int height) {
		this.width = width;
		this.height = height;
		
	}

	public TKPState updatePossition() {
		switch (state) {
		case Egg:
			
			break;

		case Eat:
			
			break;

		case Jump:
			
			break;

		case FallAsleep:
			
			break;

		case Toilet:
			
			break;

		case WalkBack:
			if(mPossRect.top > 0) {
				setYPossition(mPossRect.top-10);
			} else {
				return TKPState.WalkForward;
			}
				
			break;

		case WalkForward:
		    if(mPossRect.bottom < height) {
				setYPossition(mPossRect.top+10);
			} else {
				return TKPState.WalkBack;
			}
				
			break;

		case WalkLeft:
			if(mPossRect.left > 0) {
				setXPossition(mPossRect.left-10);
			} else {
				return TKPState.WalkRight;
			}
			break;

		case WalkRight:
			if(mPossRect.right < width ) {
				setXPossition(mPossRect.left+10);
			} else {
				return TKPState.WalkLeft;
			}
			
			break;

		default:
			
			break;
		}
		return null;
	}
	
	public RectF getLeftTBPoss() {
		updateThoughtBubblesPoss();
		return leftThougtBubble;
	}
	public RectF getRightTBPoss() {
		updateThoughtBubblesPoss();
		return rightThougtBubble;
	}
	private void updateThoughtBubblesPoss() {
		int offset = 40;
		leftThougtBubble = new RectF(mPossRect.left-142+offset,  mPossRect.top-85+offset, mPossRect.left+offset, mPossRect.top+offset);
		rightThougtBubble = new RectF(mPossRect.right-offset, mPossRect.top-85+offset, mPossRect.right+142-offset, mPossRect.top+offset);
	}
}
