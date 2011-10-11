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
 * @author Jonas
 *
 */
public class AndroitchiModel {
	
	//The Resources
	private Resources resource;
	
	//Current state
	private AState currentState = AState.Egg;
	
	//Sprite object
	private AndroitchiSpriteView animation = new AndroitchiSpriteView();

	//possition of the androitchi
    private Rect mPossRect;

    /**
     * lastUpdateTimer controls how long between updates. 
     */
    private long lastUpdateTimer;
    
	/**
	 * Public constructor, must use.
	 */
	public AndroitchiModel(Resources resource) {
		//resource must be set before setState.
		this.resource = resource;
		//Starting position must be set before state.
		setCoordinateRectangle(100, 280);
		
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
	public void offSetChange(int changeX){
		if (mPossRect != null) {
			setCoordinateRectangle(mPossRect.left + ((1 - changeX) / 2), mPossRect.top);
		}
		
	}
	
	public void setMid(int xMid, int yMid) {
		if (currentState != null) {
			setCoordinateRectangle(xMid - (currentState.width/2), yMid - (currentState.height/2));
			//setCoordinateRectangle(xMid, yMid);
		}
	}
	
	public void update(long gameTime) {
		animation.update(gameTime);
		
		//Checks if any needs has gone up
		for(Need n: Need.values()) {
			if (gameTime > n.getLastUpdate() + n.getUpdateIntervall()){
				int amount = (int)((gameTime - n.getLastUpdate()) / n.getUpdateIntervall());  //amount of cycles that past since last update
				n.increaseNeedLevel(amount);
			}
		}
		
		//Random movement
    	if(gameTime > lastUpdateTimer + Varibles.fps ) {
    		lastUpdateTimer = gameTime;
    		//TODO Movement, this is example temp movement. must find out how to get screen res to be able to pan
    		if (currentState.equals(AState.Normal)) {
    			int n = 0;
    			int m = 0;
    			if (gameTime % 100 < 10) {m=1;}
    			else if (gameTime % 100 < 20) {m=1; n=-1;}
    			else if (gameTime % 100 < 30) {m=-1;}
    			else if (gameTime % 100 < 40) {m=-1;}
    			else if (gameTime % 100 < 60) {m=-1; n=1;}
    			else if (gameTime % 100 < 70) {n=1;}
    			else if (gameTime % 100 < 80) {n=-1;}
    			else if (gameTime % 100 < 90) {n=-1;}
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
