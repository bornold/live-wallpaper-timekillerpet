package chal.dat255.tkp.model;
//////////////////////////////////////////////////////////
/////////////////TODO FIX AS ABSTRACT CLASS!!////////////
////////////////////////////////////////////////////////
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import chal.dat255.tkp.R;

public class FoodNeed {
	final private long updateIntervall = 4*1000; //Four secounds, used for testing, use *60 for minutes
	
	public enum FLevel {
		None (R.drawable.cloudright1),
		Normal (R.drawable.cloudright2),
		More (R.drawable.cloudright3),
		Very (R.drawable.cloudright4), 
		Critical (R.drawable.cloudright5);
		

		public final int bitmap;
		FLevel(int theBitmap) {
			this.bitmap = theBitmap;
		}
	}

	/**
	 * mAnimation is the variable which will hold the actual bitmap containing the animation.
	 */
	private Bitmap bitmap;
	/**
     * spriteRectangle is the source rectangle variable and 
     * controls which part of the image we are rendering for each frame. 
     */
    private Rect spriteRectangle = new Rect();

	FLevel level = FLevel.None;
	private int needLevel = 0;
	private long lastUpdate = System.currentTimeMillis(); //should really be set when hatched...

	public FoodNeed() {
		needLevel = 0;
		level = FLevel.None;
		lastUpdate = System.currentTimeMillis();
		spriteRectangle = new Rect();
	}
	
    public void setTB(Resources r){
    	bitmap = BitmapFactory.decodeResource(r, level.bitmap);
        spriteRectangle.top = 0;
        spriteRectangle.bottom = bitmap.getHeight();
        spriteRectangle.left = 0;
        spriteRectangle.right = bitmap.getWidth();
    }

	public void increaseNeedLevel(int amount) {
		if (amount > 0) {
			if ((amount + needLevel) > 100) {
				needLevel = 100;
			} else {
				needLevel += amount;
			}
			checkNeedLevel();
		}
	}

	public void decreaseNeedLevel(int amount) {
		if (amount > 0) {
			if ((needLevel - amount) > 0) {
				needLevel -= amount;
			} else {
				needLevel = 0;
			}
			checkNeedLevel();
		}
	}

	public void resetNeedLevel() {
		needLevel = 0;
		checkNeedLevel();
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getUpdateIntervall() {
		return updateIntervall;
	}

	public FLevel getLevel(){
		return this.level;
	}
	
	private void setLevel(FLevel level) {
		this.level = level;
	}
	
	private void checkNeedLevel() {
		if (needLevel > 80) {
			setLevel(FLevel.Critical);
		} else if ( needLevel > 60 ) {
			setLevel(FLevel.Very);
		} else if ( needLevel > 40 ) {
			setLevel(FLevel.More);
		}  else if ( needLevel > 20 ) {
			setLevel(FLevel.Normal);
		}  else {
			setLevel(FLevel.None);
		}
	}
	
    /**
     * Draws the sprite on the provided canvas
     * @param canvas the canvas to be drawn upon
     * @param xPos X position for the left corner
     * @param yPos Y position for the left corner
     */
    public void draw(Canvas canvas, RectF poss) {
        canvas.drawBitmap(bitmap, spriteRectangle, poss, null);
    }
}

