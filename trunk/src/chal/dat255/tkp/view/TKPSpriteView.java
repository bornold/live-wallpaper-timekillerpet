package chal.dat255.tkp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
/*
 * Thanks to Stephen Flockton, Toxic Tower Studios
 * for providing a guide on sprites without xml use!
 * @link http://www.droidnova.com/2d-sprite-animation-in-android,471.html
 * @author Jonas Bornold
 *
 */
/**
 * 
 * @author jonas
 *
 */
public class TKPSpriteView {
	/**
	 * mAnimation is the variable which will hold the actual bitmap containing the animation.
	 */
	private Bitmap mAnimation;

	/**
     * spriteRectangle is the source rectangle variable and 
     * controls which part of the image we are rendering for each frame. 
     */
    private Rect spriteRectangle;

    /**
     * mNoOfFrames is simply the number of frames in the sprite sheet we are animating.
     */
    private int mNoOfFrames;
    /**
     * mCurrentFrame keep track of the current frame we are rendering so we can move to the next one in order.
     */
    private int mCurrentFrame;
    
    /**
     * mSpriteHeight & mSpriteWidth contain the height and width of an Individual Frame,
     * not the entire bitmap and are used to calculate the size of the source rectangle.
     */
    private int mSpriteHeight;
    private int mSpriteWidth;
    
    /**
     * Constructor default value, use initialize
     */
    public TKPSpriteView() {
        spriteRectangle = new Rect(0,0,0,0);
        mCurrentFrame = 0;
    }
  

	/**
     * Initialize function works almost as a constructor and sets the variables.
     * @param theBitmap actual bitmap containing the animation.
     * @param Height contain the height and width of an Individual Frame 
     * @param Width contain the width and width of an Individual Frame
     * @param theFrameCount keep track of the current frame we are rendering so we can move to the next one in order
     */
    public void initialize(Bitmap theBitmap, int Height, int Width, int theFrameCount) {
        mAnimation = theBitmap;
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        spriteRectangle.top = 0;
        spriteRectangle.bottom = mSpriteHeight;
        spriteRectangle.left = 0;
        spriteRectangle.right = mSpriteWidth;
        mNoOfFrames = theFrameCount;
        mCurrentFrame = 0;
    }
    /**
     * Updates the class so the next frame is chosen.
     */
    public void update() {
        //Switch frame to next
            mCurrentFrame +=1;
            if(mCurrentFrame >= mNoOfFrames) {
                mCurrentFrame = 0;
            }
        //This code makes sure that the source rectangle is showing the right frame.
        //This is updated by multiplying the sprite width by the current frame to get
        //the left most boundary of the frame and adding the sprite width onto this 
        //value to get the right most boundary.
        spriteRectangle.left = mCurrentFrame * mSpriteWidth;
        spriteRectangle.right = spriteRectangle.left + mSpriteWidth;
    }
    
    /**
     * Draws the current sprite on the provided canvas
     * @param canvas the canvas to be drawn upon
     * @param possRect What position the sprite should be drawn on.
     */
    public void draw(Canvas canvas, RectF possRect) {
        canvas.drawBitmap(mAnimation, spriteRectangle, possRect, null);
    }
}
