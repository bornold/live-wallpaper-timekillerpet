package chal.dat255.tkp.view;

import chal.dat255.tkp.R;
import chal.dat255.tkp.R.drawable;

public enum TKPState {
	//Thinking (R.drawable.thinking)
	Egg (R.drawable.egg, 142, 142, 8),
	Jump (R.drawable.jump, 215, 170, 4), //TODO need to fix pixels on sprite 850/4=212.5
	Toilet (R.drawable.toilet, 142, 142, 10),
	Eat (R.drawable.eat, 142, 147, 8),
	FallAsleep (R.drawable.fallsleep, 142, 142, 3),
	WalkLeft (R.drawable.left, 142, 147, 4),
	WalkRight (R.drawable.right, 142, 147, 4), 
	WalkForward (R.drawable.forward, 142, 142, 4),
	WalkBack (R.drawable.back, 142, 142, 4),
	Thinking (R.drawable.thinking, 142, 142, 1);
	

	public final int bitmap;
	public final int height;
	public final int width;
	public final int frameCount;
	
	TKPState(int theBitmap, int width, int height, int frameCount) {
		this.bitmap = theBitmap;
		this.width = width;
		this.height = height;
		this.frameCount = frameCount;
	}
}