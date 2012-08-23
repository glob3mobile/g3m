package org.glob3.mobile.specific;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpConnection;
import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import android.util.Log;

public class Downloader_Android_Handler {

   private long                     _priority;
   private boolean                  _canceled;
   private org.glob3.mobile.generated.URL                      _url;
   private URL                      _URL;
   private ArrayList<ListenerEntry> _listeners;


   public Downloader_Android_Handler(org.glob3.mobile.generated.URL url,
                                     IDownloadListener listener,
                                     int priority,
                                     long requestId) {
      // TODO Auto-generated constructor stub
      _url = url;
      try {
         _URL = new URL(url.getPath());
      }
      catch (MalformedURLException e) {
         // TODO Logger_Android
         Log.e("Downloader_Android_Handler", e.getMessage());
         e.printStackTrace();
      }
      _listeners = new ArrayList<ListenerEntry>();
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


   public void runWithDownloader(IDownloader _downloader) {
      // TODO finish

      try {
         URLConnection connection = _URL.openConnection();
//         connection.setUseCaches(true);
         int status = ((HttpURLConnection) connection).getResponseCode();
//         InputStream is = connection.getInputStream();
         // Bitmap bmp = BitmapFactory.decodeStream(is);
         
      }
      catch (IOException e) {
         // TODO Logger_Android
         Log.e("Downloader_Android_Handler", e.getMessage());
         e.printStackTrace();
      }


   }

}
