package com.sense360.dao.http;

import java.net.URL;
import java.util.concurrent.Callable;

public class Request implements Callable<Response> {

      private URL url;

      public Request(URL url) {
          this.url = url;
      }

      public Response call() throws Exception {
          return new Response(url.openStream());
      }

}
