package com.example.animationtry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class AnimView extends View {

  float squareWidth;
  float h,x,y;
  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;
  float shift = 0;
  float shift2 = 0;
  PointNew[] points = new PointNew[4];
  Paint paint,rectPaint, rect2Paint;
  RectF rect;
  RectF rect2;

  boolean swipeLeft = false;
  boolean swipeRight = false;

  public AnimView(Context context) {
    super(context);
    paint = new Paint();
    rectPaint = new Paint();
    rectPaint.setColor(Color.RED);
    rectPaint.setStyle(Paint.Style.STROKE);
    rectPaint.setStrokeWidth(4f);
    rect2Paint = new Paint();
    rect2Paint.setColor(Color.BLUE);
    rect2Paint.setStrokeWidth(4f);
    rect2Paint.setStyle(Paint.Style.STROKE);

    rect = new RectF();
    rect2 = new RectF();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    h = getWidth()/6;
    x = getWidth()/2 - 3*h/2;
    y = getHeight()/2;
    for(int i = 0; i < 4;i++){
      points[i] = new PointNew(x,y);
      x += h;
    }
    for(PointNew p : points) {
      canvas.drawCircle(p.getCoordX(),p.getCoordY(),h/8,paint);
    }
    squareWidth = h/4;

    rect.left = points[0].getCoordX() - squareWidth + shift;
    rect.top = points[0].getCoordY() - squareWidth;
    rect.right = points[0].getCoordX() + squareWidth + shift;
    rect.bottom = points[0].getCoordY() + squareWidth;

    rect2.left = points[0].getCoordX() - squareWidth + shift2;
    rect2.top = points[0].getCoordY() - squareWidth;
    rect2.right = points[0].getCoordX() + squareWidth + shift2;
    rect2.bottom = points[0].getCoordY() + squareWidth;

    canvas.drawRect(rect,rectPaint);
    canvas.drawRect(rect2,rect2Paint);
    if(swipeRight){
      if(shift2 < points[2].getCoordX()-points[0].getCoordX()){
        shift2 += 2*h/30;
        invalidate();
      }
      if(shift < points[3].getCoordX()-points[0].getCoordX()){
        shift += h/10;
        invalidate();
      } else {
        swipeRight = false;
      }
    } else if(swipeLeft){
      if(shift2 > 0){
        shift2 -= 2*h/30;
        invalidate();
      }
      if(shift > 0){
        shift -= h/10;
        invalidate();
      } else {
        swipeLeft = false;
      }
    }

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      xDown = event.getX();
      yDown = event.getY();
      //Log.d("myLog","ACTION_DOWN " + String.valueOf(xDown) + " " + String.valueOf(yDown));
    }
    if (event.getAction() == MotionEvent.ACTION_MOVE) {

      //Log.d("myLog","ACTION_MOVE");
    }
    if (event.getAction() == MotionEvent.ACTION_UP) {
      xUp = event.getX();
      yUp = event.getY();

      xDiff = xDown - xUp;
      yDiff = yDown - yUp;

      if (Math.abs(xDiff) > Math.abs(yDiff)) {
        if (xDiff > 0) {
          Log.d("myLog", "SWIPE LEFT");
          swipeLeft = true;
          swipeRight = false;
          invalidate();
        }
        if (xDiff < 0) {
          Log.d("myLog", "SWIPE RIGHT");
          swipeRight = true;
          swipeLeft = false;
          invalidate();
        }
      } else {
        if (yDiff > 0) {
          Log.d("myLog", "SWIPE UP");
          invalidate();
        }
        if (yDiff < 0) {
          Log.d("myLog", "SWIPE DOWN");
          invalidate();
        }
      }
      //Log.d("myLog","ACTION_UP " + String.valueOf(xUp) + " " + String.valueOf(yUp));
    }
    return true;
  }
}
