package chal.dat255.tkp;

import android.graphics.Canvas;
import android.graphics.Paint;
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
		
		
		private Paint painter = new Paint();
		
		public TKPEngine() {
			final Paint paint = painter;
			paint.setColor(0xffA4C639);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touchX = event.getX();
				touchY = event.getY();
				draw();
			} 
			super.onTouchEvent(event);
		}

		void draw() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					c.drawCircle(touchX, touchY, 30, painter);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

		}
	}

}
