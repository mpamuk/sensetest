package com.sense360.search;

import java.util.List;

import org.json.JSONObject;

import com.sense360.api.FactualLocationProvider;
import com.sense360.api.LocationProvider;
import com.sense360.api.LocationSearchParams;
import com.sense360.api.ratelimit.RateLimiterResponse;
import com.sense360.dao.POI;
import com.sense360.dao.TopPOIResponse;



public class SearchServices {


  private static final double FILL_RATE_PER_MS =  0.00001;
  private static final com.sense360.api.ratelimit.RateLimiter mrl = new com.sense360.api.ratelimit.RateLimiter(5.0, FILL_RATE_PER_MS);


  public static String place(String latitude, String longitude, String radius)
  {
    RateLimiterResponse rateLimiterResponse = mrl.consume(2.0);
    JSONObject errorObj = new JSONObject();

    if (rateLimiterResponse.isCanConsume()) {
      LocationSearchParams lsp = new LocationSearchParams(Double.parseDouble(latitude), Double.parseDouble(longitude), Integer.parseInt(radius),20);
      LocationProvider locationProvider = new FactualLocationProvider();
      TopPOIResponse tpr = null;
      try {
        tpr = locationProvider.topPOIs(lsp);
      }
      catch (Exception e){
        errorObj.put("message", e.getMessage());
        return errorObj.toString();
      }
      List<POI>poisByDistance = tpr.getTopPoisByDistance();
      List<POI>poisByPlaceRank = tpr.getTopPoisByPlaceRank();
      JSONObject responseObj = createResponseObject(poisByDistance, poisByPlaceRank);
      return responseObj.toString();
    }
    else {
      errorObj.put("message",  "Location API limit exceeded wait for " + rateLimiterResponse.getWaitTime() + " seconds");
      return errorObj.toString();
    }
  }





  private static JSONObject createResponseObject(List<POI> poisByDistance, List<POI> poisByPlaceRank) {
    JSONObject responseObj = new JSONObject();
    JSONObject distanceObj = new JSONObject();
    JSONObject placeRankObj = new JSONObject();
    addPois(distanceObj, poisByDistance);
    addPois(placeRankObj, poisByPlaceRank);
    responseObj.put("distance", distanceObj);
    responseObj.put("place rank", placeRankObj);
    return responseObj;
  }

  private static void addPois(JSONObject sortKeyObj, List<POI> poisBySortKey) {
    JSONObject sortKeyBarsObj = new JSONObject();
    JSONObject sortKeyRestaurantObj = new JSONObject();
    for (POI poi : poisBySortKey){
      if (poi.isBar()){
        sortKeyBarsObj.put(poi.getName(), poi.getDistanceFromQueryLocation());
      }
      if (poi.isRestaurant()){
        sortKeyRestaurantObj.put(poi.getName(), poi.getDistanceFromQueryLocation());
      }
    }
    sortKeyObj.put("bars", sortKeyBarsObj);
    sortKeyObj.put("restaurants", sortKeyRestaurantObj);
  }





}
