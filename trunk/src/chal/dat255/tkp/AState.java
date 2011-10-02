package chal.dat255.tkp;

public enum AState {
	Egg (0,0,0,0), 
	Normal (R.drawable.normal, 22, 24, 7), 
	Hungry (0,0,0,0),
	VeryHungry (0,0,0,0), 
	Thinking (R.drawable.bubble, 32, 30, 2);

	public final int bitmap;
	public final int height;
	public final int width;
	public final int frameCount;
	
	AState(int theBitmap, int width, int height, int frameCount) {
		this.bitmap = theBitmap;
		this.width = width;
		this.height = height;
		this.frameCount = frameCount;
	}
}