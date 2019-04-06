package com.example.varradcircles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class AnimView extends View {

  private int currentItem;
  private int previousItem;
  private float percentageDone;
  private boolean isAnimated = false;
  private long timeStartAnimation;
  private final long animationTime = 200;

  private final int MOVE_ANIMATION = 1;
  private final int BORDER_ANIMATION = 2;
  private int ANIMATION_TYPE = 0;

  float h,x,y;
  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;
  PointNew[] points = new PointNew[4];
  float[] radiuses = new float[4];
  Paint paint;

  public AnimView(Context context) {
    super(context);
    paint = new Paint();
    currentItem = 0;
  }
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    h = getWidth()/6;
    if(!isAnimated) {
      for (int i = 0; i < radiuses.length; i++) {
        if (i == currentItem) {
          radiuses[i] = h/8;
        } else {
          radiuses[i] = 0;
        }
      }
    } else {
      percentageDone = getPercentageDone(System.currentTimeMillis());
      if(ANIMATION_TYPE == MOVE_ANIMATION){
        for (int i = 0; i < radiuses.length; i++){
          if(i == currentItem){
            radiuses[i] = percentageDone*h/8;
          } else if(i == previousItem){
            radiuses[i] = (1-percentageDone)*h/8;
          } else {
            radiuses[i] = 0;
          }
        }
      }
      if(ANIMATION_TYPE == BORDER_ANIMATION) {
        if (percentageDone < 0.5f) {
          radiuses[currentItem] = percentageDone * h / 10 + h / 8;
        } else {
          radiuses[currentItem] = (1 - percentageDone) * h / 10 + h / 8;
        }
      }
    }

    x = getWidth()/2 - 3*h/2;
    y = getHeight()/2;
    for(int i = 0; i < 4;i++){
      points[i] = new PointNew(x,y);
      x += h;
    }

    for(int i = 0; i < points.length;i++) {
      canvas.drawCircle(points[i].getCoordX(),points[i].getCoordY(),h/10 + radiuses[i],paint);
    }
    if(isAnimated){
      invalidate();
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
          swipeLeft();
        }
        if (xDiff < 0) {
          Log.d("myLog", "SWIPE RIGHT");
          swipeRight();
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
  private void swipeRight(){
    if(!isAnimated){
      if(currentItem<radiuses.length-1){
        previousItem = currentItem++;
        ANIMATION_TYPE = MOVE_ANIMATION;
        startAnim(System.currentTimeMillis());
        invalidate();
      } else {
        ANIMATION_TYPE = BORDER_ANIMATION;
        startAnim(System.currentTimeMillis());
        invalidate();
      }
    }
  }
  private void swipeLeft(){
    if(!isAnimated){
      if(currentItem>0){
        previousItem = currentItem--;
        ANIMATION_TYPE = MOVE_ANIMATION;
        startAnim(System.currentTimeMillis());
        invalidate();
      } else {
        ANIMATION_TYPE = BORDER_ANIMATION;
        startAnim(System.currentTimeMillis());
        invalidate();
      }
    }
  }
  private float getPercentageDone(long currentTime){
    if(AnimationEnded(currentTime)){
      isAnimated = false;
      return 1f;
    }
    float percent = (float)(currentTime - timeStartAnimation)/animationTime;
    return percent;
  }
  private void startAnim(long startTime){
    isAnimated = true;
    timeStartAnimation = startTime;
  }
  private boolean AnimationEnded(long currentTime){
    return animationTime < (currentTime - timeStartAnimation);
  }
}