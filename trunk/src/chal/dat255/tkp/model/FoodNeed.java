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
 * Class represented the food need, also contains the sprite Bitmap and a draw method. 
 * 
 * @author jonas
 *
 */
public class FoodNeed {
	final private long updateIntervall = Varibles.hungerUpdateIntervall;
	
	/**
	 * This enum has the integer representation of the bitmap together with the 
	 * name of each state representation
	 * @author jonas
	 *
	 */
	public enum FLevel {
		None (R.drawable.cloudright1),
		Normal (R.drawable.cloudright2),
		More (R.drawable.cloudright3),
		Very (R.drawable.cloudright4), 
		Critical (R.drawable.cloudright5);

		/**
		 * Constructor for the enum.  
		 */
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
	 * The resource is saved so that new bitmap can be stored
	 */
	private Resources resource;
	
	/**
	 * 
	 * The enum representation of the hunger level.
	 * Can be None, Normal, More, Very, and Critical
	 */
	private FLevel level;
	/**
	 * The the need representation, goes from 0 to 100 and is incremented for each updateintervall
	 */
	private int needLevel;
	/**
	 * The last updated varible is set each time the system has updated the needs. 
	 */
	private long lastUpdate;

	/**
	 * Empty Constructor
	 */
	public FoodNeed(Resources r) {
		resource = r;
		needLevel = 0;
		level = FLevel.None;
		lastUpdate = System.currentTimeMillis();
	}
	
	/**
	 * setTB sets the bitmap to the right canvas.
	 * @param r the resource so the bitmap can be constructed.
	 */
    private void setTB(){
    	bitmap = BitmapFactory.decodeResource(resource, level.bitmap);
    }

    /**
     * Increases the need with a amount, can not be more then 100,
     * also calls checkNeedLeve to see if the need is to be updated 
     * @param amount The amount the need should increse
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
		return updateIntervall;
	}

	/**
	 * Returns the level of represented by the Enum FLecvel
	 * @return FLevel enum of the need
	 */
	public FLevel getLevel(){
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
			this.level =(FLevel.Critical);
		} else if ( needLevel > 60 ) {
			this.level =(FLevel.Very);
		} else if ( needLevel > 40 ) {
			this.level =(FLevel.More);
		}  else if ( needLevel > 10 ) {
			this.level =(FLevel.Normal);
		}  else {
			this.level =(FLevel.None);
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

