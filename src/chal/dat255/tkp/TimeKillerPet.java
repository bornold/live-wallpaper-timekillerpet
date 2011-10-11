package chal.dat255.tkp;

import chal.dat255.tkp.model.AndroitchiModel;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;


public class TimeKillerPet extends WallpaperService {

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
		
		//coordinates for latest touch
		private float touchX = -1;
		private float touchY = -1;
		
		//visible boolean
		boolean mVisible = false;
		
		//model object
		private AndroitchiModel model;
		
		// handler for message queue
		private final Handler mHandler = new Handler();

		private final Runnable mDrawPattern = new Runnable() {
				public void run() {
					model.update(System.currentTimeMillis());
					draw();
				}
		};
		
		public TKPEngine() {
			model = new AndroitchiModel(getResources());
			

			//TODO Understand Screen size.
			// TO DETECTE SCREEN SIZE
			// Not sure how to use yet but soon enough
		/*	
			DisplayMetrics metrics = new DisplayMetrics();
			Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			display.getMetrics(metrics);

			Rect mRectFrame = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);
			
			// ALTERNETIVLY USE
			
			 getDesiredMinimumHeight();
			 getDesiredMinimumWidth()();
		*/
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
			mHandler.removeCallbacks(mDrawPattern);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				draw();
			} else {
				mHandler.removeCallbacks(mDrawPattern);
			}
		}
		
		//TODO Understand this method to use finding middle
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            //model.setMid(width, height);
            draw();
        }
        
		//TODO Understand this method to use when panning
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
        	super.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
        	
        	//TODO check and redraw for offsets when Panning here---
			/*DisplayMetrics metrics = new DisplayMetrics();
			Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			display.getMetrics(metrics);
			model.offSetChange((int)(xOffset * metrics.widthPixels));
			*/
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
			mHandler.removeCallbacks(mDrawPattern);
		}

        //TODO guess must listen to these events for screen movements
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touchX = event.getX();
				touchY = event.getY();
				model.isTouched((int)touchX, (int)touchY);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				
			}

			else {
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
					model.draw(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			mHandler.removeCallbacks(mDrawPattern);
			if (mVisible) {
				mHandler.postDelayed(mDrawPattern, Varibles.fps / 1000 );
			}
		}
	}
}
