

package com.glob3mobile.server.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.glob3mobile.json.JSONConvertible;
import com.glob3mobile.json.JSONUtils;


public class RESTJSONResponse
   implements
      RESTResponse {

   private final int    _responseStatus;
   private final Object _result;


   public <T extends Object> RESTJSONResponse(final Map<String, T> result) {
      this(HttpServletResponse.SC_OK, result);
   }


   public <T extends Object> RESTJSONResponse(final List<T> result) {
      this(HttpServletResponse.SC_OK, result);
   }


   public <T extends JSONConvertible> RESTJSONResponse(final T result) {
      this(HttpServletResponse.SC_OK, result);
   }


   public RESTJSONResponse(final int responseStatus,
                           final String key,
                           final Object value) {
      _responseStatus = responseStatus;
      final HashMap<String, Object> map = new LinkedHashMap<>(1);
      map.put(key, value);
      _result = map;
   }


   public RESTJSONResponse(final String key,
                           final Object value) {
      this(HttpServletResponse.SC_OK, key, value);
   }


   public <T extends Object> RESTJSONResponse(final int responseStatus,
                                              final Map<String, T> result) {
      _responseStatus = responseStatus;
      _result = Collections.unmodifiableMap(result);
   }


   public <T extends Object> RESTJSONResponse(final int responseStatus,
                                              final List<T> result) {
      _responseStatus = responseStatus;
      _result = Collections.unmodifiableList(result);
   }


   public <T extends JSONConvertible> RESTJSONResponse(final int responseStatus,
                                                       final T result) {
      _responseStatus = responseStatus;
      _result = result;
   }


   private static final String JSON_MIME_TYPE    = "application/json";
   private static final String CHARSET_ENCONDING = "UTF-8";


   @Override
   public void sendTo(final HttpServletResponse response) throws IOException {
      response.setStatus(_responseStatus);
      response.setContentType(JSON_MIME_TYPE);
      response.setCharacterEncoding(CHARSET_ENCONDING);

      final ServletOutputStream os = response.getOutputStream();
      final byte[] utf8Output = JSONUtils.toJSON(_result).getBytes(CHARSET_ENCONDING);
      os.write(utf8Output);
      os.close();
   }


}
