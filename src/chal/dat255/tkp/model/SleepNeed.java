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
import chal.dat255.tkp.Varibles;

/**
 * Class represented the sleep need, also contains the sprite Bitmap and a draw method. 
 * 
 * @author jonas
 *
 */
public class SleepNeed {
	final private long updateIntervall = Varibles.sleepUpdateIntervall;
	/**
	 * This enum has the integer representation of the bitmap together with the 
	 * name of each state representation
	 * @author jonas
	 *
	 */
	public enum SLevel {
		None (R.drawable.cloudleft1),
		Normal (R.drawable.cloudleft2),
		More (R.drawable.cloudleft3),
		Very (R.drawable.cloudleft4), 
		Critical (R.drawable.cloudleft5);

		/**
		 * Constructor for the enum.  
		 */
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
	 * The resource is saved so that new bitmap can be stored
	 */
	private Resources resource;
	
	/**
	 * The enum representation of the sleep level.
	 * Can be None, Normal, More, Very, and Critical
	 */
	private SLevel level;
	/**
	 * The the need representation, goes from 0 to 100 and is incremented for each updateIntervall
	 */
	private int needLevel;
	/**
	 * The last updated varible is set each time the system has updated the needs. 
	 */
	private long lastUpdate;

	/**
	 * Constructor
	 */
	public SleepNeed(Resources r) {
		resource  = r;
		needLevel = 0;
		level = SLevel.None;
		lastUpdate = System.currentTimeMillis();
		
	}
	/**
	 * setTB sets the bitmap to the right canvas.
	 */
    private void setTB(){
    	bitmap = BitmapFactory.decodeResource(resource, level.bitmap);
    }
    /**
     * Increases the need with a amount, can not be more then 100,
     * also calls checkNeedLevel to see if the need is to be updated 
     * @param amount The amount the need should increased
     */
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
 /**
  * Decrease the need with a amount, can not be less then 0,
  * also calls checkNeedLeve to see if the need is to be updated 
  * @param amount The amount the need should decreased
  */
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
	/**
	 * reset the level to 0 calls checkNeedLevel.
	 */
	public void resetNeedLevel() {
		needLevel = 0;
		checkNeedLevel();
	}
	/**
	 * Return the last update
	 * @return lastupdate time in millis
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}
	/**
	 * Set the last update 
	 * @param lastUpdate in millisecounds
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	/**
	 * Returns the update interval in millisecound
	 * @return the update interval in millisecound
	 */
	public long getUpdateInterval() {
		return this.updateIntervall;
	}
	/**
	 * Returns the level of represented by the Enum SLevel
	 * @return SLevel enum of the need
	 */
	public SLevel getLevel(){
		return this.level;
	}
	
	/**
	 * Returns the need level in integer 
	 * @return need level, is between 0 and 100
	 */
	public int getNeedLevel() {
		return this.needLevel;
	}
	/**
	 * Private method to update Level according what NeedLevel is at
	 */
	private void checkNeedLevel() {
		if (needLevel > 80) {
			this.level =(SLevel.Critical);
		} else if ( needLevel > 60 ) {
			this.level =(SLevel.Very);
		} else if ( needLevel > 40 ) {
			this.level =(SLevel.More);
		}  else if ( needLevel > 20 ) {
			this.level =(SLevel.Normal);
		}  else {
			this.level =(SLevel.None);
		}
		setTB();
	}
	
	  /**
     * Draws the sprite on the provided canvas
     * @param canvas the canvas to be drawn upon
     * @param poss the position rectangle of where the bitmap should be printed
     */
    public void draw(Canvas canvas, RectF poss) {
        canvas.drawBitmap(bitmap, null, poss, null);
    }
}