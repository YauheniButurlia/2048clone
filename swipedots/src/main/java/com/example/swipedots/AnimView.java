package com.example.swipedots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaDrmException;
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
  float destination = 0;
  float HI_SPEED;
  float MEDIUM_SPEED;
  float LOW_SPEED;
  float speed = MEDIUM_SPEED;
  PointNew[] points = new PointNew[4];
  Paint paint,rectPaint;
  RectF rect;

  int SWIPE_LEFT = 1;
  int SWIPE_RIGHT = 2;
  int NO_SWIPE = 0;
  int SWIPE_DIRACTION;

  private boolean thirdCircleTouchDown = false;
  private boolean firstCircleTouchDown = false;
  private boolean secondCircleTouchDown = false;
  private boolean fourCircleTouchDown = false;


  public AnimView(Context context) {
    super(context);
    paint = new Paint();
    rectPaint = new Paint();
    rectPaint.setColor(Color.RED);
    rectPaint.setStyle(Paint.Style.STROKE);
    rectPaint.setStrokeWidth(4f);

    rect = new RectF();

    SWIPE_DIRACTION = NO_SWIPE;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    h = getWidth()/6;
    LOW_SPEED = h/40;
    MEDIUM_SPEED = h/20;
    HI_SPEED = h/10;
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

    canvas.drawRect(rect,rectPaint);
    if(SWIPE_DIRACTION == SWIPE_RIGHT){
      if(shift < destination){
        shift += speed;
        invalidate();
      } else {
        SWIPE_DIRACTION = NO_SWIPE;
      }
    }
    if(SWIPE_DIRACTION == SWIPE_LEFT){
      if(shift > destination){
        shift -= speed;
        invalidate();
      } else {
        SWIPE_DIRACTION = NO_SWIPE;
      }
    }

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      xDown = event.getX();
      yDown = event.getY();
      if(touchInsideThirdCircle(xDown,yDown)){
        thirdCircleTouchDown = true;
      }
      if(touchInsideFirstCircle(xDown,yDown)){
        firstCircleTouchDown = true;
      }
      if(touchInsideSecondCircle(xDown,yDown)){
        secondCircleTouchDown = true;
      }
      if(touchInsideFourCircle(xDown,yDown)){
        fourCircleTouchDown = true;
      }
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

      if(touchInsideThirdCircle(xUp, yUp) && thirdCircleTouchDown){
        processThirdCircleTouch();
        return true;
      }
      if(touchInsideFirstCircle(xUp,yUp) && firstCircleTouchDown){
        processFirstCircleTouch();
        return true;
      }
      if(touchInsideSecondCircle(xUp,yUp) && secondCircleTouchDown){
        processSecondCircleTouch();
        return true;
      }
      if(touchInsideFourCircle(xUp,yUp) && fourCircleTouchDown){
        processFourCircleTouch();
        return true;
      }

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
  private boolean touchInsideFourCircle(float x, float y){
    float xCoord = points[3].getCoordX();
    float yCoord = points[3].getCoordY();
    return x >= xCoord - h/4 && y >= yCoord - h/4
            && x <= xCoord + h/4 && y <= yCoord + h/4;
  }
  private boolean touchInsideSecondCircle(float x, float y){
    float xCoord = points[1].getCoordX();
    float yCoord = points[1].getCoordY();
    return x >= xCoord - h/4 && y >= yCoord - h/4
            && x <= xCoord + h/4 && y <= yCoord + h/4;
  }
  private boolean touchInsideFirstCircle(float x, float y){
    float xCoord = points[0].getCoordX();
    float yCoord = points[0].getCoordY();
    return x >= xCoord - h/4 && y >= yCoord - h/4
            && x <= xCoord + h/4 && y <= yCoord + h/4;
  }
  private boolean touchInsideThirdCircle(float x, float y){
    float xCoord = points[2].getCoordX();
    float yCoord = points[2].getCoordY();
    return x >= xCoord - h/4 && y >= yCoord - h/4
            && x <= xCoord + h/4 && y <= yCoord + h/4;
  }
  private void swipeRight(){
    SWIPE_DIRACTION = SWIPE_RIGHT;
    speed = MEDIUM_SPEED;

    if(shift >= 0 && shift < h){
      destination = h;
    }
    if(shift >= h && shift < 2*h){
      destination = 2*h;
    }
    if(shift >= 2*h && shift < 3*h){
      destination = 3*h;
    }

    invalidate();
  }
  private void swipeLeft(){
    SWIPE_DIRACTION = SWIPE_LEFT;
    speed = MEDIUM_SPEED;

    if(shift == 0){
    }
    if(shift > 0 && shift <= h){
      destination = 0;
    }
    if(shift > h && shift <= 2*h){
      destination = h;
    }
    if(shift > 2*h && shift <= 3*h){
      destination = 2*h;
    }

    invalidate();
  }
  private void processThirdCircleTouch(){
    destination = 2*h;

    if(shift == 0){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = MEDIUM_SPEED;
    }
    if(shift == h){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = LOW_SPEED;
    }
    if(shift == 3*h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = LOW_SPEED;
    }

    thirdCircleTouchDown = false;
    invalidate();
  }
  private void processFirstCircleTouch(){
    destination = 0;

    if(shift > 0 && shift <= h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = LOW_SPEED;
    }
    if(shift > h && shift <= 2*h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = MEDIUM_SPEED;
    }
    if(shift > 2*h && shift <= 3*h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = HI_SPEED;
    }

    firstCircleTouchDown = false;
    invalidate();
  }
  private void processSecondCircleTouch(){
    destination = h;

    if(shift == 0){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = LOW_SPEED;
    }
    if(shift == 2*h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = LOW_SPEED;
    }
    if(shift == 3*h){
      SWIPE_DIRACTION = SWIPE_LEFT;
      speed = MEDIUM_SPEED;
    }

    secondCircleTouchDown = false;
    invalidate();
  }
  private void processFourCircleTouch(){
    destination = 3*h;

    if(shift == h){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = MEDIUM_SPEED;
    }
    if(shift == 2*h){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = LOW_SPEED;
    }
    if(shift == 0){
      SWIPE_DIRACTION = SWIPE_RIGHT;
      speed = HI_SPEED;
    }

    fourCircleTouchDown = false;
    invalidate();
  }

}