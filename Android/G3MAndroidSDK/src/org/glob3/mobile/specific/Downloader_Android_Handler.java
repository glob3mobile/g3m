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
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Response;
import org.glob3.mobile.generated.URL;

import android.util.Log;

public class Downloader_Android_Handler {

   final static String              TAG = "Downloader_Android_Handler";

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
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + ": MalformedURLException url=" + _url.getPath());
         }
         else {
            Log.e(TAG, ": MalformedURLException url=" + _url.getPath());
         }
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
         connection.setUseCaches(false);
         connection.connect();
         statusCode = connection.getResponseCode();
         
         if (statusCode == 200) {
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length = 0;

            while ((length = bis.read(buffer)) > 0) {
               baos.write(buffer, 0, length);
            }

            baos.flush();
            _data = baos.toByteArray();
            baos.close();
         }
         
         dataIsValid = (_data != null) && (statusCode == 200);
         Log.w(TAG, "statusCode=" + statusCode + " dataIsValid=" + dataIsValid + " url=" + _url.getPath());
      }
      catch (IOException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + "runWithDownloader: IOException url=" + _url.getPath());
         }
         else {
            Log.e(TAG, "runWithDownloader: IOException url=" + _url.getPath());
         }
         e.printStackTrace();
      }
      finally {
         if (connection != null) {
            connection.disconnect();
         }
         if (!dataIsValid) {
            if (ILogger.instance() != null) {
               ILogger.instance().logError(
                        TAG + "Error runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());
            }
            else {
               Log.e(TAG, "Error runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());
            }
         }
         else {
            Log.i(TAG, "Success runWithDownloader: statusCode=" + statusCode + ", url=" + _url.getPath());
         }
      }
      // inform downloader to remove myself, to avoid adding new Listener
      dl.removeDownloadingHandlerForUrl(_url.getPath());

      return (dataIsValid);
   }


   public synchronized void processResponse(boolean dataIsValid) {
      Log.i(TAG, "processResponse: url=" + _url.getPath() + " dataIsValid=" + dataIsValid);
      if (dataIsValid) {
         ByteBuffer buffer = new ByteBuffer(_data, 0);
         Response response = new Response(_url, buffer);
         Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            ListenerEntry entry = iter.next();
            if (entry.isCanceled()) {
               Log.w(TAG, "triggering onCancel");
               entry.getListener().onCanceledDownload(response);

               Log.w(TAG, "triggering onCancel");
               entry.getListener().onCancel(_url);
            }
            else {
               Log.i(TAG, "triggering onDownload");
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
            Log.e(TAG, "triggering onError");
            entry.getListener().onError(response);
         }
      }
   }
}
