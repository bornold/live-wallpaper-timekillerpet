package chal.dat255.tkp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class TKPThougtBView {
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
     * mSpriteHeight & mSpriteWidth contain the height and width of the Frame,
     * not the entire bitmap and are used to calculate the size of the source rectangle.
     */
    private int mSpriteHeight;
    private int mSpriteWidth;
    
    /**
     * Constructor default value, use initialize
     */
    public TKPThougtBView() {
        spriteRectangle = new Rect(0,0,0,0);
    }

	/**
     * Initialize function works almost as a constructor and sets the varibles.
     * @param theBitmap actual bitmap containing the animation.
     * @param Height contain the height and width of an Individual Frame 
     * @param Width contain the width and width of an Individual Frame
     * @param theFPS the number of frames we wish to show per second, should be from 3-10 (no need to put ms, just s)
     */
    public void initialize(Bitmap theBitmap, int Height, int Width) {
        mAnimation = theBitmap;
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        spriteRectangle.top = 0;
        spriteRectangle.bottom = mSpriteHeight;
        spriteRectangle.left = 0;
        spriteRectangle.right = mSpriteWidth;
    }
    
    /**
     * Draws the sprite on the provided canvas
     * @param canvas the canvas to be drawn upon
     * @param xPos X position for the left corner
     * @param yPos Y position for the left corner
     */
    public void draw(Canvas canvas, RectF poss) {
        canvas.drawBitmap(mAnimation, spriteRectangle, poss, null);
    }
}
