package org.glob3.mobile.specific;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.Response;
import org.glob3.mobile.generated.URL;

import android.util.Log;

public class Downloader_Android_Handler {

   private long                     _priority;
   private URL                      _url;
   private java.net.URL             _URL;
   private ArrayList<ListenerEntry> _listeners;
   private byte[]                   _data;


   public Downloader_Android_Handler(URL url,
                                     IDownloadListener listener,
                                     int priority,
                                     long requestId) {
      _priority = priority;
      _url = url;
      try {
         _URL = new java.net.URL(url.getPath());
      }
      catch (MalformedURLException e) {
         Logger_Android.instance().logError("Downloader_Android_Handler: url=" + _url.getPath() + " " + e.getMessage());
         e.printStackTrace();
      }
      _listeners = new ArrayList<ListenerEntry>();
      ListenerEntry entry = new ListenerEntry(listener, requestId);
      _listeners.add(entry);
      _data = null;
   }


   public void addListener(IDownloadListener listener,
                           int priority,
                           long requestId) {
      ListenerEntry entry = new ListenerEntry(listener, requestId);

      synchronized (this) {
         _listeners.add(entry);

         if (priority > _priority) {
            _priority = priority;
         }
      }
   }


   public synchronized long getPriority() {
      return _priority;
   }


   public boolean cancelListenerForRequestId(int requestId) {
      boolean canceled = false;

      synchronized (this) {
         Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext() && !canceled) {
            ListenerEntry entry = iter.next();

            if (entry.getRequestId() == requestId) {
               entry.cancel();
               canceled = true;
            }
         }
      }

      return canceled;
   }


   public boolean removeListenerForRequestId(int requestId) {
      boolean removed = false;

      synchronized (this) {
         Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            ListenerEntry entry = iter.next();

            if (entry.getRequestId() == requestId) {
               entry.getListener().onCancel(_url);
               _listeners.remove(entry);
               removed = true;

               break;
            }
         }
      }

      return removed;
   }


   public synchronized boolean hasListener() {
      return !_listeners.isEmpty();
   }


   public boolean runWithDownloader(IDownloader downloader) {
      Downloader_Android dl = (Downloader_Android) downloader;
      HttpURLConnection connection = null;
      int statusCode = 0;
      boolean dataIsValid = false;
      try {
         connection = (HttpURLConnection) _URL.openConnection();
         connection.setConnectTimeout(60000);
         connection.setReadTimeout(60000);
         connection.setUseCaches(true);

         BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] buffer = new byte[1024];
         int length = 0;

         while ((length = bis.read(buffer)) > 0) {
            baos.write(buffer, 0, length);
         }

         baos.flush();
         _data = baos.toByteArray();
         baos.close();
         statusCode = connection.getResponseCode();
         dataIsValid = (_data != null) && (statusCode == 200);

         if (!dataIsValid) {
            Logger_Android.instance().logError("Error runWithDownloader, StatusCode=" + statusCode + ", URL=" + _url.getPath());
         }
         else {
            Log.i("Downloader_Android_Handler", "Success runWithDownloader, StatusCode=" + statusCode + ", URL=" + _url.getPath());
         }
      }
      catch (IOException e) {
         Logger_Android.instance().logError("Downloader_Android_Handler: url=" + _url.getPath() + " " + e.getMessage());
         e.printStackTrace();
      }
      finally {
         if (connection != null) {
            connection.disconnect();
         }
      }
      // inform downloader to remove myself, to avoid adding new Listener
      dl.removeDownloadHandlerForUrl(_url.getPath());

      Log.i("Handler", "returning dataIsValid=" + dataIsValid);
      return (dataIsValid);
   }


   public synchronized void processResponse(boolean dataIsValid) {
      Log.e("Handler", "processingResponse url=" + _url.getPath());
      if (dataIsValid) {
         ByteBuffer buffer = new ByteBuffer(_data, 0);
         Response response = new Response(_url, buffer);
         Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            ListenerEntry entry = iter.next();
            if (entry.isCanceled()) {
               entry.getListener().onCanceledDownload(response);

               entry.getListener().onCancel(_url);
            }
            else {
               entry.getListener().onDownload(response);
            }
         }
      }
      else {
         ByteBuffer buffer = new ByteBuffer(null, 0);
         Response response = new Response(_url, buffer);
         Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            ListenerEntry entry = iter.next();
            entry.getListener().onError(response);
         }
      }
   }
}
