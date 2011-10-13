package chal.dat255.tkp.model;

import chal.dat255.tkp.view.AState;
import android.graphics.RectF;

public class TPKMovementModel {
	// possition of the androitchi
	private RectF mPossRect;
	
	//Screen size
	private int width;
	private int height;
	
	//current state
	private AState state;

	public TPKMovementModel() {
		mPossRect = new RectF();
	}
	
	public RectF getmPossRect() {
		return mPossRect;
	}

	public void setmPossRect(RectF mPossRect) {
		this.mPossRect = mPossRect;
	}

	public void setState(AState s) {
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

	public AState updatePossition() {
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
				setYPossition(mPossRect.top-5);
			} else {
				return AState.WalkForward;
			}
				
			break;

		case WalkForward:
		    if(mPossRect.bottom < height) {
				setYPossition(mPossRect.top+5);
			} else {
				return AState.WalkBack;
			}
				
			break;

		case WalkLeft:
			if(mPossRect.left > 0) {
				setXPossition(mPossRect.left-5);
			} else {
				return AState.WalkRight;
			}
			break;

		case WalkRight:
			if(mPossRect.right < width ) {
				setXPossition(mPossRect.left+5);
			} else {
				return AState.WalkLeft;
			}
			
			break;

		default:
			
			break;
		}
		return null;		
	}
}
