package chal.dat255.tkp.model;

import chal.dat255.tkp.view.AState;
import android.graphics.RectF;

public class TPKMovementModel {
	// possition of the androitchi
	private RectF mPossRect;
	
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

	public void setXYPossition(float mCenterX, float mCenterY) {
		mPossRect.set(mCenterX, mCenterY, mCenterX + state.width, mCenterY + state.height);
	}
	public void setXPossition(float mCenterX) {
		mPossRect.set(mCenterX, mPossRect.top, mCenterX+state.width, mPossRect.bottom);
	}
	public void setYPossition(float mCenterY) {
		mPossRect.set(mPossRect.left, mCenterY, mPossRect.right, mCenterY + state.height);
	}

	public void updatePossition() {
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
			setYPossition(mPossRect.top-5);
			break;

		case WalkForward:
			setYPossition(mPossRect.top+5);
			break;

		case WalkLeft:
			setXPossition(mPossRect.left-5);
			break;

		case WalkRight:
			setXPossition(mPossRect.left+5);
			break;

		default:
			break;
		}		
	}
}
