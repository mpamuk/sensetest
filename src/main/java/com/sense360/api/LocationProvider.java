package com.sense360.api;


import com.sense360.dao.TopPOIResponse;

public interface LocationProvider {

   public TopPOIResponse topPOIs(LocationSearchParams locationSearchParams) throws Exception;

}
