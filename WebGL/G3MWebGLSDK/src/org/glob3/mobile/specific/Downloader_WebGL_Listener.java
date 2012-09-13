

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IImageDownloadListener;


public class Downloader_WebGL_Listener {
   private final IBufferDownloadListener _bufferListener;
   private final IImageDownloadListener  _imageListener;


   public Downloader_WebGL_Listener(final IBufferDownloadListener bufferListener) {
      _bufferListener = bufferListener;
      _imageListener = null;
   }


   public Downloader_WebGL_Listener(final IImageDownloadListener imageListener) {
      _bufferListener = null;
      _imageListener = imageListener;
   }
}
