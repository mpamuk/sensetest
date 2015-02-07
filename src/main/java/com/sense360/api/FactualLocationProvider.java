package com.sense360.api;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sense360.dao.POI;
import com.sense360.dao.TopPOIResponse;
import com.sense360.dao.http.Request;
import com.sense360.dao.http.Response;
import com.sense360.util.IOUtil;

public class FactualLocationProvider implements LocationProvider{

  private static final String DISTANCE = "distance";
  private static final String PLACE_RANK = "placeRank";
  private static final int RESTAURANT_MAX_CATEGORY_ID = 368;
  private static final int RESTAURANT_MIN_CATEGORY_ID = 347;
  private static final String FACTUAL_API_URL = "http://api.v3.factual.com/t/places-us";
  private static final Object BAR_CATEGORY_ID = 312;


  public TopPOIResponse topPOIs(LocationSearchParams lsp)  throws Exception{
    ExecutorService executor = Executors.newFixedThreadPool(1);
    String urlForDistance = assembleUrlForSort(lsp, DISTANCE);

    List<POI> poisByDistance;
    poisByDistance = getPoisFromService(executor, urlForDistance);
    String urlForPlaceRank = assembleUrlForSort(lsp, PLACE_RANK);
    List<POI>poisByPlaceRank = getPoisFromService(executor, urlForPlaceRank);
    executor.shutdown();
    TopPOIResponse tpr = new TopPOIResponse(poisByDistance, poisByPlaceRank);
    return tpr;
  }



  private String assembleUrlForSort(LocationSearchParams lsp, String sortKey) {
    double latitude = lsp.getLatitude();
    double longitude = lsp.getLongitude();
    int radius = lsp.getRadius();
    int limit = lsp.getLimit();
    String geoPred = "geo={\"$circle\":{\"$center\":[" + latitude + "," + longitude + "],\"$meters\":" + radius + "}}";
    String categoryPred = "filters={\"category_ids\":{\"$includes_any\":[312,347]}}";
    geoPred = geoPred.replaceAll("\"", "%22");
    categoryPred = categoryPred.replaceAll("\"", "%22");
    String sortStr = null;
    if (sortKey.equals(DISTANCE)){
      sortStr = "sort=$distance";
    }
    else if (sortKey.equals(PLACE_RANK)){
      sortStr = "sort=placerank:desc";
    }
    String url = FACTUAL_API_URL + "?limit=" + limit + (sortStr==null ? "" : "&" + sortStr) + "&" + categoryPred
          + "&" + geoPred + "&KEY=nXD7KzX7ZtIHvzaVmwveUURbdQdA6Rx2cvvefSkz";
    System.out.println(url);
    return url;
  }




  private List<POI> getPoisFromService(ExecutorService executor, String url) throws MalformedURLException, InterruptedException, ExecutionException {

    Future<Response> response = executor.submit(new Request(new URL(url)));
    InputStream body = response.get().getBody();
    String content = IOUtil.getStringFromInputStream(body);
    List<POI>poisByDistance = parsePOIs(content);
    return poisByDistance;
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
