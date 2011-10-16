package chal.dat255.tkp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;


public class BackGroundView {
	private RectF mPossRect = new RectF();
	private Rect mScalRect = new Rect();
	private Bitmap mBackground;
	
	private int mNrOfHomeScreeN = 1;
	
	private int mWidth;
	private int mHeight;

	private float mXMid;
	public BackGroundView(Bitmap theBitmap) {
		this.mBackground = theBitmap;
		
	}
	
	public void setSurfaceSize(int width, int height) {
		mWidth = width;
		mHeight = height;
		
		mXMid = width/2f;
		
	}

	public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		changeXPossition(xPixels, xStep); // Panning
		mNrOfHomeScreeN = (int)((1/xStep) + 1);
	}
	
	public void changeXPossition(float xChange, float xStep) {
		float xChangeTot = xChange+mWidth*xStep;
		mPossRect.set(xChangeTot, mPossRect.top, xChangeTot+mWidth, mPossRect.bottom);
	}
	
	private void setScaleRect(int left, int top, int right, int bottom) {
		mScalRect.set(left, top, right, bottom);
	}

	/**
    * Draws the current sprite on the provided canvas
    * @param canvas the canvas to be drawn upon
    * @param possRect
    */
   public void draw(Canvas canvas, RectF possRect) {
       canvas.drawBitmap(mBackground, mScalRect, mPossRect, null);
   }
}
