package com.example.yauheni.jerkthing.OtherFiles;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.yauheni.jerkthing.Cell;

public class InputListener implements View.OnTouchListener {
  private Cell[][] array;
  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;

  public InputListener(Cell[][] array) {
    this.array = array;
    //Log.d("myLog","New Start");
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
/*
    if(event.getAction() == MotionEvent.ACTION_DOWN){
      xDown = event.getX();
      yDown = event.getY();
      //Log.d("myLog","ACTION_DOWN " + String.valueOf(xDown) + " " + String.valueOf(yDown));
    }
    if(event.getAction() == MotionEvent.ACTION_MOVE){

      //Log.d("myLog","ACTION_MOVE");
    }
    if(event.getAction() == MotionEvent.ACTION_UP){
      xUp = event.getX();
      yUp = event.getY();
      xDiff = xDown-xUp;
      yDiff = yDown - yUp;

      if(Math.abs(xDiff)>Math.abs(yDiff)){
        if(xDiff > 0){
          Log.d("myLog","SWIPE LEFT");
          PlayGround.swipeBigArrayLeft();
          PlayGround.addNewCell();
          v.invalidate();
        }
        if(xDiff < 0){
          Log.d("myLog","SWIPE RIGHT");
          PlayGround.swipeBigArrayRight();
          PlayGround.addNewCell();
          v.invalidate();
        }
      } else {
        if(yDiff > 0){
          Log.d("myLog","SWIPE UP");
          PlayGround.swipeBigArrayUp();
          PlayGround.addNewCell();
          v.invalidate();
        }
        if(yDiff < 0 ){
          Log.d("myLog","SWIPE DOWN");
          PlayGround.swipeBigDown();
          PlayGround.addNewCell();
          v.invalidate();
        }
      }
      //Log.d("myLog","ACTION_UP " + String.valueOf(xUp) + " " + String.valueOf(yUp));
    }*/
    return false;
  }

}
