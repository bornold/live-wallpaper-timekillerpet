package chal.dat255.tkp;
/**
 * A enum-like class containing the important varibles in the system. 
 * 
 * @author Jonas Bornold
 */
public final class Varibles {
	public final static int fps = 5; // the frames per secound, amount of times the wallpaper will redraw and update model per secound.
	public final static long updateIntervallMillis = 1000/fps; // millisecounds between update
	public final static int sleepDelayMillis = 8*60*1000; //8 min between updates when no visible
	public final static int sleepUpdateIntervall = 8*60*1000; //8 minuts, will need to be feed every 20 min to be at 0
	public final static int sleepRegenerationMultiplier = 3; // amount of time we will regenerate sleep then increase it
	public final static int hungerUpdateIntervall = 8*60*1000; //8 min to max, will fall asleep after 13 hours
	public final static int mealGeneration = 40; // How much each meal should decrease the hunger
}