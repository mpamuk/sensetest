package com.sense360.api;



public interface LocationProvider {

   public TopPOIResponse topPOIs(LocationSearchParams locationSearchParams) throws Exception;

}
