

package org.glob3.mobile.specific;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.Response;
import org.glob3.mobile.generated.URL;

import android.util.Log;


public class Downloader_Android_Handler {

   final static String                    TAG = "Downloader_Android_Handler";

   private long                           _priority;
   private final URL                      _url;
   private java.net.URL                   _URL;
   private final ArrayList<ListenerEntry> _listeners;


   public Downloader_Android_Handler(final URL url,
                                     final IDownloadListener listener,
                                     final long priority,
                                     final long requestId) {
      _priority = priority;
      _url = url;
      try {
         _URL = new java.net.URL(url.getPath());
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + "Downloader_Android_Handler: MalformedURLException url=" + _url.getPath());
         }
         else {
            Log.e(TAG, "Downloader_Android_Handler: MalformedURLException url=" + _url.getPath());
         }
         e.printStackTrace();
      }
      _listeners = new ArrayList<ListenerEntry>();
      final ListenerEntry entry = new ListenerEntry(listener, requestId);
      _listeners.add(entry);
   }


   public void addListener(final IDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(listener, requestId);

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


   public boolean cancelListenerForRequestId(final long requestId) {
      boolean canceled = false;

      synchronized (this) {
         final Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext() && !canceled) {
            final ListenerEntry entry = iter.next();

            if (entry.getRequestId() == requestId) {
               entry.cancel();
               canceled = true;
            }
         }
      }

      return canceled;
   }


   public boolean removeListenerForRequestId(final long requestId) {
      boolean removed = false;

      synchronized (this) {
         final Iterator<ListenerEntry> iter = _listeners.iterator();

         while (iter.hasNext()) {
            final ListenerEntry entry = iter.next();

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


   public void runWithDownloader(final IDownloader downloader) {
      //      Log.i(TAG, "runWithDownloader url=" + _url.getPath());

      final Downloader_Android dl = (Downloader_Android) downloader;
      HttpURLConnection connection = null;
      int statusCode = 0;
      byte[] data = null;
      try {
         connection = (HttpURLConnection) _URL.openConnection();
         connection.setConnectTimeout(dl.getConnectTimeout());
         connection.setReadTimeout(dl.getReadTimeout());
         connection.setUseCaches(false);
         connection.connect();
         statusCode = connection.getResponseCode();

         if (statusCode == 200) {
            final BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int length = 0;

            while ((length = bis.read(buffer)) > 0) {
               baos.write(buffer, 0, length);
            }

            baos.flush();
            data = baos.toByteArray();
            baos.close();
            bis.close();
         }
      }
      catch (final IOException e) {
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
      }
      // inform downloader to remove myself, to avoid adding new Listener
      dl.removeDownloadingHandlerForUrl(_url.getPath());

      IThreadUtils.instance().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }

   public class ProcessResponseGTask
            extends
               GTask {

      int                        _statusCode = 0;
      byte[]                     _data       = null;
      Downloader_Android_Handler _handler;


      public ProcessResponseGTask(final int statusCode,
                                  final byte[] data,
                                  final Downloader_Android_Handler handler) {
         _statusCode = statusCode;
         _data = data;
         _handler = handler;
      }


      @Override
      public void run() {
         synchronized (_handler) {
            final boolean dataIsValid = (_data != null) && (_statusCode == 200);

            if (dataIsValid) {
               final ByteBuffer buffer = new ByteBuffer(_data, _data.length);
               final Response response = new Response(_url, buffer);
               final Iterator<ListenerEntry> iter = _listeners.iterator();

               while (iter.hasNext()) {
                  final ListenerEntry entry = iter.next();
                  if (entry.isCanceled()) {
                     //                     Log.w(TAG, "triggering onCanceledDownload");
                     entry.getListener().onCanceledDownload(response);

                     //                     Log.w(TAG, "triggering onCancel");
                     entry.getListener().onCancel(_url);
                  }
                  else {
                     //                     Log.i(TAG, "triggering onDownload");
                     entry.getListener().onDownload(response);
                  }
               }
            }
            else {
               if (ILogger.instance() != null) {
                  ILogger.instance().logError(
                           TAG + "Error runWithDownloader: statusCode=" + _statusCode + ", url=" + _url.getPath());
               }
               else {
                  Log.e(TAG, "Error runWithDownloader: statusCode=" + _statusCode + ", url=" + _url.getPath());
               }
               final ByteBuffer buffer = new ByteBuffer(null, 0);
               final Response response = new Response(_url, buffer);
               final Iterator<ListenerEntry> iter = _listeners.iterator();

               while (iter.hasNext()) {
                  final ListenerEntry entry = iter.next();
                  //                  Log.e(TAG, "triggering onError");
                  entry.getListener().onError(response);
               }
            }
         }
      }
   }
}
