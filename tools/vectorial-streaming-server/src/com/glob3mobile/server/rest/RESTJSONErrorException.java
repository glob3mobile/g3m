

package com.glob3mobile.server.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class RESTJSONErrorException
   extends
      RESTException {


   private static final long serialVersionUID = 1L;

   private final int         _responseStatus;


   public RESTJSONErrorException(final int responseStatus,
                                 final String code) {
      this(responseStatus, code, null);
   }


   public RESTJSONErrorException(final int responseStatus,
                                 final String code,
                                 final String description) {
      super(code, description);
      _responseStatus = responseStatus;
   }


   @Override
   public void sendTo(final HttpServletResponse response) throws IOException {
      response.setStatus(_responseStatus);
      response.setContentType("application/json");

      final PrintWriter writer = response.getWriter();
      writer.print("{\"errorCode\":\"");
      writer.print(_code);
      writer.print("\"");
      if (_description != null) {
         writer.print(",\"errorDescription\":\"");
         writer.print(_description.replace("\"", "\\\""));
         writer.print("\"");
      }
      writer.println("}");
   }


}
