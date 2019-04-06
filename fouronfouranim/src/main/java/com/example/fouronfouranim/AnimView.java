package com.example.fouronfouranim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class AnimView extends View {

  private int currentItem;
  private int previousItem;
  private float percentageDone;
  private boolean isAnimated = false;
  private long timeStartAnimation;
  private final long animationTime = 80;//100

  private final int SWIPE_LEFT = 1;
  private final int SWIPE_RIGHT = 2;
  private final int SWIPE_UP = 3;
  private final int SWIPE_DOWN = 4;
  private final int COLLIDING_AND_NEW_CELL = 5;
  private final int NEWCELL = 6;
  private int ANIMATION_TYPE = 0;

  private final long TRANSITION_ANIMATION_TIME = 80;
  private final long COLLIDE_AND_NEW_CELL_ANIMATION_TIME = 100;


  private static int SIZE_OF_ARRAY = 4;

  private final float TEXT_SIZE = 80f;

  float h,x,y;
  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;
  float shift = 0;
  PointNew[][] coords = new PointNew[4][4];
  Cell[][] numbers;
  Cell[][] prevNums;
  int[][] animations;
  Paint paint, numPaint,squarePaint;

  float radius = 10f;
  float halfWidth = 100f;

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
    numPaint.setTextSize(TEXT_SIZE);

    squarePaint = new Paint();
    squarePaint.setStyle(Paint.Style.FILL);
    squarePaint.setColor(Color.CYAN);

    numbers = generateArray(new int[]{1024,512,256,128,8,16,32,64,4,2,2,0,0,0,0,0});//new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    addNewCell();
    addNewCell();
    prevNums = createBoardFrom();
    reserveBoard();
  }
  @SuppressLint("NewApi")
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawRGB(128,128,128);
    h = getWidth()/6;

    x = getWidth()/2 - 15*h/8;
    for(int i = 0; i < 4; i++){
      y = getHeight()/2 - 15*h/8;
      for (int j = 0; j < 4; j++) {
        coords[j][i] = new PointNew(x,y);
        y += 5*h/4;
      }
      x += 5*h/4;
    }
    for(PointNew[] array: coords){
      for(PointNew p: array){
        //canvas.drawRect(p.getCoordX()-t,p.getCoordY()-t,p.getCoordX()+t,p.getCoordY()+t, paint);

        //API 21+
        canvas.drawRoundRect(p.getCoordX()-halfWidth,p.getCoordY()-halfWidth,p.getCoordX()+halfWidth,p.getCoordY()+halfWidth,radius,radius, paint);
      }
    }

    if(isAnimated) {

      if(ANIMATION_TYPE == SWIPE_LEFT) {
        percentageDone = getPercentageDone(System.currentTimeMillis());

        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (prevNums[i][j].getValue().equals(0)) {
              continue;
            }
            drawCell(canvas,coords[i][j].getCoordX()- percentageDone * (j - animations[i][j]) * h,
                    coords[i][j].getCoordY(),prevNums[i][j], halfWidth, radius);
          }
        }
        if(percentageDone == 1f){
          startAnim(System.currentTimeMillis());
          ANIMATION_TYPE = COLLIDING_AND_NEW_CELL;
        }
        invalidate();
      }
      if(ANIMATION_TYPE == SWIPE_RIGHT) {
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (prevNums[i][j].getValue().equals(0)) {
              continue;
            }

            drawCell(canvas,coords[i][j].getCoordX()+ percentageDone * (animations[i][j] - j) * h,
                    coords[i][j].getCoordY(),prevNums[i][j], halfWidth, radius);
          }
        }
        if(percentageDone == 1f){
          startAnim(System.currentTimeMillis());
          ANIMATION_TYPE = COLLIDING_AND_NEW_CELL;
        }
        invalidate();
      }
      if(ANIMATION_TYPE == SWIPE_UP) {
        percentageDone = getPercentageDone(System.currentTimeMillis());

        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (prevNums[i][j].getValue().equals(0)) {
              continue;
            }
            drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY()-
                    percentageDone * (i - animations[i][j]) * h,prevNums[i][j], halfWidth, radius);
          }
        }
        if(percentageDone == 1f){
          startAnim(System.currentTimeMillis());
          ANIMATION_TYPE = COLLIDING_AND_NEW_CELL;
        }
        invalidate();
      }
      if(ANIMATION_TYPE == SWIPE_DOWN) {
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (prevNums[i][j].getValue().equals(0)) {
              continue;
            }
            drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY()+
                    percentageDone * (animations[i][j] - i) * h,prevNums[i][j], halfWidth, radius);
          }
        }
        if(percentageDone == 1f){
          startAnim(System.currentTimeMillis());
          ANIMATION_TYPE = COLLIDING_AND_NEW_CELL;
        }
        invalidate();
      }

      if(ANIMATION_TYPE == COLLIDING_AND_NEW_CELL){
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (numbers[i][j].getValue().equals(0)) {
              continue;
            }
            if(numbers[i][j].isItNew()) {
              drawCell(canvas, coords[i][j].getCoordX(), coords[i][j].getCoordY(), numbers[i][j],
                      percentageDone * (halfWidth - 10) + 10f, radius, percentageDone * TEXT_SIZE);
            }
            if(numbers[i][j].haventBeenCollapsed() && !numbers[i][j].isItNew()){
              drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j], halfWidth, radius);
            }
            if(!numbers[i][j].haventBeenCollapsed()){

              /*
              drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j],
                      halfWidth + percentageDone*halfWidth/4, radius,TEXT_SIZE + percentageDone*TEXT_SIZE/4);
*/

              if(percentageDone <= 0.5f){
                drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j],
                        halfWidth + percentageDone*halfWidth/2, radius,TEXT_SIZE + percentageDone*TEXT_SIZE/2);
              } else {
                drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j],
                        halfWidth + (1-percentageDone)*halfWidth/2, radius,TEXT_SIZE + (1-percentageDone)*TEXT_SIZE/2);
              }
            }
          }
        }

        /*
        if(percentageDone == 1f){
          startAnim(System.currentTimeMillis());
          ANIMATION_TYPE = NEWCELL;
        }*/
        invalidate();
      }
      /*
      if(ANIMATION_TYPE == NEWCELL){
        percentageDone = getPercentageDone(System.currentTimeMillis());
        for (int i = 0; i < SIZE_OF_ARRAY; i++) {
          for (int j = 0; j < SIZE_OF_ARRAY; j++) {
            if (numbers[i][j].getValue().equals(0)) {
              continue;
            }
            if(numbers[i][j].isItNew()){
              drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j],
                      percentageDone*(halfWidth-10) + 10f, radius, percentageDone*TEXT_SIZE);
            } else {
              drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j], halfWidth, radius);
            }
          }
        }
        invalidate();
      }*/
    } else {
      for (int i = 0; i < SIZE_OF_ARRAY; i++) {
        for (int j = 0; j < SIZE_OF_ARRAY; j++) {
          if (numbers[i][j].getValue().equals(0)) {
            continue;
          }
          drawCell(canvas,coords[i][j].getCoordX(),coords[i][j].getCoordY(),numbers[i][j], halfWidth, radius);
        }
      }
    }
  }

  @SuppressLint("NewApi")
  private void drawCell(Canvas canvas, float xCoord, float yCoord, Cell cell, float halfWidth, float radius){
    canvas.drawRoundRect(xCoord-halfWidth,yCoord-halfWidth,xCoord+halfWidth,yCoord+halfWidth,radius,radius,squarePaint);
    canvas.drawText("" + cell.getValue(), xCoord, yCoord + 30f, numPaint);
  }

  @SuppressLint("NewApi")
  private void drawCell(Canvas canvas, float xCoord, float yCoord, Cell cell, float halfWidth, float radius, float textSize) {
    numPaint.setTextSize(textSize);
    canvas.drawRoundRect(xCoord-halfWidth,yCoord-halfWidth,xCoord+halfWidth,yCoord+halfWidth,radius,radius,squarePaint);
    canvas.drawText("" + cell.getValue(), xCoord, yCoord + 30f, numPaint);
    numPaint.setTextSize(TEXT_SIZE);
  }


  private int amountOfEmptyCells(){
    int amount = 0;
    for (Cell[] subArray: numbers) {
      for(Cell c : subArray){
        if(c.getValue().equals(0)){
          amount++;
        }
      }
    }
    return amount;
  }
  public void addNewCell(){
    int amountOfEmptyCells = amountOfEmptyCells();
    Cell[] tmpArray = new Cell[amountOfEmptyCells];
    int i = 0;
    for (Cell[] subArray: numbers) {
      for(Cell c : subArray){
        if(c.getValue().equals(0)){
          tmpArray[i] = c;
          i++;
        }
      }
    }
    Random rand = new Random();
    int randomCellIndex = rand.nextInt(amountOfEmptyCells);
    int value=2;
    if(rand.nextInt(10)==5){
      value=4;
    }
    tmpArray[randomCellIndex].setValue(value);
    tmpArray[randomCellIndex].setNew();
  }
  private Cell[][] createBoardFrom(){
    Cell[][] prevBoard = generateArray(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
    return prevBoard;
  }
  private void reserveBoard(){
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        prevNums[i][j].setValue(numbers[i][j].getValue());
      }
    }
  }
  private static Cell[][] generateArray(int[] longArray){
    Cell[][] array = new Cell[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    int i = 0;
    for(int x = 0; x < SIZE_OF_ARRAY;x++){
      for(int y = 0; y < SIZE_OF_ARRAY;y++){
        array[x][y] = new Cell(longArray[i]);
        i++;
      }
    }
    return array;
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
      cell.setNotNew();
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

    int[] result = new int[SIZE_OF_ARRAY];

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
          addNewCell();
          invalidate();
        }
        if (xDiff < 0) {
          Log.d("myLog", "SWIPE RIGHT");
          swipeRight();
          addNewCell();
          invalidate();
        }
      } else {
        if (yDiff > 0) {
          Log.d("myLog", "SWIPE UP");
          swipeUp();
          addNewCell();
          invalidate();
        }
        if (yDiff < 0) {
          Log.d("myLog", "SWIPE DOWN");
          swipeDown();
          addNewCell();
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
      animations = swipeBigArrayRight();
      ANIMATION_TYPE = SWIPE_RIGHT;
      startAnim(System.currentTimeMillis());
    }
  }
  private void swipeLeft(){
    if(!isAnimated){
      reserveBoard();
      animations = swipeBigArrayLeft();
      ANIMATION_TYPE = SWIPE_LEFT;
      startAnim(System.currentTimeMillis());
    }

  }
  private void swipeUp(){
    if(!isAnimated){
      reserveBoard();
      animations = swipeUpBig();
      ANIMATION_TYPE = SWIPE_UP;
      startAnim(System.currentTimeMillis());
    }
  }
  private void swipeDown(){
    if(!isAnimated){
      reserveBoard();
      animations = swipeDownBig();
      ANIMATION_TYPE = SWIPE_DOWN;
      startAnim(System.currentTimeMillis());
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
    return animationTime <= (currentTime - timeStartAnimation);
  }
  public int[][] swipeUpBig(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    int[][] anim = new int[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = numbers[x][y];
      }
      anim[y] = swipeLeftArray(tmpArray);
    }
    return transponeArray(anim);
  }
  public int[][] swipeDownBig(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    int[][] anim = new int[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = numbers[x][y];
      }
      anim[y] = swipeRightArray(tmpArray);
    }
    return transponeArray(anim);
  }

  private int[][] transponeArray(int[][] inputArray){
    int[][] result = new int[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        result[j][i] = inputArray[i][j];
      }
    }
    return result;
  }
  public int[][] swipeBigArrayLeft(){
    int[][] anim = new int[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for(int i = 0; i < numbers.length;i++){
      anim[i] = swipeLeftArray(numbers[i]);
    }
    /*
    for(Cell[] arr : numbers){
      swipeLeftArray(arr);
    }
    */
    return anim;
  }
  public int[][] swipeBigArrayRight(){
    int[][] anim = new int[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for(int i = 0; i < numbers.length;i++){
      anim[i] = swipeRightArray(numbers[i]);
    }
    /*
    for(Cell[] arr : numbers){
      swipeRightArray(arr);
    }
    */
    return anim;
  }
}

