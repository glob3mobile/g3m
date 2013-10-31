

package org.glob3.mobile.specific;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;

import android.util.Log;


public final class Downloader_Android_Handler {

   final static String                    TAG        = "Downloader_Android_Handler";

   private long                           _priority;
   private final URL                      _g3mURL;
   private java.net.URL                   _javaURL;
   private final ArrayList<ListenerEntry> _listeners = new ArrayList<ListenerEntry>();


   Downloader_Android_Handler(final URL url,
                              final IBufferDownloadListener listener,
                              final long priority,
                              final long requestId) {
      _priority = priority;
      _g3mURL = url;
      try {
         _javaURL = new java.net.URL(url.getPath());

         final ListenerEntry entry = new ListenerEntry(listener, null, requestId);
         _listeners.add(entry);
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         else {
            Log.e(TAG, "MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   Downloader_Android_Handler(final URL url,
                              final IImageDownloadListener listener,
                              final long priority,
                              final long requestId) {
      _priority = priority;
      _g3mURL = url;
      try {
         _javaURL = new java.net.URL(url.getPath());

         final ListenerEntry entry = new ListenerEntry(null, listener, requestId);
         _listeners.add(entry);
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         else {
            Log.e(TAG, "MalformedURLException url=" + _g3mURL.getPath());
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   public void addListener(final IBufferDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(listener, null, requestId);

      synchronized (this) {
         _listeners.add(entry);

         if (priority > _priority) {
            _priority = priority;
         }
      }
   }


   public void addListener(final IImageDownloadListener listener,
                           final long priority,
                           final long requestId) {
      final ListenerEntry entry = new ListenerEntry(null, listener, requestId);

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
               entry.onCancel(_g3mURL);
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


   void runWithDownloader(final Downloader_Android downloader,
                          final G3MContext context) {
      //      Log.i(TAG, "runWithDownloader url=" + _url.getPath());

      if (_javaURL == null) {
         return;
      }

      int statusCode = 0;
      byte[] data = null;
      HttpURLConnection connection = null;

      try {
         if (_g3mURL.isFileProtocol()) {
            final String filePath = _g3mURL.getPath().replaceFirst(URL.FILE_PROTOCOL, "");

            final File file = new File(filePath);
            final InputStream fileIS = file.exists() ? new FileInputStream(file) //
                                                    : downloader.getAppContext().getAssets().open(filePath);

            data = getData(fileIS, -1);
            if (data != null) {
               statusCode = 200;
            }
         }
         else {
            connection = (HttpURLConnection) _javaURL.openConnection();
            connection.setConnectTimeout((int) downloader.getConnectTimeout().milliseconds());
            connection.setReadTimeout((int) downloader.getReadTimeout().milliseconds());
            connection.setUseCaches(false);
            connection.connect();
            statusCode = connection.getResponseCode();

            if (statusCode == 200) {
               data = getData(connection.getInputStream(), connection.getContentLength());
            }
         }
      }
      catch (final IOException e) {
         ILogger.instance().logError(TAG + " runWithDownloader: IOException url=" + _g3mURL.getPath());
         e.printStackTrace();
      }
      finally {
         if (connection != null) {
            connection.disconnect();
         }
      }

      // inform downloader to remove myself, to avoid adding new Listener
      downloader.removeDownloadingHandlerForUrl(_g3mURL.getPath());


      context.getThreadUtils().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, this), true);
   }


   private byte[] getData(final InputStream is,
                          final int contentLength) {
      byte[] data = null;

      try {
         final BufferedInputStream bis = new BufferedInputStream(is);
         final boolean hasContentLength = contentLength > 0;
         final ByteArrayOutputStream baos = hasContentLength ? new ByteArrayOutputStream(contentLength)
                                                            : new ByteArrayOutputStream(4096);
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
      catch (final IOException e) {
         ILogger.instance().logError(TAG + " getData: IOException url=" + _g3mURL.getPath());
         e.printStackTrace();
      }

      return data;
   }

   private class ProcessResponseGTask
            extends
               GTask {

      private final int                        _statusCode;
      private final byte[]                     _data;
      private final Downloader_Android_Handler _handler;


      public ProcessResponseGTask(final int statusCode,
                                  final byte[] data,
                                  final Downloader_Android_Handler handler) {
         _statusCode = statusCode;
         _data = data;
         _handler = handler;
      }


      @Override
      public void run(final G3MContext context) {
         synchronized (_handler) {
            final boolean dataIsValid = (_data != null) && (_statusCode == 200);

            if (dataIsValid) {
               for (final ListenerEntry entry : _listeners) {
                  if (entry.isCanceled()) {
                     entry.onCanceledDownload(_g3mURL, _data);

                     entry.onCancel(_g3mURL);
                  }
                  else {
                     entry.onDownload(_g3mURL, _data);
                  }
               }
            }
            else {
               final ILogger logger = ILogger.instance();
               final String msg = "Error runWithDownloader: statusCode=" + _statusCode + ", url=" + _g3mURL.getPath();
               if (logger != null) {
                  logger.logError(TAG + " " + msg);
               }
               else {
                  Log.e(TAG, msg);
               }

               for (final ListenerEntry entry : _listeners) {
                  entry.onError(_g3mURL);
               }
            }
         }
      }
   }
}
