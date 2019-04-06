package com.example.yauheni.jerkthing;

import java.util.Random;

public class PlayGround {

  public static int SIZE_OF_ARRAY = 4;
  private Cell[][] board;
  private Cell[][] previousStepBoard;
  public static void main(String[] args){

    PlayGround play = new PlayGround();

    Cell[][] cells = generateArray(new int[]{2,4,8,16,8,2,4,2,2,4,2,4,16,8,4,2});
  }

  /**
   * This function allows user to start a new Game.
   * It creates new Board with all ZERO value Cells.
   * Adds two random placed new Cells and creating the previousStepBoard.
   */
  public void newBoard(){
    board = generateArray(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});//new int[]{1024,512,256,128,8,16,32,64,4,2,2,0,0,0,0,0}
    addNewCell();
    addNewCell();
    previousStepBoard = createBoardFrom();
    reserveBoard();
  }

  public void createBoard(int[][] array, int[][] backArray){
    board = generate(array);
    previousStepBoard = generate(backArray);
  }

  private Cell[][] generate(int[][] array){
    Cell[][] result = new Cell[SIZE_OF_ARRAY][SIZE_OF_ARRAY];
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        result[i][j] = new Cell(array[i][j]);
      }
    }
    return result;
  }

  public void setBoards(int[][] array, int[][] backArray){
    //TODO board things. If board is not exist yet...and others
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        board[i][j].setValue(array[i][j]);
      }
    }
  }
  public void fakeNewBoard(){
    clearBoard();
    addNewCell();
    addNewCell();
    reserveBoard();
  }

  public Integer getStepBack(int row, int col){
    return previousStepBoard[row][col].getValue();
  }

  private void clearBoard(){
    for(Cell[] arr : board){
      for(Cell c : arr){
        c.setNull();
      }
    }
  }


  public boolean thereIsNothingToChangeSWIPEDOWN(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = board[x][y];
      }
      if(!thereIsNothingToChangeSmallArraySWIPERIGHT(tmpArray)){
        return false;
      }
    }
    return true;
  }
  public boolean thereIsNothingToChangeSWIPEUP(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = board[x][y];
      }
      if(!thereIsNothingToChangeSmallArraySWIPELEFT(tmpArray)){
        return false;
      }
    }
    return true;
  }
  public boolean thereIsNothingToChangeSWIPERIGHT(){
    for(Cell[] array: board){
      if(!thereIsNothingToChangeSmallArraySWIPERIGHT(array)){
        return false;
      }
    }
    return true;
  }
  public boolean thereIsNothingToChangeSWIPELEFT(){
    for(Cell[] array: board){
      if(!thereIsNothingToChangeSmallArraySWIPELEFT(array)){
        return false;
      }
    }
    return true;
  }


  private boolean thereIsNothingToChangeSmallArraySWIPERIGHT(Cell[] array){
    reverseArray(array);
    boolean result = thereIsNothingToChangeSmallArraySWIPELEFT(array);
    reverseArray(array);
    return result;
  }
  private boolean thereIsNothingToChangeSmallArraySWIPELEFT(Cell[] array){
    if(array[0].getValue().equals(0)){
      if(areThereAnyCellRightTo(array,0)){
        return false;
      } else {
        return true;
      }
    } else {
      for(int i = 0; i < array.length-1;i++){
        if(areThereAnyCellRightTo(array,i)){
          if(array[i+1].getValue().equals(0)){
            return false;
          } else if(array[i+1].getValue().equals(array[i].getValue())){
            return false;
          }
        } else {
          return true;
        }
      }
    }
    return true;
  }


  private boolean areThereAnyCellRightTo(Cell[] array,int currentIndex){
    for(int i = currentIndex+1; i < array.length;i++){
      if(!array[i].getValue().equals(0)){
        return true;
      }
    }
    return false;
  }
  public Integer get(int row, int col){
    return board[row][col].getValue();
  }

  /**
   * Creating reserve copy of the Board to previousStepBoard. To allow stepBack functionality.
   */
  public void reserveBoard(){
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        previousStepBoard[i][j].setValue(board[i][j].getValue());
      }
    }
  }
  private Cell[][] createBoardFrom(){
    Cell[][] prevBoard = generateArray(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
    return prevBoard;
  }

  /**
   * Performing stepBack functionality.
   * All the values of the current board will be rewritten with values from previousStepBoard.
   */
  public void stepBack(){
    for (int i = 0; i < SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < SIZE_OF_ARRAY; j++) {
        board[i][j].setValue(previousStepBoard[i][j].getValue());
      }
    }
  }

  /**
   *
   * @return
   */
  public boolean reached2048(){
    for(Cell[] arr : board){
      for(Cell c : arr){
        if(c.getValue().equals(2048)){
          return true;
        }
      }
    }
    return false;
  }

  /**
   * If player has no move left on current Board the function is returning true.
   * @return true if Game is Over
   */
  public boolean GameIsOver(){
    return HasNoMovesLeftAndRight() && HasNoMovesUpAndDown();
  }
  private boolean HasNoMovesUpAndDown(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = board[x][y];
      }
      if(!lineHasNoMoves(tmpArray)){
        return false;
      }
    }
    return true;
  }
  private boolean HasNoMovesLeftAndRight(){
    for(Cell[] a: board){
      if(!lineHasNoMoves(a)){
        return false;
      }
    }
    return true;
  }
  private boolean lineHasNoMoves(Cell[] array){
    for (int i = 1; i < array.length; i++) {
      if(array[i].getValue().equals(0)){
        return false;
      }
      if(isThereAnyCellLeftTo(array,i)){
        Cell closestCell = array[getClosestCell(array,i)];
        if(closestCell.getValue().equals(array[i].getValue())){
          return false;
        }
      } else {
        return false;
      }

    }
    return true;
  }

  /**
   *
   * Function adding new Cell to the board in random place.
   * Actually it is just changing Cell value of an empty Cell(value = 0) to 2 or 4.
   *
   */
  public void addNewCell(){
    int amountOfEmptyCells = amountOfEmptyCells();

    /*if(amountOfEmptyCells == 0) {
      throw new Exception("There is no empty cells left.");
    }*/
    Cell[] tmpArray = new Cell[amountOfEmptyCells];
    int i = 0;
    for (Cell[] subArray: board) {
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
  }
  private int amountOfEmptyCells(){
    int amount = 0;
    for (Cell[] subArray: board) {
      for(Cell c : subArray){
        if(c.getValue().equals(0)){
          amount++;
        }
      }
    }
    return amount;
  }

  /**
   * The function investivates if there is any place to put an new Cell.
   * @return true if there is
   */
  public boolean isThereAnyPlaceToNewCell(){
    return amountOfEmptyCells()!=0;
  }

  /**
   * Performing Swipe Up on the Board.
   */
  public void swipeBigArrayUp(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = board[x][y];
      }
      swipeLeft(tmpArray);
      //inputNewValues(array,tmpArray,y);
    }
  }
  public void swipeBigDown(){
    Cell[] tmpArray = new Cell[SIZE_OF_ARRAY];
    for (int y = 0; y < SIZE_OF_ARRAY; y++) {
      for (int x = 0; x < SIZE_OF_ARRAY; x++) {
        tmpArray[x] = board[x][y];
      }
      swipeRight(tmpArray);
      //inputNewValues(array,tmpArray,y);
    }
  }
  private static void printBigArray(Cell[][] array){
    for(int x = 0; x < SIZE_OF_ARRAY; x++){
      for(int y = 0; y < SIZE_OF_ARRAY; y++){
        System.out.print("|");
        System.out.print(array[x][y].getValue());
      }
      System.out.println();
    }
    System.out.println();
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
  public void swipeBigArrayLeft(){
     for(Cell[] arr : board){
       swipeLeft(arr);
     }
  }
  public void swipeBigArrayRight(){
    for(Cell[] arr : board){
      swipeRight(arr);
    }
  }
  private void swipeRight(Cell[] array){
    reverseArray(array);
    swipeLeft(array);
    reverseArray(array);
  }
  private void reverseArray(Cell[] array){
    Cell[] preservedArray = new Cell[SIZE_OF_ARRAY];
    for(int i = 0; i < array.length;i++){
      preservedArray[i] = array[i];
    }
    for(int i = 0; i < array.length;i++){
      array[i] = preservedArray[array.length-i-1];
    }
  }
  private void swipeLeft(Cell[] array){
    clear(array);

    for(int currentCellIndex=1;currentCellIndex<array.length;currentCellIndex++){
      Cell currentCell = array[currentCellIndex];
      if(currentCell.getValue().equals(0)){
        continue;
      }
      if(!isThereAnyCellLeftTo(array,currentCellIndex)){
        array[0].setValue(currentCell.getValue());
        currentCell.setNull();
      } else {
        int closestCellIndex = getClosestCell(array,currentCellIndex);
        Cell closestCell = array[closestCellIndex];
        if(closestCell.getValue().equals(currentCell.getValue())){
          if(closestCell.haventBeenCollapsed()){
            closestCell.doubleValue();
            currentCell.setNull();
          } else {
            array[closestCellIndex+1].setValue(currentCell.getValue());
            currentCell.setNull();
          }
        } else {
          if(closestCellIndex+1==currentCellIndex){
            continue;
          } else {
            array[closestCellIndex+1].setValue(currentCell.getValue());
            currentCell.setNull();
          }
        }
      }
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
  private void clear(Cell[] array){
    for(Cell cell : array) {
      cell.clearCollapsed();
    }
  }
  private static void print(Cell[] array){
    for(Cell c : array){
      System.out.print("|");
      System.out.print(c.getValue());
    }
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
}
