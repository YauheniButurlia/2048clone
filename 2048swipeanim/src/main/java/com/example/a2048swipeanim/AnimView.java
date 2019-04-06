package com.example.a2048swipeanim;

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
  private final long animationTime = 100;

  private final int SWIPE_LEFT = 1;
  private final int SWIPE_RIGHT = 2;
  private int ANIMATION_TYPE = 0;

  float h,x,y;
  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;
  float shift = 0;
  PointNew[] points = new PointNew[4];
  Cell[] numbers = new Cell[]{new Cell(4),new Cell(4),new Cell(4),new Cell(4)};
  Cell[] prevNums = new Cell[4];
  int[] animations = new int[4];
  Paint paint, numPaint;

  public AnimView(Context context) {
    super(context);

    init();
  }

  private void init(){
    paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(4f);

    numPaint = new Paint();
    numPaint.setTextAlign(Paint.Align.CENTER);
    numPaint.setTextSize(80f);

    for(int i = 0;i < prevNums.length; i++){
      prevNums[i] = new Cell(numbers[i].getValue());
    }
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

    float width = h/3;

    for(int i = 0; i < points.length;i++) {
      canvas.drawRect(points[i].getCoordX() - width,points[i].getCoordY()-width,
              points[i].getCoordX() + width, points[i].getCoordY() + width, paint);
    }

    if(isAnimated) {
      if(ANIMATION_TYPE == SWIPE_LEFT) {
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < points.length; i++) {
          if (prevNums[i].getValue().equals(0)) {
            continue;
          }
          canvas.drawText("" + prevNums[i].getValue(), points[i].getCoordX() -
                  percentageDone * (i - animations[i]) * h, points[i].getCoordY() + 30f, numPaint);
        }
        invalidate();
      }
      if(ANIMATION_TYPE == SWIPE_RIGHT) {
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < points.length; i++) {
          if (prevNums[i].getValue().equals(0)) {
            continue;
          }
          canvas.drawText("" + prevNums[i].getValue(), points[i].getCoordX() +
                  percentageDone * (animations[i] - i) * h, points[i].getCoordY() + 30f, numPaint);
        }
        invalidate();
      }
    } else {
      for(int i = 0; i < points.length;i++){
        if(numbers[i].getValue().equals(0)){
          continue;
        }
        canvas.drawText("" + numbers[i].getValue(),points[i].getCoordX(),points[i].getCoordY()+30f, numPaint);
      }
    }
    if(isAnimated){
      invalidate();
    }
  }

  private void reserveBoard(){
    for (int i = 0; i < 4; i++) {
        prevNums[i].setValue(numbers[i].getValue());
    }
  }
  private void reverseValuesOfArray(int[] array){
    for(int i = 0; i < array.length;i++){
      array[i] = 3 - array[i];
    }
  }
  private void reverseIntArray(int[] array){
    int[] buffer = new int[4];
    for(int i = 0; i < array.length;i++){
      buffer[i] = array[i];
    }
    for(int i = 0; i < array.length;i++){
      array[i] = buffer[array.length-i-1];
    }
  }
  private int[] swipeRightArray(Cell[] array){
    reverseArray(array);
    int[] result = swipeLeftArray(array);
    reverseArray(array);
    reverseIntArray(result);
    reverseValuesOfArray(result);
    return result;
  }
  private void reverseArray(Cell[] array){
    Cell[] preservedArray = new Cell[4];
    for(int i = 0; i < array.length;i++){
      preservedArray[i] = array[i];
    }
    for(int i = 0; i < array.length;i++){
      array[i] = preservedArray[array.length-i-1];
    }
  }
  private void clear(Cell[] array){
    for(Cell cell : array) {
      cell.clearCollapsed();
    }
  }
  private int getClosestCell(Cell[] array, int currentCellIndex){
    for(int i = currentCellIndex-1;i>=0;i--){
      if(!array[i].getValue().equals(0)){
        return i;
      }
    }
    return -1;
  }
  private boolean isThereAnyCellLeftTo(Cell[] array, int currentIndex){
    for(int i = currentIndex-1; i >= 0; i--){
      if(array[i].getValue().equals(0)){
        continue;
      } else {
        return true;
      }
    }
    return false;
  }
  private int[] swipeLeftArray(Cell[] array){
    clear(array);

    int[] result = new int[4];

    for(int i=1;i<array.length;i++){
      Cell currentCell = array[i];
      if(currentCell.getValue().equals(0)){
        continue;
      }
      if(!isThereAnyCellLeftTo(array,i)){
        array[0].setValue(currentCell.getValue());
        currentCell.setNull();
        result[i] = 0;
      } else {
        int closestCellIndex = getClosestCell(array,i);
        Cell closestCell = array[closestCellIndex];
        if(closestCell.getValue().equals(currentCell.getValue())){
          if(closestCell.haventBeenCollapsed()){
            closestCell.doubleValue();
            currentCell.setNull();
            result[i] = closestCellIndex;
          } else {
            array[closestCellIndex+1].setValue(currentCell.getValue());
            currentCell.setNull();
            result[i] = closestCellIndex+1;
          }
        } else {
          if(closestCellIndex+1==i){
            result[i] = i;
            continue;
          } else {
            array[closestCellIndex+1].setValue(currentCell.getValue());
            currentCell.setNull();
            result[i] = closestCellIndex+1;
          }
        }
      }
    }
    return result;
  }

  private int[] getInfoAnim(Cell[] array){
    clear(array);

    int[] result = new int[3];

    for(int i=1;i<array.length;i++){
      Cell currentCell = array[i];
      if(currentCell.getValue().equals(0)){
        continue;
      }
      if(!isThereAnyCellLeftTo(array,i)){
        result[i-1] = 0;
      } else {
        int closestCellIndex = getClosestCell(array,i);
        Cell closestCell = array[closestCellIndex];
        if(closestCell.getValue().equals(currentCell.getValue())){
          if(closestCell.haventBeenCollapsed()){
            result[i-1] = closestCellIndex;
          } else {
            result[i-1] = closestCellIndex+1;
          }
        } else {
          if(closestCellIndex+1==i){
            result[i-1] = i;
            continue;
          } else {
            result[i-1] = closestCellIndex+1;
          }
        }
      }
    }
    return result;
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
      reserveBoard();
      animations = swipeRightArray(numbers);
      Log.d("myLog","" + animations[0] + " " + animations[1] + " " + animations[2] + " " + animations[3]);
      ANIMATION_TYPE = SWIPE_RIGHT;
      startAnim(System.currentTimeMillis());
      invalidate();
    }
  }
  private void swipeLeft(){
    if(!isAnimated){
      reserveBoard();
      animations = swipeLeftArray(numbers);
      Log.d("myLog","" + animations[0] + " " + animations[1] + " " + animations[2] + " " + animations[3]);
      ANIMATION_TYPE = SWIPE_LEFT;
      startAnim(System.currentTimeMillis());
      invalidate();
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
