package com.sense360.api.factual;

import java.util.concurrent.Callable;

import com.factual.driver.Factual;
import com.factual.driver.Query;

public class FactualRequest implements Callable<FactualResponse> {

    private static final String FACTUAL_KEY = "nXD7KzX7ZtIHvzaVmwveUURbdQdA6Rx2cvvefSkz";
    private static final String FACTUAL_SECRET = "lsNeUbXFfeYTOYwyJRfBkLM7qWvyd0pzj0qUlbBn";

    private static final Factual factual = new Factual(FACTUAL_KEY, FACTUAL_SECRET);

      private Query query;

      public FactualRequest(Query q) {
          this.query = q;
      }

      public FactualResponse call() throws Exception {
          return new FactualResponse(factual.fetch("places", query));
      }

}
