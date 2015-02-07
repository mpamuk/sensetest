package com.sense360.api.ratelimit;


public class RateLimiter {

  private static final int MS_IN_A_SECOND = 1000;
  private final double fillRatePerMs;
  private final double maxBudget;
  private double currentBudget;
  private long lastUpdateTime;

  public RateLimiter(double maxBudget, double fillRatePerMs) {
    this.fillRatePerMs  = fillRatePerMs;
    this.maxBudget      = maxBudget;
    this.currentBudget  = maxBudget;
    this.lastUpdateTime = System.currentTimeMillis();
  }


  public RateLimiterResponse consume(double amount) {
    long msSinceLastUpdate = System.currentTimeMillis() - lastUpdateTime;
    currentBudget = Math.min(maxBudget,
        currentBudget + msSinceLastUpdate * fillRatePerMs);
    lastUpdateTime += msSinceLastUpdate;
    if (currentBudget >= amount) {
      currentBudget -= amount;
      return new RateLimiterResponse(true,0.0);
    } else {
      return new RateLimiterResponse(false,(amount-currentBudget)/fillRatePerMs/MS_IN_A_SECOND);
    }
  }

}
