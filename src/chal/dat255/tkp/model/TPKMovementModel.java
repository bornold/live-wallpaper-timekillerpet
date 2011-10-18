package chal.dat255.tkp.model;

import chal.dat255.tkp.view.TKPState;
import android.graphics.RectF;

/**
 * This class handles the movement of the androiditchi.
 * all the logic of where and how to walk is contained here.
 * 
 * @author Jonas Bornold
 *
 */
public class TPKMovementModel {
	// possition of the androitchi
	private RectF mPossRect;
	
	//movement speed
	private final int mSpeedPixels = 10;
	
	/**
	 * Coordiants for the latest touch
	 * if they are set to -1 no movement is active.
	 */
	private int moveXCoordinates =-1;
	private int moveYCoordinates =-1;
	
	/**
	 * number of home screen
	 */
	float mNrOfHomeScreeN = 1;
	
	/**
	 * Position of thought bubbles
	 */
	private RectF rightThougtBubble;
	private RectF leftThougtBubble;
	
	/**
	 *  Screen Offsets Used for panning, not implemented yet
	 */
	private float mXOffset = 0, mYOffset = 0, mXStep = 0, mYStep = 0,
			mXPixels = 0, mYPixels = 0;
	
	/**
	 * Screen size width
	 */
	private int mWidth;
	/**
	 * Screen size height
	 */
	private int mHeight;
	
	/**
	 * Copy of the current state from TKPModel
	 */
	private TKPState state;

	/**
	 * Constructor
	 */
	public TPKMovementModel() {
		mPossRect = new RectF();
		rightThougtBubble = new RectF();
		leftThougtBubble= new RectF();
	}
	
	/**
	 * getter for position rectangle
	 * @return the rectangle representing the position of the androiditchi
	 */
	public RectF getmPossRect() {
		return mPossRect;
	}

	/**
	 * Setter of state, is set by model when the state is changed
	 * @param state the new state to be set
	 */
	public void setState(TKPState state) {
		this.state = state;
	}
	
	/**
	 * this method is called when screen is changed, for example with panning
	 * not implemented yet..
	 * @param xOffset
	 * @param yOffset
	 * @param xStep
	 * @param yStep
	 * @param xPixels
	 * @param yPixels
	 */
	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		mXOffset = xOffset;
		mYOffset = yOffset;
		mXStep = xStep;
		mYStep = yStep;
		mXPixels = xPixels;
		mYPixels = yPixels;
		mNrOfHomeScreeN = ((1/xStep) + 1);
		// TODO Panning NOT working, need to consider number of screens
		//changeXPossition(mXPixels, mNrOfHomeScreeN); 
	}
	
	
	/**
	 * Setter for position in x and y
	 * @param mCenterX position of x
	 * @param mCenterY position of y
	 */
	//Set Possitions
	public void setXYPossition(float mCenterX, float mCenterY) {
		mPossRect.set(mCenterX, mCenterY, mCenterX + state.width, mCenterY + state.height);
	}
	/**
	 * Setter for position in x
	 * @param mCenterX position in x
	 */
	public void setXPossition(float mCenterX) {
		mPossRect.set(mCenterX, mPossRect.top, mCenterX+state.width, mPossRect.bottom);
	}
	/**
	 * Setter for position in y
	 * @param mCenterY position i y
	 */
	public void setYPossition(float mCenterY) {
		mPossRect.set(mPossRect.left, mCenterY, mPossRect.right, mCenterY + state.height);
	}
	
	//CHANGE possitions
	/**
	 * This change position, depending on the current possition. Is used when panning
	 * Not fully implemented yet.
	 * @param xChange
	 * @param yChange
	 * @param xStep
	 * @param yStep
	 */
	public void changeXYPossition(float xChange, float yChange, float xStep, float yStep) {
		float xChangeTot = xChange+mWidth*xStep;
		float yChangeTot = yChange+mHeight*yStep;
		mPossRect.set(xChangeTot, yChangeTot, xChangeTot+state.width, yChangeTot + state.height);
	}
	/**
	 * This change position, depending on the current possition. Is used when panning
	 * Not fully implemented yet.
	 * @param xChange
	 * @param xStep
	 */
	public void changeXPossition(float xChange, float xStep) {
		float xChangeTot = xChange+mWidth*xStep;
		mPossRect.set(xChangeTot, mPossRect.top, xChangeTot+state.width, mPossRect.bottom);
	}
	/**
	 * This change position, depending on the current possition. Is used when panning
	 * Not fully implemented yet.
	 * @param yChange
	 * @param yStep
	 */
	public void changeYPossition(float yChange, float yStep) {
		float yChangeTot = yChange+mHeight*yStep;
		mPossRect.set(mPossRect.left, yChangeTot, mPossRect.right, yChangeTot + state.height);
	}
	
	/**
	 * Setter for the size of the screen
	 * @param width the new width of the screen
	 * @param height the new height of the screen
	 */
	public void setSurfaceSize(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
		
	}

	/**
	 * This is the main method that is called repeatedly when the model is updated,
	 * if android is in a walking state the position is updated.
	 * if androiditchi is at end of screen, the walking direction (The state) is changed by returning a new state to TKPModel
	 * @return if state is changed it return the new sate, else null
	 */
	public TKPState updatePossition() {
		
		//if -1 and -1 move is disabled
		if (moveXCoordinates != -1 && moveYCoordinates != -1) {
			//if moveCoordinate to not inside
			if (!(mPossRect.contains(moveXCoordinates, moveYCoordinates))) {
				//if right of right, walk right
				if (moveXCoordinates  > ((int)mPossRect.right)) {
					if (state != TKPState.WalkRight) {
						return TKPState.WalkRight;
					}
				// if left of left, walk left;
				} else if (moveXCoordinates  < ((int)mPossRect.left)){
					if (state != TKPState.WalkLeft) {
						return TKPState.WalkLeft;
					}
				//if under bottom
				} else if (moveYCoordinates  > ((int)mPossRect.bottom)){
					if (state != TKPState.WalkForward) {
						return TKPState.WalkForward; //TODO Dont work when pressed outside designated walking area..
					}
				//if over top
				} else if (moveYCoordinates  < ((int)mPossRect.top)){
					if (state != TKPState.WalkBack) {
						return TKPState.WalkBack;
					}
				}
			}else {
				moveXCoordinates = -1;
				moveYCoordinates = -1;
			}
		}
		//this switch case change position of the sprite
		// or direction if androiditchi is at end of screen
		switch (state) {
		case WalkBack:
			if(mPossRect.top > mHeight/2) {
				setYPossition(mPossRect.top-mSpeedPixels);
			} else {
				return TKPState.WalkForward;
			}
				
			break;

		case WalkForward:
		    if(mPossRect.bottom < mHeight) {
				setYPossition(mPossRect.top+mSpeedPixels);
			} else {
				return TKPState.WalkBack;
			}
				
			break;

		case WalkLeft:
			if(mPossRect.left > 0){//-(width * (mNrOfHomeScreeN-1)/2)) {
				setXPossition(mPossRect.left-mSpeedPixels);
			} else {
				return TKPState.WalkRight;
			}
			break;

		case WalkRight:
			if(mPossRect.right < (mWidth)){// * (mNrOfHomeScreeN)/2)) {
				setXPossition(mPossRect.left+mSpeedPixels);
			} else {
				return TKPState.WalkLeft;
			}
			
			break;

		default:
			
			break;
		}
		return null;
	}
	
	/**
	 * Whenever a new possition is calculated the thought bubbles position is changed with this private metod
	 */
	private void updateThoughtBubblesPoss() {
		int offset = 40; // offset to get the bubbles closer to the sprite
		// TODO dont staticly write width and height of the bubbles-- must get them some how..
		leftThougtBubble = new RectF(mPossRect.left-142+offset,  mPossRect.top-85+offset, mPossRect.left+offset, mPossRect.top+offset);
		rightThougtBubble = new RectF(mPossRect.right-offset, mPossRect.top-85+offset, mPossRect.right+142-offset, mPossRect.top+offset);
	}
	
	/**
	 * return the left thougt bubble (The sleep thought bubble)
	 * @return rect position of the sleep thougt bubble
	 */
	public RectF getLeftTBPoss() {
		updateThoughtBubblesPoss();
		return leftThougtBubble;
	}
	/**
	 * return the right thought bubble (The food thought bubble)
	 * @return rect position of the food thought bubble
	 */
	public RectF getRightTBPoss() {
		updateThoughtBubblesPoss();
		return rightThougtBubble;
	}
	
	/**
	 * Position andrioditchi should go to next
	 * @param x the x value the andrioditchi should go to
	 * @param y the y value the andrioditchi should go to
	 */
	public void goTo(int x, int y) {
		// TODO Move androiditchi to these cordinates
		moveXCoordinates = x;
		if (y > mHeight/2) {
			moveYCoordinates = (mHeight/2); 
		} else {
			moveYCoordinates = y;
		}
	}
}
