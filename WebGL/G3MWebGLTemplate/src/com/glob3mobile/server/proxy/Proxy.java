

package com.glob3mobile.server.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Proxy
         extends
            HttpServlet {

   /**
    * 
    */
   private static final long serialVersionUID = -6020365142488111237L;


   //   private static final int  CACHE_DURATION_IN_SECOND = 31536000;


   @Override
   public void doPost(final HttpServletRequest request,
                      final HttpServletResponse response) throws IOException {
      doGet(request, response);
   }


   @Override
   public void doGet(final HttpServletRequest request,
                     final HttpServletResponse response) throws IOException {

      String reqUrl = request.getQueryString();

      if (reqUrl == null) {
         response.setStatus(400);
         response.getOutputStream().println("ERROR 400: No target specified for proxy.");
         return;
      }


      reqUrl = URLDecoder.decode(reqUrl, "UTF-8");
      //      reqUrl = reqUrl.replaceFirst("url=", "");
      if (reqUrl.toLowerCase().startsWith("url=")) {
         reqUrl = reqUrl.substring("url=".length());
      }

      try {
         final URL url = new URL(reqUrl);

         final Enumeration headerNames = request.getHeaderNames();
         final Map<String, String> headers = new HashMap<String, String>();

         while (headerNames.hasMoreElements()) {
            final String headerName = (String) headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            if (headerName.equalsIgnoreCase("Host")) {
               // headerValue = reqUrl;
               final int port = url.getPort();
               final String host = url.getHost();
               headerValue = ((port == 80) || (port == -1)) ? host : host + ":" + port;
            }
            else if (headerName.equalsIgnoreCase("User-Agent")) {
               continue;
            }
            else if (headerName.equalsIgnoreCase("Accept-Encoding")) {
               continue;
            }
            headers.put(headerName, headerValue);
         }

         HttpURLConnection connection = null;
         try {
            //Create connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            for (final String headerName : headers.keySet()) {
               connection.setRequestProperty(headerName, headers.get(headerName));
            }

            connection.setRequestMethod("GET");

            // Set maxAge
            //            connection.setRequestProperty("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);

            // Send request
            //            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //            wr.flush();
            //            wr.close();

            // Get content type
            final String contentType = connection.getContentType();
            if (contentType != null) {
               response.setContentType(contentType);

               final int responseCode = connection.getResponseCode();
               response.setStatus(responseCode);
               BufferedInputStream in;
               if ((responseCode == 200) || (responseCode == 201)) {
                  in = new BufferedInputStream(connection.getInputStream());
               }
               else {
                  in = new BufferedInputStream(connection.getErrorStream());
               }


               //send output to client
               final BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
               final byte[] buffer = new byte[4096];
               int length = 0;
               int totalLength = 0;
               while ((length = in.read(buffer)) > 0) {
                  out.write(buffer, 0, length);
                  totalLength += length;
               }
               response.setContentLength(totalLength);
               out.flush();
               out.close();
               in.close();
            }
            else {
               System.out.println("Content type is null");
            }
         }
         //         catch (final Exception e) {
         //            e.printStackTrace();
         //         }
         finally {
            if (connection != null) {
               connection.disconnect();
            }
         }

      }
      catch (final MalformedURLException e1) {
         response.setStatus(400);
         response.getOutputStream().println("ERROR 400: Invalid URL: " + reqUrl);
         return;
      }
   }
}
