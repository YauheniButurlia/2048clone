package com.example.yauheni.jerkthing.TestSpeed;

import com.example.yauheni.jerkthing.Cell;

public class TestView {

  private TestGame game;

  public TestView(TestGame game){
    this.game = game;
  }

  public void draw(){
    for(int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(game.get(i,j) < 1000){

        }
        if(game.get(i,j) > 1000){

        }
        if(game.get(i,j) > 10000){

        }
        System.out.print("|");
        System.out.print(game.get(i,j));
      }
      System.out.println();
    }
  }

  public void drawCells(Cell[][] cells){
    for(int i = 0; i < 8; i++){
      for (int j = 0; j < 8; j++){
        if(cells[i][j].getValue() < 1000){

        }
        if(cells[i][j].getValue() > 1000){

        }
        if(cells[i][j].getValue() > 10000){

        }
        System.out.print("|");
        System.out.print(cells[i][j].getValue());
      }
      System.out.println();
    }
  }
}
