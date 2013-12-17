

package org.glob3.mobile.specific;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;


public final class Downloader_JavaDesktop_Handler {

   private static final int                                      DEFAULT_BUFFER_SIZE = 32 * 1024;

   private final static String                                   TAG                 = "Downloader_JavaDesktop_Handler";

   private long                                                  _priority;
   private final URL                                             _g3mURL;
   private java.net.URL                                          _javaURL;
   private final ArrayList<Downloader_JavaDesktop_ListenerEntry> _listeners          = new ArrayList<Downloader_JavaDesktop_ListenerEntry>();

   private boolean                                               _hasImageListeners;


   Downloader_JavaDesktop_Handler(final URL url,
                                  final IBufferDownloadListener listener,
                                  final long priority,
                                  final long requestId) {
      _priority = priority;
      _g3mURL = url;
      _hasImageListeners = false;
      try {
         _javaURL = new java.net.URL(url.getPath());

         _listeners.add(new Downloader_JavaDesktop_ListenerEntry(listener, null, requestId));
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         else {
            ILogger.instance().logError(TAG, "MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   Downloader_JavaDesktop_Handler(final URL url,
                                  final IImageDownloadListener listener,
                                  final long priority,
                                  final long requestId) {
      _priority = priority;
      _g3mURL = url;
      _hasImageListeners = true;
      try {
         _javaURL = new java.net.URL(url.getPath());

         _listeners.add(new Downloader_JavaDesktop_ListenerEntry(null, listener, requestId));
      }
      catch (final MalformedURLException e) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + " MalformedURLException url=\"" + _g3mURL.getPath() + "\"");
         }
         else {
            ILogger.instance().logError(TAG, "MalformedURLException url=" + _g3mURL.getPath());
         }
         e.printStackTrace();

         listener.onError(url);
      }

   }


   void addListener(final IBufferDownloadListener listener,
                    final long priority,
                    final long requestId) {
      final Downloader_JavaDesktop_ListenerEntry entry = new Downloader_JavaDesktop_ListenerEntry(listener, null, requestId);

      synchronized (this) {
         _listeners.add(entry);

         if (priority > _priority) {
            _priority = priority;
         }
      }
   }


   void addListener(final IImageDownloadListener listener,
                    final long priority,
                    final long requestId) {
      final Downloader_JavaDesktop_ListenerEntry entry = new Downloader_JavaDesktop_ListenerEntry(null, listener, requestId);

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
         for (final Downloader_JavaDesktop_ListenerEntry entry : _listeners) {
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
         for (final Downloader_JavaDesktop_ListenerEntry entry : _listeners) {
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


   void runWithDownloader(final Downloader_JavaDesktop downloader,
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
            final InputStream fileIS = new FileInputStream(file);
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

      IImage image;
      try {
         image = (_hasImageListeners && (data != null)) ? decodeImage(data, _g3mURL) : null;
      }
      catch (final IOException e) {
         ILogger.instance().logError(TAG + ": " + e.getMessage());
         image = null;
      }
      context.getThreadUtils().invokeInRendererThread(new ProcessResponseGTask(statusCode, data, image), true);
   }


   private static IImage decodeImage(final byte[] data,
                                     final URL url) throws IOException {
      // final long start = System.currentTimeMillis();
      // convert byte array back to BufferedImage
      final InputStream in = new ByteArrayInputStream(data);
      BufferedImage bufferedImage;
      bufferedImage = ImageIO.read(in);
      in.close();

      if (bufferedImage == null) {
         ILogger.instance().logError("Downloader_Android: Can't create image from data (URL=" + url.getPath() + ")");
         return null;
      }

      return new Image_JavaDesktop(bufferedImage, data);


      // ILogger.instance().logInfo("DOWNLOADER - onDownload: Bitmap parsed in " + (System.currentTimeMillis() - start) + "ms");


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
         ILogger.instance().logError(TAG + " getData: IOException url=" + _g3mURL.getPath());
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
            for (final Downloader_JavaDesktop_ListenerEntry entry : _listeners) {
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
            final String msg = "Error runWithDownloader: statusCode=" + _statusCode + ", url=" + _g3mURL.getPath();
            if (logger != null) {
               logger.logError(TAG + " " + msg);
            }
            else {
               ILogger.instance().logError(TAG, msg);
            }

            for (final Downloader_JavaDesktop_ListenerEntry entry : _listeners) {
               entry.onError(_g3mURL);
            }
         }
         // }

         if (_image != null) {
            _image.dispose();
            _image = null;
         }
      }
   }


}
