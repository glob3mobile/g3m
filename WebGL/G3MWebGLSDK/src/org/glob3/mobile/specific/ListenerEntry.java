

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;


public final class ListenerEntry {

   final static String                   TAG = "ListenerEntry";

   private final IBufferDownloadListener _bufferListener;
   private final IImageDownloadListener  _imageListener;
   private final boolean                 _deleteListener;
   private final long                    _requestId;
   private boolean                       _canceled;


   public ListenerEntry(final IBufferDownloadListener bufferListener,
                        final IImageDownloadListener imageListener,
                        final boolean deleteListener,
                        final long requestId) {
      _bufferListener = bufferListener;
      _imageListener = imageListener;
      _deleteListener = deleteListener;
      _requestId = requestId;
      _canceled = false;
   }


   public long getRequestId() {
      return _requestId;
   }


   public IBufferDownloadListener getBufferListener() {
      return _bufferListener;
   }


   public IImageDownloadListener getImageListener() {
      return _imageListener;
   }


   public void cancel() {
      if (_canceled) {
         log(LogLevel.ErrorLevel, ": Listener for requestId=" + _requestId + " already canceled");
      }
      _canceled = true;
   }


   public boolean isCanceled() {
      return _canceled;
   }


   void onCancel(final URL url) {
      if (_bufferListener != null) {
         _bufferListener.onCancel(url);
         if (_deleteListener) {
            _bufferListener.dispose();
         }
      }
      if (_imageListener != null) {
         _imageListener.onCancel(url);
         if (_deleteListener) {
            _imageListener.dispose();
         }
      }
   }


   void onError(final URL url) {
      if (_bufferListener != null) {
         _bufferListener.onError(url);
         if (_deleteListener) {
            _bufferListener.dispose();
         }
      }
      if (_imageListener != null) {
         _imageListener.onError(url);
         if (_deleteListener) {
            _imageListener.dispose();
         }
      }
   }


   void onDownload(final URL url,
                   final JavaScriptObject data) {
      if (_bufferListener != null) {
         final IByteBuffer byteBuffer = new ByteBuffer_WebGL(data);

         _bufferListener.onDownload(url, byteBuffer, false);

         if (_deleteListener) {
            _bufferListener.dispose();
         }
      }
      if (_imageListener != null) {
         final Image_WebGL image = new Image_WebGL(data);

         if (image.getImage() == null) {
            log(LogLevel.ErrorLevel, ": Can't create image from data (URL=" + url._path + ")");
            _imageListener.onError(url);
         }
         else {
            _imageListener.onDownload(url, image, false);
            //IFactory.instance().deleteImage(image);
         }

         if (_deleteListener) {
            _imageListener.dispose();
         }
      }
   }


   void onCanceledDownload(final URL url,
                           final JavaScriptObject data) {
      if (_bufferListener != null) {
         final IByteBuffer byteBuffer = new ByteBuffer_WebGL(data);

         _bufferListener.onCanceledDownload(url, byteBuffer, false);
      }
      if (_imageListener != null) {
         final Image_WebGL image = new Image_WebGL(data);

         if (image.getImage() == null) {
            log(LogLevel.ErrorLevel, ": Can't create image from data (URL=" + url._path + ")");
         }
         else {
            _imageListener.onCanceledDownload(url, image, false);
            image.dispose();
         }
      }
   }


   public void log(final LogLevel level,
                   final String msg) {
      if (ILogger.instance() != null) {
         switch (level) {
            case InfoLevel:
               ILogger.instance().logInfo(TAG + msg);
               break;
            case WarningLevel:
               ILogger.instance().logWarning(TAG + msg);
               break;
            case ErrorLevel:
               ILogger.instance().logError(TAG + msg);
               break;
            default:
               break;
         }
      }
      else {
         GWT.log(TAG + msg);
      }

   }
}
