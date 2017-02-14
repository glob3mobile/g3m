

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.JavaScriptObject;


public interface Downloader_WebGL_Handler {

   void init(final URL url,
             final IBufferDownloadListener bufferListener,
             final boolean deleteListener,
             final long priority,
             final long requestID,
             final String tag);


   void init(final URL url,
             final IImageDownloadListener imageListener,
             final boolean deleteListener,
             final long priority,
             final long requestID,
             final String tag);


   boolean isRequestingImage();


   void addListener(final IBufferDownloadListener listener,
                    final boolean deleteListener,
                    final long priority,
                    final long requestID,
                    final String tag);


   void addListener(final IImageDownloadListener listener,
                    final boolean deleteListener,
                    final long priority,
                    final long requestID,
                    final String tag);


   long getPriority();


   boolean cancelListenerForRequestId(final long requestID);


   boolean removeListenerForRequestId(final long requestID);


   boolean hasListener();


   void runWithDownloader(final IDownloader downloader);


   void removeFromDownloaderDownloadingHandlers();


   void processResponse(final int statusCode,
                        final JavaScriptObject data);


   void jsRequest(String url);


   boolean removeListenersTagged(String tag);


   void cancelListenersTagged(String tag);

}
