package chal.dat255.tkp.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import chal.dat255.tkp.AState;
import chal.dat255.tkp.Varibles;
import chal.dat255.tkp.view.AndroitchiSpriteView;

/**
 * Model of androitchi, contains State and possition
 * 
 * @author jonas
 *
 */
public class AndroitchiModel {
	
	//The Resources
	private Resources resource;
	
	//Current state
	private AState currentState = AState.Normal;
	
	//Sprite object
	private AndroitchiSpriteView animation = new AndroitchiSpriteView();

	//possition of the androitchi
    private Rect mPossRect;

    /**
     * lastUpdateTimer controls how long between updates. 
     */
    private long lastUpdateTimer;
    
	/**
	 * Public structor, must use.
	 */
	public AndroitchiModel(Resources resource) {
		//resource must be set before setState.
		this.resource = resource;
		//Starting position must be set before state.
		setCoordinateRectangle(40, 280);
		
		setState(AState.Egg);
		

	}

	/**
	 * If the touch is on sprite, change state
	 */
	public void isTouched(int x, int y) {
		if ((mPossRect.contains(x, y))) {
			switch (currentState) {
			case Egg:
				setState(AState.Normal);
				break;

			case Normal:
				setState(AState.Thinking);
				break;

			case Thinking:
				setState(AState.Normal);
				break;

			default:
				break;
			} 
		}
	}
	
	public void draw(Canvas c){
		animation.draw(c, mPossRect);
	}
	
	public void update(long gameTime) {
		animation.update(gameTime);
        //This is the code that does the checking.
        //If the Game time variable is greater than the uppdatetimer 
        //+ the FPS then what that means is that the amount of time FPS is set to
        //has elapsed and which case is time to change frames.
    	if(gameTime > lastUpdateTimer + Varibles.fps ) {
    		lastUpdateTimer = gameTime;
    		//TODO Movement, this is example temp movement. must find out how to get screen res to be able to pan
    		if (currentState.equals(AState.Normal)) {
    			int n = 0;
    			int m = 0;
    			if (gameTime % 10 == 5) {m=2;} 
    			else if (gameTime % 10 == 1) {m=1;}
    			else if (gameTime % 10 == 2) {m=1; n=-1;}
    			else if (gameTime % 10 == 3) {m=-3;}
    			else if (gameTime % 10 == 4) {m=-1;}
    			else if (gameTime % 10 == 6) {m=-1; n=1;}
    			else if (gameTime % 10 == 7) {n=1;}
    			else if (gameTime % 10 == 8) {n=-1;}
        		setCoordinateRectangle(mPossRect.left+n, mPossRect.top+m);
    		}
        }
	}

	private void setState(AState s) {
		currentState = s;
		setAnimation();
	}
	
	private void setAnimation() {
		animation.initialize(BitmapFactory.decodeResource(resource, currentState.bitmap), currentState.height, currentState.width, currentState.frameCount);
		setCoordinateRectangle(mPossRect.left, mPossRect.top);
	}
	
	private void setCoordinateRectangle(int x, int y) {
		this.mPossRect = new Rect(x, y, x+currentState.width, y+currentState.height);
	}
}
