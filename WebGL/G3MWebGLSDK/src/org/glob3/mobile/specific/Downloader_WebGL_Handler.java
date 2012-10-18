

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.JavaScriptObject;


public interface Downloader_WebGL_Handler {

   public void init(final URL url,
                    final IBufferDownloadListener bufferListener,
                    final long priority,
                    final long requestId);


   public void init(final URL url,
                    final IImageDownloadListener imageListener,
                    final long priority,
                    final long requestId);


   public boolean isRequestingImage();


   public void addListener(final IBufferDownloadListener listener,
                           final long priority,
                           final long requestId);


   public void addListener(final IImageDownloadListener listener,
                           final long priority,
                           final long requestId);


   public long getPriority();


   public boolean cancelListenerForRequestId(final long requestId);


   public boolean removeListenerForRequestId(final long requestId);


   public boolean hasListener();


   public void runWithDownloader(final IDownloader downloader);


   public void removeFromDownloaderDownloadingHandlers();


   public void processResponse(final int statusCode,
                               final JavaScriptObject data);


   public void jsRequest(String url);

}
