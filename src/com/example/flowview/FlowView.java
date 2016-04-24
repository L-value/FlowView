package com.example.flowview;

import java.util.ArrayList;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FlowView extends View{
	private Paint paint;
	private int current = 0;
	private Bitmap bitmap;
	private int width,height;
	private int size = 0;
	private int id[];
	private Matrix matrix;
	private Bitmap[] bitmaps = new Bitmap[3];
	private Bitmap replace;
	
	
	public FlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.GRAY);
		paint.setAntiAlias(true);
		paint.setDither(true);
	//	paint.setStyle(Paint.Style.STROKE);//空心
		paint.setStrokeWidth(1);
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.flowView);
		size = array.getInteger(R.styleable.flowView_size, 1);
		bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bk01);
		bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bk02);
		bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bk03);
		System.out.println(size);
		id = new int[size];
		matrix = new Matrix();
		animatorSet = new AnimatorSet();
	}
	int i = 0;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		System.out.println(i+++"==="+"width"+widthMeasureSpec+"height"+heightMeasureSpec/MeasureSpec.getSize(heightMeasureSpec)*height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	private boolean first = true;//判断是否第一次ondraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		System.out.println("height"+height+"width"+width);
		replace = bitmaps[i%3];
		if (first) {
			matrix.postScale((float) ((0.0+width)/replace.getWidth()), 1);
			first = false;
		}
		//matrix.setSkew(0.1f, 0.1f);
		bitmap = Bitmap.createBitmap(replace, 0, 0, replace.getWidth(), replace.getHeight(), matrix, true);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		drawCircle(canvas);
		ObjectAnimator obaEnd = ObjectAnimator.ofFloat(this, "alpha", 1f);
		obaEnd.setDuration(1500);
		ObjectAnimator obaBegin = ObjectAnimator.ofFloat(this, "alpha", 0.8f);
		obaBegin.setDuration(1500);
	
		animatorSet.play(obaEnd).before(obaBegin);
		animatorSet.start();
		handler.postDelayed(runThread, 4000);
	}
	
	private void drawCircle(Canvas canvas) {
		if (i%3 == 0) {
			canvas.drawCircle(width - 60, bitmap.getHeight() - 50, 10, paint);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(width - 140, bitmap.getHeight() - 50, 10, paint);
			paint.setColor(Color.GRAY);
			canvas.drawCircle(width - 100, bitmap.getHeight() - 50, 10, paint);
		}else if (i%3 == 1) {
			canvas.drawCircle(width - 60, bitmap.getHeight() - 50, 10, paint);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(width - 100, bitmap.getHeight() - 50, 10, paint);
			paint.setColor(Color.GRAY);
			canvas.drawCircle(width - 140, bitmap.getHeight() - 50, 10, paint);
		}else if(i%3 == 2){
			paint.setColor(Color.WHITE);
			canvas.drawCircle(width - 60, bitmap.getHeight() - 50, 10, paint);
			paint.setColor(Color.GRAY);
			canvas.drawCircle(width - 100, bitmap.getHeight() - 50, 10, paint);
			canvas.drawCircle(width - 140, bitmap.getHeight() - 50, 10, paint);
		}
		// TODO Auto-generated method stub
		
	}
	RunThread runThread = new RunThread();
	static Handler handler = new Handler();
	private AnimatorSet animatorSet;
	class RunThread implements Runnable{ 
		@Override
		public void run() {
			synchronized (ALPHA) {
				i++;
				invalidate();
			}
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Toast.makeText(getContext(), ""+i%3, Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return true;
	}

	
}
