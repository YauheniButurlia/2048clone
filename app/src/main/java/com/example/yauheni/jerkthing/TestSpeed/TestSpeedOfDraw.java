package com.example.yauheni.jerkthing.TestSpeed;

public class TestSpeedOfDraw {
  public static void main(String[] args){
    TestGame g = new TestGame();
    TestView v = new TestView(g);
    long start1 = System.currentTimeMillis();
    g.startGame(v);
    long finish1 = System.currentTimeMillis() - start1;
    long start2 = System.currentTimeMillis();
    g.startAnotherGame(v);
    long finish2 = System.currentTimeMillis() - start2;

    System.out.println(finish1);
    System.out.println(finish2);
  }
}
