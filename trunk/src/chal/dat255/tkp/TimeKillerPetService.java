package chal.dat255.tkp;

import chal.dat255.tkp.model.TKPModel;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * http://developer.android.com/reference/android/service/wallpaper/
 * WallpaperService.html A wallpaper service is responsible for showing a live
 * wallpaper behind applications that would like to sit on top of it. This
 * service object itself does very little -- its only purpose is to generate
 * instances of WallpaperService.Engine as needed. Implementing a wallpaper thus
 * involves subclassing from this, subclassing an Engine implementation, and
 * implementing onCreateEngine() to return a new instance of your engine.
 * 
 * @author Jonas Bornold
 * 
 */
public class TimeKillerPetService extends WallpaperService {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new TKPEngine();
	}

	/**
	 * The actual implementation of a wallpaper. A wallpaper service may have
	 * multiple instances running (for example as a real wallpaper and as a
	 * preview), each of which is represented by its own Engine instance.
	 * 
	 * @author jonas
	 * 
	 */
	class TKPEngine extends Engine {

		/**
		 *  The background class
		 *  contains the bitmap and responsible for panning.
		 */
		float mPixels = 0f;
		
		/** 
		 *  Visible boolean, if the screen is visible or not
		 */
		private boolean mVisible = false;

		/**
		 * The Android model, contains the current sprite, movement, needs etc.
		 */
		private TKPModel model;

		/**
		 * Handler for message queue
		 */
		private final Handler mHandler = new Handler();

		/**
		 * The wallpaper loop.
		 */
		private final Runnable mThread = new Runnable() {
			public void run() {
				model.update(System.currentTimeMillis());
				if (mVisible) {
					draw();
				} else {
					// if not visible, it will not update in so and so millis
					mHandler.postDelayed(mThread, Varibles.sleepDelayMillis);
				}
			}
		};

		/**
		 * Constructor for engine, creates a new model. 
		 */
		TKPEngine() {
			model = new TKPModel(getResources());

		}

		/**
		 * Called by the system when the service is first created.
		 * Enables touch events.
		 */
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			// enabling touch events
			setTouchEventsEnabled(true);
		}

		/**
		 * Called by the system to notify a Service that it is no longer used and is being removed. 
		 * Cleans up an resources it holds
		 */
		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mThread);
		}

		/**
		 * Called to inform you of the wallpaper becoming visible or hidden.
		 * Will still update (but not draw).
		 */
		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				draw();
			} else {
				mHandler.removeCallbacks(mThread);
				mHandler.postDelayed(mThread, Varibles.sleepDelayMillis);
			}
		}
		/**
		 * This is called immediately after any structural changes (format or size) 
		 * have been made to the surface.
		 * Sends the message to the model.
		 */

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			model.setSurfaceSize(width, height);
			draw();
		}

		/**
		 * Called to inform you of the wallpaper's offsets changing within its contain.
		 * Is called when user is "panning".
		 */
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			super.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels,
					yPixels);
			mPixels = xPixels;
			model.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels,
					yPixels);

			draw();
		}
		/**
		 * This is called immediately before a surface is being destroyed.
		 */
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mThread);
		}

		// TODO Use in future for picking up Androiditchi etc.
		/**
		 * Called as the user performs touch-screen interaction with the wallpaper.
		 * Sent to Model.
		 */
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				model.isTouched((int) event.getX(), (int) event.getY());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			} else {

			}
			super.onTouchEvent(event);
		}

		/**
		 * Draw one frame of the animation. This method gets called repeatedly
		 * by posting a delayed Runnable.
		 */
		private void draw() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					c.drawColor(Color.WHITE);
//					c.translate(mPixels, 0f); // panning, this will not work with touch events yet..
					c.drawBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.bg), 0, 0, null);
					model.draw(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			mHandler.removeCallbacks(mThread);
			if (mVisible) {
				mHandler.postDelayed(mThread, Varibles.updateIntervallMillis);
			} else {
				mHandler.postDelayed(mThread, Varibles.sleepDelayMillis);
			}
		}
	}
}