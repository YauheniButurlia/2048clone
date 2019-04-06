package com.example.yauheni.jerkthing.TestSpeed;

import com.example.yauheni.jerkthing.Cell;
import com.example.yauheni.jerkthing.PlayGround;



public class TestBoard {
  private static int SIZE_OF_ARRAY = 8;
  private Cell[][] cells;

  public TestBoard(){
    this.cells = generateArray(new int[]{16,2,0,4,0,2,0,8,8,32,0,0,32,4,2,256,16,2,0,4,0,2,0,8,8,32,0,0,32,4,2,256,16,2,0,4,0,2,0,8,8,32,0,0,32,4,2,256,16,2,0,4,0,2,0,8,8,32,0,0,32,4,2,256});
  }

  private Cell[][] generateArray(int[] longArray){
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

  public Cell[][] getCells(){
    return cells;
  }

  public Integer get(int row, int col){
    return cells[row][col].getValue();
  }
}
