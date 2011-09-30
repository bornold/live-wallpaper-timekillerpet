package chal.dat255.tkp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
/**
 * Thanks to Stephen Flockton, Toxic Tower Studios
 * for providing a guide on sprites without xml use!
 * @link http://www.droidnova.com/2d-sprite-animation-in-android,471.html
 * @author Jonas Bornold
 *
 */
public class AndroitchiSpriteView {
	/**
	 * mAnimation is the variable which will hold the actual bitmap containing the animation.
	 */
	private Bitmap mAnimation;
	/**
	 * mXPos ad mYPos hold the X and Y screen coordinates for where we want the sprite to be on the screen. 
	 */
    private int mXPos;
    private int mYPos;
    /**
     * mSRectangle is the source rectangle variable and 
     * controls which part of the image we are rendering for each frame. 
     */
    private Rect mSRectangle;
    /**
     * mFPS is the number of frames we wish to show per second, should be from 3-10
     */
    private int mFPS;
    /**
     * mNoOfFrames is simply the number of frames in the sprite sheet we are animating.
     */
    private int mNoOfFrames;
    /**
     * mCurrentFrame keep track of the current frame we are rendering so we can move to the next one in order.
     */
    private int mCurrentFrame;
    /**
     * mFrameTimer controls how long between frames. 
     */
    private long mFrameTimer;
    
    /**
     * mSpriteHeight & mSpriteWidth contain the height and width of an Individual Frame,
     * not the entire bitmap and are used to calculate the size of the source rectangle.
     */
    private int mSpriteHeight;
    private int mSpriteWidth;
    
    /**
     * Constructor default value, use initialize 
     */
    public AndroitchiSpriteView() {
        mSRectangle = new Rect(0,0,0,0);
        mFrameTimer =0;
        mCurrentFrame =0;
        mXPos = 80;
        mYPos = 200;
    }
    public int getmXPos() {
		return mXPos;
	}
	public void setmXPos(int mXPos) {
		this.mXPos = mXPos;
	}
	public int getmYPos() {
		return mYPos;
	}
	public void setmYPos(int mYPos) {
		this.mYPos = mYPos;
	}
	/**
     * Initialize function works almost as a constructor and sets the varibles.
     * @param theBitmap actual bitmap containing the animation.
     * @param Height contain the height and width of an Individual Frame 
     * @param Width contain the width and width of an Individual Frame
     * @param theFPS the number of frames we wish to show per second, should be from 3-10 (no need to put ms, just s)
     * @param theFrameCount keep track of the current frame we are rendering so we can move to the next one in order
     */
    public void initialize(Bitmap theBitmap, int Height, int Width, int theFPS, int theFrameCount) {
        mAnimation = theBitmap;
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        mSRectangle.top = 0;
        mSRectangle.bottom = mSpriteHeight;
        mSRectangle.left = 0;
        mSRectangle.right = mSpriteWidth;
        mFPS = 1000 /theFPS;
        mNoOfFrames = theFrameCount;
    }
    /**
     * Updates the class so the next frame is choosen according to fps.
     */
    public void update(long gameTime) {
        if(gameTime > mFrameTimer + mFPS ) {
            mFrameTimer = gameTime;
            mCurrentFrame +=1;
            //This is the code that does the checking.
            //If the Game time variable is greater than the frametimer 
            //+ the FPS then what that means is that the amount of time FPS is set to
            //has elapsed and which case is time to change frames.
            if(mCurrentFrame >= mNoOfFrames) {
                mCurrentFrame = 0;
            }
        }
        //This code makes sure that the source rectangle is showing the right frame.
        //This is updated by multiplying the sprite width by the current frame to get
        //the left most boundary of the frame and adding the sprite width onto this 
        //value to get the right most boundary.
        mSRectangle.left = mCurrentFrame * mSpriteWidth;
        mSRectangle.right = mSRectangle.left + mSpriteWidth;
    }
    
    /**
     * Draws the current sprite on the provided canvas
     * @param canvas the canvas to be drawn upon
     * @param xPos X position for the left corner
     * @param yPos Y position for the left corner
     */
    
    public void draw(Canvas canvas, int xPos, int yPos) {
        Rect dest = new Rect(xPos, yPos, xPos + mSpriteWidth,
                        yPos + mSpriteHeight);
     
        canvas.drawBitmap(mAnimation, mSRectangle, dest, null);
    }

}