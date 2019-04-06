package com.example.fouronfouranim;

public class Cell {
  private Integer value;
  private boolean justCollapsed;
  private boolean isItNew;

  public Cell(int value){
    this.value = value;
    this.justCollapsed = false;
    this.isItNew = false;
  }

  public boolean isItNew(){
    return isItNew;
  }
  public void setNew(){
    this.isItNew = true;
  }
  public void setNotNew(){
    this.isItNew = false;
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



