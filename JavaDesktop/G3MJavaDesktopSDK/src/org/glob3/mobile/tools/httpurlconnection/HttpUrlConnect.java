

package org.glob3.mobile.tools.httpurlconnection;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Map;

import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.JSONObject;


public class HttpUrlConnect {


   private final String _url;


   public HttpUrlConnect(final String url) {
      _url = url;
   }


   public String postRequest(final Map<String, String> requestProperty,
                             final Map<String, String> urlParameters) throws MalformedURLException, ProtocolException,
                                                                     IOException {
      return ConnectionManager.httpConnection(_url, ConnectionManager.methodPost, requestProperty, urlParameters);
   }


   public String getRequest(final Map<String, String> requestProperty,
                            final Map<String, String> urlParameters) throws MalformedURLException, ProtocolException, IOException {
      return ConnectionManager.httpConnection(_url, ConnectionManager.methodGet, requestProperty, urlParameters);
   }


   public JSONObject uploadFile(final Map<String, String> requestProperty,
                                final Map<String, String> urlParameters,
                                final File fileToUpload) {
      requestProperty.put("Connection", "Keep-Alive");
      requestProperty.put("Cache-Control", "no-cache");
      requestProperty.put("Accept-Charset", "UTF-8");
      requestProperty.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");


      try {
         final String json = ConnectionManager.httpConnectionMultipart(_url, fileToUpload, requestProperty, urlParameters);
         return IJSONParser.instance().parse(json).asObject();
      }
      catch (final IOException e) {
         ILogger.instance().logError("Exception: " + e.getMessage());
         return null;
      }

   }

}
