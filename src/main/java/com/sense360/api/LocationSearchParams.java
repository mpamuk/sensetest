package com.sense360.api;

public class LocationSearchParams {


  private double latitude;
  private double longitude;
  private int radius;
  private int limit;

  public LocationSearchParams(double latitude, double longitude, int radius, int limit) {
    super();
    this.latitude = latitude;
    this.longitude = longitude;
    this.radius = radius;
    this.limit = limit;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public int getRadius() {
    return radius;
  }

  public int getLimit() {
    return limit;
  }


}
