

package org.glob3.mobile.tools.httpurlconnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.fileupload.util.Streams;


public class ConnectionManager {

   public static final String methodPost = "POST";
   public static final String methodGet  = "GET";


   final static String        crlf       = "\r\n";
   final static String        twoHyphens = "--";


   public static File downloadFile(final String requestUrl,
                                   final File outputDir) throws IOException {
      File outputFile = null;

      final URL url = new URL(requestUrl);
      final HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      httpConn.setConnectTimeout(60000);
      httpConn.setReadTimeout(65000);
      httpConn.setUseCaches(false);

      final int responseCode = httpConn.getResponseCode();


      // always check HTTP response code first
      if (responseCode == HttpURLConnection.HTTP_OK) {
         String fileName = "";
         final String disposition = httpConn.getHeaderField("Content-Disposition");
         if (disposition != null) {
            // extracts file name from header field
            final int index = disposition.indexOf("filename=");
            if (index > 0) {
               fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
         }
         else {
            // extracts file name from URL
            fileName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.length());
         }

         if (outputDir.exists() && outputDir.isDirectory()) {
            outputFile = new File(outputDir, fileName);
         }
         else {
            httpConn.disconnect();
            throw new IOException("Outputdir don't exist");
         }
         Streams.copy(httpConn.getInputStream(), new FileOutputStream(outputFile), true);
         System.out.println("File downloaded");
      }
      else {
         System.out.println("No file to download. Server replied HTTP code: " + responseCode);
      }
      httpConn.disconnect();

      return outputFile;
   }


   public static String httpConnectionMultipart(final String requestUrl,
                                                final File fileToUpload,
                                                final Map<String, String> requestProperty,
                                                final Map<String, String> urlParameters) throws MalformedURLException,
                                                                                        IOException, ProtocolException {

      // Just generate some unique random value.
      final String boundary = Long.toHexString(System.currentTimeMillis());
      HttpURLConnection httpUrlConnection = null;
      final URL url = new URL(requestUrl);
      httpUrlConnection = (HttpURLConnection) url.openConnection();
      httpUrlConnection.setInstanceFollowRedirects(false);
      httpUrlConnection.setUseCaches(false);
      httpUrlConnection.setDoInput(true);
      httpUrlConnection.setDoOutput(true);

      httpUrlConnection.setRequestMethod(methodPost);
      for (final String key : requestProperty.keySet()) {
         httpUrlConnection.setRequestProperty(key, requestProperty.get(key));
      }
      httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


      DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
      request.writeBytes(twoHyphens + boundary + crlf);


      if (!urlParameters.isEmpty()) {
         for (final String key : urlParameters.keySet()) {
            // for every param
            final String param = urlParameters.get(key);
            request.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + crlf);
            request.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
            request.writeBytes("Content-Length: " + param.length() + crlf);
            request.writeBytes(crlf);
            request.writeBytes(param + crlf);
            request.writeBytes(twoHyphens + boundary + crlf);
         }
      }

      request.writeBytes("Content-Disposition: form-data; name=\"" + "fileuploader" + "\";filename=\"" + fileToUpload.getName()
                         + "\"" + crlf);

      String contentType = HttpURLConnection.guessContentTypeFromName(fileToUpload.getName());
      contentType = ((contentType != null) ? contentType : "");
      System.out.println("Content-Type: " + contentType);
      request.writeBytes("Content-Type: " + contentType + crlf);
      request.writeBytes("Content-Transfer-Encoding: binary" + crlf);

      request.writeBytes(crlf);
      request.flush();


      final FileInputStream in = new FileInputStream(fileToUpload);

      final long total = Streams.copy(in, request, false);
      System.out.println("Count: " + total + "; Total size: " + ((total) / 1000000) + " MB.");

      //End content wrapper:
      request.writeBytes(crlf);
      request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
      //Flush output buffer:
      request.flush();

      request.close();
      request = null;


      final StringBuilder sb = new StringBuilder();
      final BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
         sb.append(line);
      }
      reader.close();


      httpUrlConnection.disconnect();

      System.out.println(sb.toString());
      return sb.toString();
   }


   public static String httpConnection(final String requestUrl,
                                       final String method,
                                       final Map<String, String> requestProperty,
                                       final Map<String, String> urlParameters) throws MalformedURLException, IOException,
                                                                               ProtocolException {
      HttpURLConnection connection = null;


      if (method.equalsIgnoreCase(methodPost)) {
         final URL url = new URL(requestUrl);
         System.out.println(url.toString());
         connection = (HttpURLConnection) url.openConnection();
         connection.setInstanceFollowRedirects(false);
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         connection.setRequestMethod(method);
         for (final String key : requestProperty.keySet()) {
            connection.setRequestProperty(key, requestProperty.get(key));
         }
         connection.setDoOutput(true);
         if (!urlParameters.isEmpty()) {
            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            boolean first = true;
            for (final String key : urlParameters.keySet()) {
               if (first) {
                  first = false;
               }
               else {
                  wr.writeBytes("&");
               }
               wr.writeBytes(key + "=" + urlParameters.get(key));
               wr.flush();
            }

            wr.close();
         }
      }
      else {
         final StringBuffer params = new StringBuffer();
         if (!urlParameters.isEmpty()) {
            boolean first = true;
            for (final String key : urlParameters.keySet()) {
               if (first) {
                  params.append("?");
                  first = false;
               }
               else {
                  params.append("&");
               }
               params.append(key + "=" + urlParameters.get(key));
            }
         }
         final URL url = new URL(requestUrl + params.toString());
         connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod(method);
         connection.setInstanceFollowRedirects(false);
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         for (final String key : requestProperty.keySet()) {
            connection.setRequestProperty(key, requestProperty.get(key));
         }
      }


      final StringBuilder sb = new StringBuilder();
      final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
         sb.append(line);
      }
      reader.close();
      connection.disconnect();

      return sb.toString();
   }
}
