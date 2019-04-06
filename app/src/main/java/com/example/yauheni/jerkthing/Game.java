package com.example.yauheni.jerkthing;

public class Game {

  private PlayGround playGround;
  private GameView gView;
  private boolean showedYouWIN = false;

  public Game(){
    playGround = new PlayGround();
  }
  public void start(){
    playGround.newBoard();
  }
  public void startNew(){
    playGround.fakeNewBoard();
    showedYouWIN = false;
  }
  public void load(int[][] array, int[][] backArray){
    playGround.createBoard(array, backArray);
  }

  public void stepBack(){
    playGround.stepBack();
  }

  public void swipeLeft(){
    if(!playGround.thereIsNothingToChangeSWIPELEFT()){
      playGround.reserveBoard();
      playGround.swipeBigArrayLeft();
      if (!showedYouWIN) {
        if (playGround.reached2048()) {
          showWin();
        }
      }
      playGround.addNewCell();
      if (playGround.GameIsOver()) {
        showGameOver();
      }
    }
  }
  public void swipeRight(){
    if(!playGround.thereIsNothingToChangeSWIPERIGHT()) {
      playGround.reserveBoard();
      playGround.swipeBigArrayRight();
      if (!showedYouWIN) {
        if (playGround.reached2048()) {
          showWin();
        }
      }
      playGround.addNewCell();
      if (playGround.GameIsOver()) {
        showGameOver();
      }
    }
  }
  public void swipeUp(){
    if(!playGround.thereIsNothingToChangeSWIPEUP()) {
      playGround.reserveBoard();
      playGround.swipeBigArrayUp();
      if (!showedYouWIN) {
        if (playGround.reached2048()) {
          showWin();
        }
      }
      playGround.addNewCell();
      if (playGround.GameIsOver()) {
        showGameOver();
      }
    }
  }
  public void swipeDown(){
    if(!playGround.thereIsNothingToChangeSWIPEDOWN()) {
      playGround.reserveBoard();
      playGround.swipeBigDown();
      if (!showedYouWIN) {
        if (playGround.reached2048()) {
          showWin();
        }
      }
      playGround.addNewCell();
      if (playGround.GameIsOver()) {
        showGameOver();
      }
    }
  }
  /**
   * Something strange...happening here even for me...
   *
   */
  public Integer getBoardValueAt(int row, int col){
    return playGround.get(row,col);
  }
  public Integer getStebBackValueAt(int row, int col){
    return playGround.getStepBack(row, col);
  }
  private void showGameOver(){
    gView.drawGameOver();
  }
  private void showWin(){
    gView.drawWin();
    showedYouWIN = true;
  }
  //TODO did at 2.13 don't judge me. I'm gonna sleep right now
  public void attachView(GameView view){
    this.gView = view;
  }
}

