package chal.dat255.tkp.view;

import chal.dat255.tkp.R;

public enum ThoughBubbles {
	HungerNone (R.drawable.cloudright1),
	HungerNormal (R.drawable.cloudright2),
	HungerMore (R.drawable.cloudright3),
	HungerVery (R.drawable.cloudright4), 
	HungerCritical (R.drawable.cloudright5), 
	SleepNone (R.drawable.cloudleft1), 
	SleepNormal (R.drawable.cloudleft2),
	SleepMore (R.drawable.cloudleft3),
	SleepVery (R.drawable.cloudleft4),
	SleepCritical (R.drawable.cloudleft5);
	

	public final int bitmap;
	public final int width = 340;
	public final int height = 567;
	
	ThoughBubbles(int theBitmap) {
		this.bitmap = theBitmap;
	}
}