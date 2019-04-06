package com.example.yauheni.jerkthing;

public class Cell {
  private Integer value;
  private boolean justCollapsed;

  public Cell(int value){
    this.value = value;
    this.justCollapsed = false;
  }
  public Integer getValue(){
    return this.value;
  }
  public void setNull(){
    this.value = 0;
  }
  public void setValue(Integer value){
    this.value = value;
  }
  public void doubleValue(){
    this.value *=2;
    this.justCollapsed = true;
  }
  public void clearCollapsed(){
    this.justCollapsed = false;
  }
  public boolean haventBeenCollapsed(){
    return !this.justCollapsed;
  }
}

