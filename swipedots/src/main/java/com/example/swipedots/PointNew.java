package com.example.swipedots;

public class PointNew {
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

  public PointNew(float coordX, float coordY) {

    this.coordX = coordX;
    this.coordY = coordY;
  }
}

