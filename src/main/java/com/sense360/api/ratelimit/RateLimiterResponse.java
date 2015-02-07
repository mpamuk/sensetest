package com.sense360.api.ratelimit;

public class RateLimiterResponse {

  private boolean canConsume;
  private double waitTime;

  public RateLimiterResponse(boolean canConsume, double waitTime) {
    super();
    this.canConsume = canConsume;
    this.waitTime = waitTime;
  }

  public boolean isCanConsume() {
    return canConsume;
  }

  public void setCanConsume(boolean canConsume) {
    this.canConsume = canConsume;
  }

  public double getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(double waitTime) {
    this.waitTime = waitTime;
  }


}
