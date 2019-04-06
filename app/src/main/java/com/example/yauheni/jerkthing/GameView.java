package com.example.yauheni.jerkthing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class GameView extends View {

  private final float TEXT_SIZE = 80f;
  private final float GRID_CELL_HALF_WIDTH = 100f;

  private Game game;
  private Point[][] coords = new Point[4][4];

  Paint paint;
  Paint sPaint;
  Paint stepBackPaint;
  Paint startNewPaint;
  float x;
  float y;
  float h;

  float xDown,yDown;
  float xUp,yUp;
  float xDiff,yDiff;

  float width,height;

  float xCoordOfStepBackButton, yCoordOfStepBackButton;
  float xStartNew, yStartNew;
  boolean touchDownWasOnStepBackButton = false;
  boolean touchDownWasOnStartNewButton = false;

  boolean gameOver = false;
  boolean gameWin = false;

  private Bitmap bitmapStepBack;
  private Bitmap bitmapNewGame;

  private Rect rectSrcNew;
  private RectF rectDstNew;

  private Rect rectSrcBack;
  private RectF rectDstBack;

  private Context context;

  private Animation transitionAnimation;
  private Animation collideAndAppearAnimation;

  public GameView(Context context, Game game){
    super(context);

    this.context = context;

    paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setTextSize(TEXT_SIZE);
    paint.setTextAlign(Paint.Align.CENTER);

    sPaint = new Paint();
    sPaint.setColor(Color.BLACK);
    sPaint.setStrokeWidth(4f);
    sPaint.setStyle(Paint.Style.STROKE);

    stepBackPaint = new Paint();
    startNewPaint = new Paint();

    this.game = game;

    bitmapStepBack = BitmapFactory.decodeResource(getResources(),R.drawable.double_left_52);
    bitmapNewGame = BitmapFactory.decodeResource(getResources(),R.drawable.restart_52);

    init();

    rectSrcNew = new Rect(0,0,bitmapNewGame.getWidth(),bitmapNewGame.getHeight());
    rectSrcBack = new Rect(0,0,bitmapStepBack.getWidth(), bitmapStepBack.getHeight());

    rectDstNew = new RectF(xStartNew - h/3, yStartNew-h/3, xStartNew + h/3, yStartNew + h/3);
    rectDstBack = new RectF(xCoordOfStepBackButton - h/3, yCoordOfStepBackButton - h/3,
            xCoordOfStepBackButton + h/3, yCoordOfStepBackButton + h/3);

    transitionAnimation = new Animation(70);
    collideAndAppearAnimation = new Animation(90);
  }

  private void init(){

    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    android.graphics.Point point = new android.graphics.Point();
    display.getSize(point);

    width = point.x;
    height = point.y;

    h = width/6;
    x = width/2 - 15*h/8;
    for(int i = 0; i < 4; i++){
      y = height/2 - 15*h/8;
      for (int j = 0; j < 4; j++) {
        coords[j][i] = new Point(x,y);
        y += 5*h/4;
      }
      x += 5*h/4;
    }

    xCoordOfStepBackButton = width/2 + 15*h/8;
    yCoordOfStepBackButton = height/2 - 25*h/8; //width
    xStartNew = xCoordOfStepBackButton - 5*h/6;
    yStartNew = yCoordOfStepBackButton;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawGrid(canvas,100f);
    drawCells(canvas);
    drawStartNewGameButton(canvas);
    drawStepBackButton(canvas);

    if(gameWin){
      canvas.drawText("You WIN!",width/2,height/2,paint);
      gameWin = false;
    }
    if(gameOver){
      canvas.drawText("GameOver!",width/2,height/2,paint);
    }
  }

  private void drawGrid(Canvas canvas, float t){
    //float t = 100;
    for(Point[] array: coords){
      for(Point p: array){
        canvas.drawRect(p.getCoordX()-t,p.getCoordY()-t,p.getCoordX()+t,p.getCoordY()+t,sPaint);
      }
    }
  }
  private void drawCells(Canvas canvas){
    for(int i = 0; i < PlayGround.SIZE_OF_ARRAY; i++){
      for (int j = 0; j < PlayGround.SIZE_OF_ARRAY; j++){
        if(game.getBoardValueAt(i,j).equals(0)){
          continue;
        }
        canvas.drawText(String.valueOf(game.getBoardValueAt(i,j)),coords[i][j].getCoordX(),
                coords[i][j].getCoordY()+30,paint);
      }
    }
  }
  private void drawStartNewGameButton(Canvas canvas){
    canvas.drawBitmap(bitmapNewGame,rectSrcNew,rectDstNew,startNewPaint);
    //canvas.drawRect(xStartNew - h/3, yStartNew-h/3, xStartNew + h/3, yStartNew + h/3, startNewPaint);
  }
  private void drawStepBackButton(Canvas canvas){
    canvas.drawBitmap(bitmapStepBack,rectSrcBack,rectDstBack,startNewPaint);
    //canvas.drawRect(xCoordOfStepBackButton - h/3, yCoordOfStepBackButton - h/3,xCoordOfStepBackButton + h/3, yCoordOfStepBackButton + h/3,stepBackPaint);
  }
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if(!gameOver) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        xDown = event.getX();
        yDown = event.getY();
        if (touchInsideStepBackButtonArea(xDown, yDown)) {
          touchDownWasOnStepBackButton = true;
        }
        if(touchInsideStartNewButtonArea(xDown, yDown)) {
          touchDownWasOnStartNewButton = true;
        }
        //Log.d("myLog","ACTION_DOWN " + String.valueOf(xDown) + " " + String.valueOf(yDown));
      }
      if (event.getAction() == MotionEvent.ACTION_MOVE) {

        //Log.d("myLog","ACTION_MOVE");
      }
      if (event.getAction() == MotionEvent.ACTION_UP) {
        xUp = event.getX();
        yUp = event.getY();

        if (touchInsideStepBackButtonArea(xUp, yUp) && touchDownWasOnStepBackButton) {
          game.stepBack();
          touchDownWasOnStepBackButton = false;
          invalidate();
          return true;
        }
        if (touchInsideStartNewButtonArea(xUp, yUp) && touchDownWasOnStartNewButton) {
          //context.startActivity(new Intent().setAction(Intent.ACTION_VIEW));
          game.startNew();
          touchDownWasOnStartNewButton = false;
          invalidate();
          return true;
        }

        xDiff = xDown - xUp;
        yDiff = yDown - yUp;

        if (Math.abs(xDiff) > Math.abs(yDiff)) {
          if (xDiff > 0) {
            Log.d("myLog", "SWIPE LEFT");
            game.swipeLeft();
            invalidate();
          }
          if (xDiff < 0) {
            Log.d("myLog", "SWIPE RIGHT");
            game.swipeRight();
            invalidate();
          }
        } else {
          if (yDiff > 0) {
            Log.d("myLog", "SWIPE UP");
            game.swipeUp();
            invalidate();
          }
          if (yDiff < 0) {
            Log.d("myLog", "SWIPE DOWN");
            game.swipeDown();
            invalidate();
          }
        }
        //Log.d("myLog","ACTION_UP " + String.valueOf(xUp) + " " + String.valueOf(yUp));
      }
      return true;
    }
    return true;
  }
  public void drawGameOver(){
    gameOver = true;
  }
  public void drawWin(){
    gameWin = true;
  }
  private boolean touchInsideStepBackButtonArea(float x, float y){
    return x >= xCoordOfStepBackButton - h/3 && x <= xCoordOfStepBackButton + h/3
            && y >= yCoordOfStepBackButton -h/3 && y <= yCoordOfStepBackButton + h/2;
  }
  private boolean touchInsideStartNewButtonArea(float x, float y){
    return x >= xStartNew - h/3 && x <= xStartNew + h/3
            && y >= yStartNew -h/3 && y <= yStartNew + h/2;
  }
}
