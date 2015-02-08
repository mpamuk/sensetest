package com.sense360.api;


import java.util.concurrent.ExecutorService;

import com.sense360.dao.TopPOIResponse;

public interface LocationProvider {

   public TopPOIResponse topPOIs(LocationSearchParams locationSearchParams, ExecutorService executor) throws Exception;

}
