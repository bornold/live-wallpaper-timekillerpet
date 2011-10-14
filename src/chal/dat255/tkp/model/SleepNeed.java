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

public class SleepNeed {
	
	public enum SLevel {
		None (R.drawable.cloudleft1),
		Normal (R.drawable.cloudleft2),
		More (R.drawable.cloudleft3),
		Very (R.drawable.cloudleft4), 
		Critical (R.drawable.cloudleft5);
		
		public final int bitmap;
		SLevel(int theBitmap) {
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

	SLevel level = SLevel.None;
	private int needLevel = 0;
	private long lastUpdate = System.currentTimeMillis(); //should really be set when hatched...
	final private long updateIntervall = 4*60*1000; // 4 min

	
	public SleepNeed() {
		needLevel = 0;
		level = SLevel.None;
		lastUpdate = System.currentTimeMillis();
		spriteRectangle = new Rect();
	}
	/**
     * Constructor and sets the varible
     * @param theBitmap actual bitmap containing the animation.
     */
    public SleepNeed(Bitmap theBitmap) {
        bitmap = theBitmap;
        spriteRectangle.top = 0;
        spriteRectangle.bottom = bitmap.getHeight();
        spriteRectangle.left = 0;
        spriteRectangle.right = bitmap.getWidth();
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

	public SLevel getLevel(){
		return this.level;
	}
	
	private void setLevel(SLevel level) {
		this.level = level;
	}
	
	private void checkNeedLevel() {
		if (needLevel > 80) {
			setLevel(SLevel.Critical);
		} else if ( needLevel > 60 ) {
			setLevel(SLevel.Very);
		} else if ( needLevel > 40 ) {
			setLevel(SLevel.More);
		}  else if ( needLevel > 20 ) {
			setLevel(SLevel.Normal);
		}  else {
			setLevel(SLevel.None);
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

