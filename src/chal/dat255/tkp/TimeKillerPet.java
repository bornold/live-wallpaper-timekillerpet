package chal.dat255.tkp;

import chal.dat255.tkp.model.AndroitchiModel;
import chal.dat255.tkp.view.AndroitchiSpriteView;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


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
		

		
		//cordinats for latest touch
		private float touchX = -1;
		private float touchY = -1;

		//visible boolean
		boolean mVisible = false;
		
		//model object
		private AndroitchiModel model;

		//animation
		AndroitchiSpriteView anim;
		
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
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
			
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawPattern);
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

		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touchX = event.getX();
				touchY = event.getY();
				model.isTouched((int)touchX, (int)touchY);
			} 
			super.onTouchEvent(event);
		}

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
