package com.sense360.dao.http;

import java.io.InputStream;

public class Response {

      private InputStream body;

      public Response(InputStream body) {
          this.body = body;
      }

      public InputStream getBody() {
          return body;
      }

}