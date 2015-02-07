package com.sense360.dao;

import java.util.List;

public class TopPOIResponse {

  private List<POI>topPoisByDistance;
  private List<POI>topPoisByPlaceRank;

  public TopPOIResponse(List<POI> topPoisByDistance, List<POI> topPoisByPlaceRank) {
    super();
    this.topPoisByDistance = topPoisByDistance;
    this.topPoisByPlaceRank = topPoisByPlaceRank;
  }

  public List<POI> getTopPoisByDistance() {
    return topPoisByDistance;
  }

  public void setTopPoisByDistance(List<POI> topPoisByDistance) {
    this.topPoisByDistance = topPoisByDistance;
  }

  public List<POI> getTopPoisByPlaceRank() {
    return topPoisByPlaceRank;
  }

  public void setTopPoisByPlaceRank(List<POI> topPoisByPlaceRank) {
    this.topPoisByPlaceRank = topPoisByPlaceRank;
  }

}
