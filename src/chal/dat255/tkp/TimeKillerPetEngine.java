package chal.dat255.tkp;

import chal.dat255.tkp.model.TKPModel;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class TimeKillerPetEngine extends WallpaperService {

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

	class TKPEngine extends Engine {

		// coordinates for latest touch
		private float touchX = -1;
		private float touchY = -1;

		// visible boolean
		boolean mVisible = false;

		// model object
		private TKPModel model;

		// handler for message queue
		private final Handler mHandler = new Handler();

		private final Runnable mThread = new Runnable() {
			public void run() {
				model.update(System.currentTimeMillis());
				if (mVisible) {
					draw();
				} else {
					mHandler.postDelayed(mThread, Varibles.sleepDelayMillis);
				}
			}
		};

		TKPEngine() {
			model = new TKPModel(getResources());
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			// enabling touch events
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mThread);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				draw();
			} else {
				mHandler.removeCallbacks(mThread);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			model.setSurfaceSize(width, height);
			draw();
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			super.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels,
					yPixels);
			model.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
			draw();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mThread);
		}

		// TODO guess must listen to these events for screen movements
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touchX = event.getX();
				touchY = event.getY();
				model.isTouched((int) touchX, (int) touchY);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			} else {
				touchX = -1;
				touchY = -1;
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
					//TODO Make class for background so it also pan with it.
					c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg), 0, 0, null);
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