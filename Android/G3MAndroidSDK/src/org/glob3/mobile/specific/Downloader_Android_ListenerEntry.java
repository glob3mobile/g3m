

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;

import android.util.Log;


public final class Downloader_Android_ListenerEntry {

   private final static String           TAG = "Downloader_Android_ListenerEntry";


   private final IBufferDownloadListener _bufferListener;
   private final IImageDownloadListener  _imageListener;
   private final boolean                 _deleteListener;
   final long                            _requestId;
   private boolean                       _canceled;


   Downloader_Android_ListenerEntry(final IBufferDownloadListener bufferListener,
                                    final IImageDownloadListener imageListener,
                                    final boolean deleteListener,
                                    final long requestId) {
      _bufferListener = bufferListener;
      _imageListener = imageListener;
      _deleteListener = deleteListener;
      _requestId = requestId;
      _canceled = false;
   }


   void cancel() {
      if (_canceled) {
         if (ILogger.instance() == null) {
            Log.e(TAG, "Listener for requestId=" + _requestId + " already canceled");
         }
         else {
            ILogger.instance().logError(TAG + ": Listener for requestId=" + _requestId + " already canceled");
         }
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
                   final byte[] data,
                   final IImage image) {
      if (_bufferListener != null) {
         final IByteBuffer buffer = new ByteBuffer_Android(data);
         _bufferListener.onDownload(url, buffer, false);
         if (_deleteListener) {
            _bufferListener.dispose();
         }
      }

      if (_imageListener != null) {
         if (image == null) {
            ILogger.instance().logError("Downloader_Android: Can't create image from data (URL=" + url._path + ")");
            _imageListener.onError(url);
         }
         else {
            _imageListener.onDownload(url, image, false);
         }
         if (_deleteListener) {
            _imageListener.dispose();
         }
      }
   }


   void onCanceledDownload(final URL url,
                           final byte[] data,
                           final IImage image) {
      if (_bufferListener != null) {
         final IByteBuffer buffer = new ByteBuffer_Android(data);
         _bufferListener.onCanceledDownload(url, buffer, false);
      }

      if (_imageListener != null) {
         if (image == null) {
            ILogger.instance().logError("Downloader_Android: Can't create image from data (URL=" + url._path + ")");
         }
         else {
            _imageListener.onCanceledDownload(url, image, false);
            image.dispose();
         }
      }
   }


}
