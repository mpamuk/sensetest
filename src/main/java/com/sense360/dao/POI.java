package com.sense360.dao;


public class POI {

  private String name;
  private double distanceFromQueryLocation;
  private boolean isBar;
  private boolean isRestaurant;

  public POI(String name, double distanceFromQueryLocation, boolean isBar, boolean isRestaurant) {
    this.name = name;
    this.distanceFromQueryLocation = distanceFromQueryLocation;
    this.isBar = isBar;
    this.isRestaurant = isRestaurant;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getDistanceFromQueryLocation() {
    return distanceFromQueryLocation;
  }

  public void setDistanceFromQueryLocation(double distance) {
    this.distanceFromQueryLocation = distance;
  }

  public boolean isBar() {
    return isBar;
  }

  public void setBar(boolean isBar) {
    this.isBar = isBar;
  }

  public boolean isRestaurant() {
    return isRestaurant;
  }

  public void setRestaurant(boolean isRestaurant) {
    this.isRestaurant = isRestaurant;
  }



}
