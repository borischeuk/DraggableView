package com.example.draggableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

public class DraggableCircle extends View {
	
	protected boolean isDraggable;
	
	protected int prevX;
	protected int prevY;
	protected int objRadius;
	protected int objHeight;
	protected int objWidth;
	protected int objColor;
	protected int mTouchSlop;
	
	protected int mScreenHeight;
	protected int mScreenWidth;
	
	protected Rect objPosition;
	protected Region objRegion;
	
	public DraggableCircle(Context context) {
		super(context);
		
		isDraggable = false;
		
		objRadius = 50;
		objHeight = objRadius * 2;
		objWidth = objRadius * 2;
		objColor = Color.CYAN;
		
		final ViewConfiguration viewConfig = ViewConfiguration.get(context);
		mTouchSlop = viewConfig.getScaledTouchSlop();
		Display display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
		mScreenHeight = display.getHeight();
		mScreenWidth = display.getWidth();
		
		objPosition = new Rect(10, 10, objRadius * 2, objRadius * 2);
		objRegion = new Region();
		objRegion.set(objPosition);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if(objRegion.contains(touchX, touchY)) {
				prevX = touchX;
				prevY = touchY;
				isDraggable = true;
			}
		}
		break;
		
		case MotionEvent.ACTION_MOVE: {
			if(isDraggable) {
				final int distX = Math.abs(touchX - prevX);
				final int distY = Math.abs(touchY - prevY);
				
				if(distX > mTouchSlop || distY > mTouchSlop) {
					int deltaX = touchX - prevX;
					int deltaY = touchY - prevY;
					
					if( (objPosition.left + deltaX) > 0 &&
						(objPosition.right + deltaX) < mScreenWidth &&
						(objPosition.top + deltaY) > 0 &&
						(objPosition.bottom + deltaY) < mScreenHeight ) {
						
						objPosition.left = objPosition.left + deltaX;
						objPosition.top = objPosition.top + deltaY;
						objPosition.right = objPosition.left + objWidth;
						objPosition.bottom = objPosition.top + objHeight;
						objRegion.set(objPosition);
						
						prevX = touchX;
						prevY = touchY;
						
						invalidate();
					}
				}
			}
		}
		break;
		
		case MotionEvent.ACTION_UP:
			isDraggable = false;
			break;
		}
		return true;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(objColor);
		canvas.drawCircle(objPosition.exactCenterX(), objPosition.exactCenterY(), objRadius, paint);
		
	}
	
}
