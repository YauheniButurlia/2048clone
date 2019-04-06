package com.example.yauheni.jerkthing;

public class Animation {
  private long duration;
  private long startTime;
  private boolean isActive;

  public Animation(long duration) {
    this.duration = duration;
    this.isActive = false;
  }

  public void start(long currentTime){
    this.startTime = currentTime;
    this.isActive = true;
  }

  public float getPercentDone(long currentTime){
    if(animationDone(currentTime)){
      this.isActive = false;
      return 1f;
    }
    return (float)((currentTime - this.startTime)/this.duration);
  }

  private boolean animationDone(long currentTime){
    return this.duration <= (currentTime - this.startTime);
  }

  public boolean isActive(){
    return this.isActive;
  }
}
