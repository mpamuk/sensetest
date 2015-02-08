package com.sense360.api.factual;


import com.factual.driver.ReadResponse;

public class FactualResponse {

      private ReadResponse response;

      public FactualResponse(ReadResponse readResponse) {
          this.response = readResponse;
      }

      public String getBody() {
          return response.getJson();
      }

}
