

package com.glob3mobile.server.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


public abstract class RESTException
   extends
      Exception {


   private static final long serialVersionUID = 1L;

   protected final String    _code;
   protected final String    _description;


   protected RESTException(final String code,
                           final String description) {
      _code = code;
      _description = description;
   }


   public abstract void sendTo(final HttpServletResponse response) throws IOException;


   @Override
   public String getMessage() {
      if (_description == null) {
         return _code;
      }
      return _code + " (" + _description + ")";
   }


}
