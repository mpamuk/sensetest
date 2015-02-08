package com.sense360.api.factual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import com.factual.driver.Circle;
import com.factual.driver.Query;
import com.sense360.api.APIExecutorService;
import com.sense360.api.LocationProvider;
import com.sense360.api.LocationSearchParams;
import com.sense360.dao.POI;
import com.sense360.dao.TopPOIResponse;

public class FactualLocationProvider implements LocationProvider{

  private static final String DISTANCE = "distance";
  private static final String PLACE_RANK = "placeRank";
  private static final int RESTAURANT_MAX_CATEGORY_ID = 368;
  private static final int RESTAURANT_MIN_CATEGORY_ID = 347;
  private static final Object BAR_CATEGORY_ID = 312;


  public TopPOIResponse topPOIs(LocationSearchParams lsp)  throws Exception{
    List<POI> poisByDistance = getPoisFromService(lsp,DISTANCE);
    List<POI>poisByPlaceRank = getPoisFromService(lsp, PLACE_RANK);
    TopPOIResponse tpr = new TopPOIResponse(poisByDistance, poisByPlaceRank);
    return tpr;
  }



  public List<POI> getPoisFromService(LocationSearchParams lsp, String sortKey) throws InterruptedException, ExecutionException{
    Query q = new Query();
    q.and(
      q.field("category_ids").includesAny(BAR_CATEGORY_ID,RESTAURANT_MIN_CATEGORY_ID),
        q.within(new Circle(lsp.getLatitude(), lsp.getLongitude(), lsp.getRadius())))
         .limit(lsp.getLimit());
    if (sortKey.equals(DISTANCE)){
      q.sortAsc("$distance");
    }
    else if (sortKey.equals(PLACE_RANK)){
      q.sortDesc("placerank");
    }
      Future<FactualResponse> response = APIExecutorService.executorService.submit(new FactualRequest(q));
    List<POI>pois = parsePOIs(response.get().getBody());
    return pois;
  }



  private List<POI> parsePOIs(String content) {
    List<POI> pois = new ArrayList<POI>();
    JSONObject obj = new JSONObject(content);
    JSONArray poiArray = obj.getJSONObject("response").getJSONArray("data");
    if (poiArray != null) {
      for (int i=0; i<poiArray.length(); i++) {
        JSONObject item = poiArray.getJSONObject(i);
        double distance = item.getDouble("$distance");
        String name = item.getString("name");

        Set<Integer>categoryIds = new HashSet<Integer>();
        JSONArray categories = item.getJSONArray("category_ids");
        if (categories!=null){
          for (int j=0; j<categories.length(); j++) {
            int categoryId = categories.getInt(j);
            categoryIds.add(categoryId);
          }
        }
        POI poi = new POI(name, distance, isBar(categoryIds), isRestaurant(categoryIds));
        pois.add(poi);
      }
    }
    return pois;
  }


  private boolean isBar(Set<Integer>categoryIds){
    return categoryIds.contains(BAR_CATEGORY_ID);
  }

  private boolean isRestaurant(Set<Integer>categoryIds){
    for (Integer categoryId : categoryIds){
      if (categoryId>=RESTAURANT_MIN_CATEGORY_ID && categoryId<=RESTAURANT_MAX_CATEGORY_ID){
        return true;
      }
    }
    return false;
  }


}
