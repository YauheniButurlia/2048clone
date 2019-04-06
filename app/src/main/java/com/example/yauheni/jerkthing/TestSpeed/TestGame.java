package com.example.yauheni.jerkthing.TestSpeed;

import com.example.yauheni.jerkthing.Cell;

public class TestGame {

  private TestBoard board;
  public TestGame(){
    this.board = new TestBoard();
  }
  public Integer get(int row, int col){
    return board.get(row, col);
  }
  public Cell[][] getCells(){
    return board.getCells();
  }
  public void startGame(TestView v){
    v.draw();
  }
  public void startAnotherGame(TestView v){
    v.drawCells(board.getCells());
  }
}
