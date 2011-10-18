package chal.dat255.tkp.view;

import chal.dat255.tkp.R;
/**
 * Enum That states the models, each state is connected to a int representation of a bitmap
 * height and width o the frame and how many frames the picture contains
 * @author Jonas Bornold
 *
 */
public enum TKPState {
	Egg (R.drawable.egg, 142, 142, 8),
	Hatch (R.drawable.hatch, 142, 142, 4),
	Jump (R.drawable.jump, 215, 170, 4), //TODO need to fix pixels on sprite 850/4=212.5
	Toilet (R.drawable.toilet, 142, 142, 10),
	Eat (R.drawable.eat, 142, 147, 8),
	FallAsleep (R.drawable.fallsleep, 142, 142, 3),
	Sleep (R.drawable.sleep, 142, 170, 3),
	WalkLeft (R.drawable.left, 142, 147, 4),
	WalkRight (R.drawable.right, 142, 147, 4), 
	WalkForward (R.drawable.forward, 142, 142, 4),
	WalkBack (R.drawable.back, 142, 142, 4),
	Thinking (R.drawable.thinking, 142, 142, 1);
	
/**
 * The integer representation off the bitmap
 */
	public final int bitmap;
	/**
	 * the height of the frame
	 */
	public final int height;
	/**
	 * the width of each frame
	 */
	public final int width;
	/**
	 * amount of frames the sprite contains
	 */
	public final int frameCount;
	
	/**
	 * Constructor 
	 */
	private double modifier = 1.5; //TODO find out how to get lcd density. 
	TKPState(int theBitmap, int width, int height, int frameCount) {
		this.bitmap = theBitmap;
		this.width = (int)(width*modifier);
		this.height = (int)(height*modifier);
		this.frameCount = frameCount;
	}
	

}