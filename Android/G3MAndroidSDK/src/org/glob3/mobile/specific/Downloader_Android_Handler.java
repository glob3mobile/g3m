

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

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public final class Downloader_Android_Handler {

   private static final int                                  DEFAULT_BUFFER_SIZE = 32 * 1024;

   private static BitmapFactory.Options                      _bitmapFactoryOptions;
   static {
      _bitmapFactoryOptions = new BitmapFactory.Options();
      _bitmapFactoryOptions.inTempStorage = new byte[128 * 1024];
   }


   private final static String                               TAG                 = "Downloader_Android_Handler";

   private long                                              _priority;
   private final URL                                         _g3mURL;
   private java.net.URL                                      _javaURL;
   private final ArrayList<Downloader_Android_ListenerEntry> _listeners          = new ArrayList<Downloader_Android_ListenerEntry>();

   private boolean                                           _hasImageListeners;


   Downloader_Android_Handler(final URL url,
                              final IBufferDownloadListener listener,
                              final boolean deleteListener,
                              final long priority,
                              final long requestId) {
      _priority = priority;
      _g3mURL = url;
      _hasImageListeners = false;
      try {
         _javaURL = new java.net.URL(url._path);

         _listeners.add(new Downloader_Android_ListenerEntry(listener, null, deleteListener, requestId));
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL._path + "\"");
         }
         else {
            Log.e(TAG, "MalformedURLException url=\"" + _g3mURL._path + "\"");
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   Downloader_Android_Handler(final URL url,
                              final IImageDownloadListener listener,
                              final boolean deleteListener,
                              final long priority,
                              final long requestId) {
      _priority = priority;
      _g3mURL = url;
      _hasImageListeners = true;
      try {
         _javaURL = new java.net.URL(url._path);

         _listeners.add(new Downloader_Android_ListenerEntry(null, listener, deleteListener, requestId));
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL._path + "\"");
         }
         else {
            Log.e(TAG, "MalformedURLException url=" + _g3mURL._path);
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   void addListener(final IBufferDownloadListener listener,
                    final boolean deleteListener,
                    final long priority,
                    final long requestId) {
      final Downloader_Android_ListenerEntry entry = new Downloader_Android_ListenerEntry(listener, null, deleteListener,
               requestId);

      synchronized (this) {
         _listeners.add(entry);

         if (priority > _priority) {
            _priority = priority;
         }
      }
   }


   void addListener(final IImageDownloadListener listener,
                    final boolean deleteListener,
                    final long priority,
                    final long requestId) {
      final Downloader_Android_ListenerEntry entry = new Downloader_Android_ListenerEntry(null, listener, deleteListener,
               requestId);

      synchronized (this) {
         _hasImageListeners = true;

         _listeners.add(entry);

         if (priority > _priority) {
            _priority = priority;
         }
      }
   }


   synchronized long getPriority() {
      return _priority;
   }


   boolean cancelListenerForRequestId(final long requestId) {
      synchronized (this) {
         for (final Downloader_Android_ListenerEntry entry : _listeners) {
            if (entry._requestId == requestId) {
               entry.cancel();
               return true;
            }
         }
      }

      return false;
   }


   boolean removeListenerForRequestId(final long requestId) {
      synchronized (this) {
         for (final Downloader_Android_ListenerEntry entry : _listeners) {
            if (entry._requestId == requestId) {
               entry.onCancel(_g3mURL);
               _listeners.remove(entry);
               return true;
            }
         }
      }

      return false;
   }


   synchronized boolean hasListener() {
      return !_listeners.isEmpty();
   }


   void runWithDownloader(final Downloader_Android downloader,
                          final G3MContext context) {
      //      Log.i(TAG, "runWithDownloader url=" + _url._path);

      if (_javaURL == null) {
         return;
      }

      int statusCode = 0;
      byte[] data = null;
      HttpURLConnection connection = null;

      try {
         if (_g3mURL.isFileProtocol()) {
            final String filePath = _g3mURL._path.replaceFirst(URL.FILE_PROTOCOL, "");

            final File file = new File(filePath);
            final InputStream fileIS = file.exists() ? new FileInputStream(file) //
                                                    : downloader.getAppContext().getAssets().open(filePath);

            data = getData(fileIS, -1);
            if (data != null) {
               statusCode = 200;
            }
         }
         else {
            //            final long s = SystemClock.currentThreadTimeMillis();
            //            ILogger.instance().logWarning(TAG + " runWithDownloader: downlaod started.");

            connection = (HttpURLConnection) _javaURL.openConnection();
            // connection.setConnectTimeout((int) downloader.getConnectTimeout().milliseconds());
            //connection.setReadTimeout((int) downloader.getReadTimeout().milliseconds());
            // connection.setUseCaches(false);
            connection.connect();
            statusCode = connection.getResponseCode();

            if (statusCode == 200) {
               data = getData(connection.getInputStream(), connection.getContentLength());
               //               final long e = SystemClock.currentThreadTimeMillis();
               //               ILogger.instance().logWarning(TAG + " runWithDownloader: downlaod: " + (e - s) + " miliseconds");
            }
         }
      }
      catch (final IOException e) {
         ILogger.instance().logError(TAG + " runWithDownloader: IOException url=" + _g3mURL._path);
         e.printStackTrace();
      }
      finally {
         if (connection != null) {
            connection.disconnect();
         }
      }

      // inform downloader to remove myself, to avoid adding new Listener
      downloader.removeDownloadingHandlerForUrl(_g3mURL._path);

      final IImage image = (_hasImageListeners && (data != null)) ? decodeImage(data, _g3mURL) : null;
      context.getThreadUtils().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, image), true);
   }


   private static IImage decodeImage(final byte[] data,
                                     final URL url) {
      // final long start = System.currentTimeMillis();
      final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, _bitmapFactoryOptions);
      // ILogger.instance().logInfo("DOWNLOADER - onDownload: Bitmap parsed in " + (System.currentTimeMillis() - start) + "ms");

      if (bitmap == null) {
         ILogger.instance().logError("Downloader_Android: Can't create image from data (URL=" + url._path + ")");
         return null;
      }

      return new Image_Android(bitmap, data);
   }


   private byte[] getData(final InputStream is,
                          final int contentLength) {
      byte[] data = null;

      try {
         final BufferedInputStream bis = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);

         final int size = (contentLength > 0) ? contentLength : DEFAULT_BUFFER_SIZE;
         final ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
         final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
         int length;
         while ((length = bis.read(buffer)) > 0) {
            baos.write(buffer, 0, length);
         }

         baos.flush();
         data = baos.toByteArray();
         baos.close();
         bis.close();
      }
      catch (final IOException e) {
         ILogger.instance().logError(TAG + " getData: IOException url=" + _g3mURL._path);
         e.printStackTrace();
      }

      return data;
   }

   private class ProcessResponseGTask
      extends
         GTask {

      private final int    _statusCode;
      private final byte[] _data;
      private IImage       _image;


      // private final Downloader_Android_Handler _handler;


      public ProcessResponseGTask(final int statusCode,
                                  final byte[] data,
                                  final IImage image
      //final Downloader_Android_Handler handler
      ) {
         _statusCode = statusCode;
         _data = data;
         _image = image;
         //_handler = handler;
      }


      @Override
      public void run(final G3MContext context) {
         //synchronized (_handler) {
         final boolean dataIsValid = (_data != null) && (_statusCode == 200);

         if (dataIsValid) {
            for (final Downloader_Android_ListenerEntry entry : _listeners) {
               final IImage imageCopy = (_image == null) ? null : _image.shallowCopy();
               if (entry.isCanceled()) {
                  entry.onCanceledDownload(_g3mURL, _data, imageCopy);

                  entry.onCancel(_g3mURL);
               }
               else {
                  entry.onDownload(_g3mURL, _data, imageCopy);
               }
            }
         }
         else {
            final ILogger logger = ILogger.instance();
            final String msg = "Error runWithDownloader: statusCode=" + _statusCode + ", url=" + _g3mURL._path;
            if (logger != null) {
               logger.logError(TAG + " " + msg);
            }
            else {
               Log.e(TAG, msg);
            }

            for (final Downloader_Android_ListenerEntry entry : _listeners) {
               entry.onError(_g3mURL);
            }
         }
         // }

         if (_image != null) {
            _image.dispose();
            _image = null;
         }
      }


      @Override
      public void dispose() {
         // TODO Auto-generated method stub

      }
   }


}
