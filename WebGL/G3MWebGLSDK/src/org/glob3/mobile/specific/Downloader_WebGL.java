
package org.glob3.mobile.specific;

import java.util.*;
import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;
import com.google.gwt.user.client.Timer;

public final class Downloader_WebGL extends IDownloader {

   // the same URL can be requested as an image and as a buffer at once
   private static final class HandlerKey {
      private final URL     _url;
      private final boolean _isImageRequest;

      private HandlerKey(final URL url,
                         final boolean isImageRequest) {
         _url            = url;
         _isImageRequest = isImageRequest;
      }

      @Override
      public int hashCode() {
         return Objects.hash(_url, _isImageRequest);
      }

      @Override
      public boolean equals(final Object obj) {
         if (this == obj) {
            return true;
         }
         if (!(obj instanceof HandlerKey)) {
            return false;
         }
         final HandlerKey that = (HandlerKey) obj;
         return (_isImageRequest == that._isImageRequest) && _url.equals(that._url);
      }
   }

   private final int                                       _maxConcurrentOperationCount;
   private final Map<HandlerKey, Downloader_WebGL_Handler> _downloadingHandlers;
   private final Map<HandlerKey, Downloader_WebGL_Handler> _queuedHandlers;
   private final Timer                                     _timer;
   private final int                                       _delayMillis;
   private final String                                    _proxy;
   public final boolean                                    _verboseErrors;

   private long _requestIDCounter;
   private long _requestsCounter;
   private long _cancelsCounter;

   public Downloader_WebGL(final int maxConcurrentOperationCount,
                           final int delayMillis,
                           final String proxy,
                           final boolean verboseErrors) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIDCounter            = 1;
      _requestsCounter             = 0;
      _cancelsCounter              = 0;
      _downloadingHandlers         = new HashMap<>();
      _queuedHandlers              = new HashMap<>();
      _delayMillis                 = delayMillis;
      _verboseErrors               = verboseErrors;

      if (proxy == null) {
         _proxy = null;
      }
      else {
         final String trimmedProxy = proxy.trim();
         if (trimmedProxy.isEmpty()) {
            _proxy = defineDefaultProxy();
         }
         else {
            _proxy = trimmedProxy;
         }
      }

      final Downloader_WebGL thisDownloader = this;
      _timer = new Timer() {
         @Override
         public void run() {
            if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
               final Downloader_WebGL_Handler handler = getHandlerToRun();
               if (handler != null) {
                  handler.runWithDownloader(thisDownloader);
               }
            }
            sendRequest();
         }
      };
   }

   @Override
   public void start() {
      sendRequest();
   }

   @Override
   public void stop() {
      _timer.cancel();
   }

   private void sendRequest() {
      _timer.schedule(_delayMillis);
   }

   @Override
   public long requestBuffer(final URL url, final long priority, final TimeInterval timeToCache, final boolean readExpired,
                             final IBufferDownloadListener listener, final boolean deleteListener, final String tag) {
      final long               requestID;
      Downloader_WebGL_Handler handler  = null;
      final URL                proxyUrl = getProxiedURL(url);
      final HandlerKey         key      = new HandlerKey(proxyUrl, false);

      _requestsCounter++;
      requestID = _requestIDCounter++;
      handler   = _downloadingHandlers.get(key);

      if (handler != null) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, deleteListener, priority, requestID, tag);
      }
      else {
         handler = _queuedHandlers.get(key);
         if (handler != null) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
         else {
            // new handler, queue it
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, deleteListener, priority, requestID, tag);
            _queuedHandlers.put(key, handler);
         }
      }

      return requestID;
   }

   private URL getProxiedURL(final URL url) {
      if (_proxy == null) {
         return url;
      }

      final String urlPath = url._path;
      if (!urlPath.startsWith("http://") && !urlPath.startsWith("https://")) {
         // assumes the URL is a relative URL to the server, no need to use proxy
         return url;
      }

      // return proxied url
      return new URL(_proxy + urlPath);
   }

   @Override
   public long requestImage(final URL url, final long priority, final TimeInterval timeToCache, final boolean readExpired,
                            final IImageDownloadListener listener, final boolean deleteListener, final String tag) {
      final long               requestID;
      Downloader_WebGL_Handler handler  = null;
      final URL                proxyUrl = getProxiedURL(url);
      final HandlerKey         key      = new HandlerKey(proxyUrl, true);

      _requestsCounter++;
      requestID = _requestIDCounter++;
      handler   = _downloadingHandlers.get(key);

      if (handler != null) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, deleteListener, priority, requestID, tag);
      }
      else {
         handler = _queuedHandlers.get(key);
         if (handler != null) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
         else {
            // new handler, queue it
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, deleteListener, priority, requestID, tag);
            _queuedHandlers.put(key, handler);
         }
      }

      return requestID;
   }

   @Override
   public boolean cancelRequest(final long requestID) {
      if (requestID < 0) {
         return false;
      }

      _cancelsCounter++;

      {
         final Iterator<Map.Entry<HandlerKey, Downloader_WebGL_Handler>> iter = _queuedHandlers.entrySet().iterator();
         while (iter.hasNext()) {
            final Map.Entry<HandlerKey, Downloader_WebGL_Handler> e       = iter.next();
            final Downloader_WebGL_Handler                        handler = e.getValue();

            if (handler.removeListenerForRequestId(requestID)) {
               if (!handler.hasListener()) {
                  iter.remove();
               }
               return true;
            }
         }
      }

      for (final Map.Entry<HandlerKey, Downloader_WebGL_Handler> e : _downloadingHandlers.entrySet()) {
         final Downloader_WebGL_Handler handler = e.getValue();
         if (handler.cancelListenerForRequestId(requestID)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void cancelRequestsTagged(final String tag) {
      if ((tag == null) || tag.isEmpty()) {
         return;
      }

      _cancelsCounter++;

      for (final Iterator<Map.Entry<HandlerKey, Downloader_WebGL_Handler>> iterator = _queuedHandlers.entrySet().iterator(); iterator.hasNext();) {
         final Map.Entry<HandlerKey, Downloader_WebGL_Handler> entry   = iterator.next();
         final Downloader_WebGL_Handler                        handler = entry.getValue();

         if (handler.removeListenersTagged(tag)) {
            if (!handler.hasListener()) {
               iterator.remove();
            }
         }
      }

      for (final Downloader_WebGL_Handler handler : _downloadingHandlers.values()) {
         handler.cancelListenersTagged(tag);
      }
   }

   @Override
   public String statistics() {
      final StringBuilder_WebGL sb = new StringBuilder_WebGL(IStringBuilder.DEFAULT_FLOAT_PRECISION);

      sb.addString("Downloader_WebGL(downloading=");
      sb.addInt(_downloadingHandlers.size());
      sb.addString(", queued=");
      sb.addInt(_queuedHandlers.size());
      sb.addString(", totalRequests=");
      sb.addLong(_requestsCounter);
      sb.addString(", totalCancels=");
      sb.addLong(_cancelsCounter);

      return sb.getString();
   }

   public void removeDownloadingHandler(final Downloader_WebGL_Handler handler) {
      _downloadingHandlers.values().remove(handler);
   }

   public Downloader_WebGL_Handler getHandlerToRun() {
      if (_queuedHandlers.isEmpty()) {
         return null;
      }

      Downloader_WebGL_Handler selectedHandler = null;
      HandlerKey               selectedKey     = null;

      {
         long selectedPriority = Long.MIN_VALUE;
         for (final Map.Entry<HandlerKey, Downloader_WebGL_Handler> entry : _queuedHandlers.entrySet()) {
            final Downloader_WebGL_Handler candidateHandler  = entry.getValue();
            final long                     candidatePriority = candidateHandler.getPriority();

            // equal priorities go oldest request first; the HashMap order is arbitrary
            if ((candidatePriority > selectedPriority) ||
                ((candidatePriority == selectedPriority) && (selectedHandler != null) &&
                 (candidateHandler.getFirstRequestID() < selectedHandler.getFirstRequestID()))) {
               selectedPriority = candidatePriority;
               selectedHandler  = candidateHandler;
               selectedKey      = entry.getKey();
            }
         }
      }

      if (selectedHandler != null) {
         // move the selected handler to _downloadingHandlers collection
         _queuedHandlers.remove(selectedKey);
         _downloadingHandlers.put(selectedKey, selectedHandler);
      }

      return selectedHandler;
   }

   static private String defineDefaultProxy() {
      return GWT.getHostPageBaseURL() + "proxy?url=";
   }

   @Override
   public void onResume(final G3MContext context) {
   }

   @Override
   public void onPause(final G3MContext context) {
   }

   @Override
   public void onDestroy(final G3MContext context) {
   }

   @Override
   public void initialize(final G3MContext context, final FrameTasksExecutor frameTasksExecutor) {

   }

}
