package com.sense360.search;

import java.util.List;

import org.json.JSONObject;

import com.sense360.api.LocationProvider;
import com.sense360.api.LocationSearchParams;
import com.sense360.api.TopPOIResponse;
import com.sense360.api.factual.FactualLocationProvider;
import com.sense360.model.POI;



public class SearchServices {


  private static final int LIMIT = 20;

  public static String place(String latitude, String longitude, String radius)
  {
    LocationSearchParams lsp = new LocationSearchParams(Double.parseDouble(latitude), Double.parseDouble(longitude), Integer.parseInt(radius),LIMIT);
    LocationProvider locationProvider = new FactualLocationProvider();
    JSONObject errorObj = new JSONObject();

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


  private static JSONObject createResponseObject(List<POI> poisByDistance, List<POI> poisByPlaceRank) {
    JSONObject responseObj = new JSONObject();
    JSONObject distanceObj = getPOIObjBySortKey(poisByDistance);
    JSONObject placeRankObj = getPOIObjBySortKey(poisByPlaceRank);
    responseObj.put("distance", distanceObj);
    responseObj.put("place rank", placeRankObj);
    return responseObj;
  }

  private static JSONObject getPOIObjBySortKey(List<POI> poisBySortKey) {
    JSONObject sortKeyObj = new JSONObject();
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
    return sortKeyObj;
  }


}
