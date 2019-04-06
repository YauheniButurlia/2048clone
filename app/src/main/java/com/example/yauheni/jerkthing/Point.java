package com.example.yauheni.jerkthing;

public class Point {
  private float coordX;
  private float coordY;

  public void setCoordX(float coordX) {
    this.coordX = coordX;
  }

  public void setCoordY(float coordY) {
    this.coordY = coordY;
  }

  public float getCoordX() {
    return coordX;
  }

  public float getCoordY() {
    return coordY;
  }

  public Point(float coordX, float coordY) {

    this.coordX = coordX;
    this.coordY = coordY;
  }
}
