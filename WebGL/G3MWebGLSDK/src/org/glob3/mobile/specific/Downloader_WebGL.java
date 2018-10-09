

package org.glob3.mobile.specific;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.FrameTasksExecutor;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;


public final class Downloader_WebGL
         extends
            IDownloader {

   private final int                                _maxConcurrentOperationCount;
   private final Map<URL, Downloader_WebGL_Handler> _downloadingHandlers;
   private final Map<URL, Downloader_WebGL_Handler> _queuedHandlers;
   private final Timer                              _timer;
   private final int                                _delayMillis;
   private final String                             _proxy;
   public final boolean                             _verboseErrors;

   private long _requestIDCounter;
   private long _requestsCounter;
   private long _cancelsCounter;


   public Downloader_WebGL(final int maxConcurrentOperationCount,
                           final int delayMillis,
                           final String proxy,
                           final boolean verboseErrors) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIDCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      _downloadingHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _queuedHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _delayMillis = delayMillis;
      _verboseErrors = verboseErrors;

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
   public long requestBuffer(final URL url,
                             final long priority,
                             final TimeInterval timeToCache,
                             final boolean readExpired,
                             final IBufferDownloadListener listener,
                             final boolean deleteListener,
                             final String tag) {
      final long requestID;
      Downloader_WebGL_Handler handler = null;
      final URL proxyUrl = getProxiedURL(url);

      _requestsCounter++;
      requestID = _requestIDCounter++;
      handler = _downloadingHandlers.get(proxyUrl);

      if ((handler != null) && !handler.isRequestingImage()) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, deleteListener, priority, requestID, tag);
      }
      else {
         handler = _queuedHandlers.get(proxyUrl);
         if ((handler != null) && !handler.isRequestingImage()) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
         else {
            // new handler, queue it
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, deleteListener, priority, requestID, tag);
            _queuedHandlers.put(proxyUrl, handler);
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
   public long requestImage(final URL url,
                            final long priority,
                            final TimeInterval timeToCache,
                            final boolean readExpired,
                            final IImageDownloadListener listener,
                            final boolean deleteListener,
                            final String tag) {
      final long requestID;
      Downloader_WebGL_Handler handler = null;
      final URL proxyUrl = getProxiedURL(url);

      _requestsCounter++;
      requestID = _requestIDCounter++;
      handler = _downloadingHandlers.get(proxyUrl);

      if ((handler != null) && handler.isRequestingImage()) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, deleteListener, priority, requestID, tag);
      }
      else {
         handler = _queuedHandlers.get(proxyUrl);
         if ((handler != null) && handler.isRequestingImage()) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
         else {
            // new handler, queue it
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, deleteListener, priority, requestID, tag);
            _queuedHandlers.put(proxyUrl, handler);
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

      boolean found = false;
      Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> iter = _queuedHandlers.entrySet().iterator();

      while (iter.hasNext() && !found) {
         final Map.Entry<URL, Downloader_WebGL_Handler> e = iter.next();
         final Downloader_WebGL_Handler handler = e.getValue();

         if (handler.removeListenerForRequestId(requestID)) {
            if (!handler.hasListener()) {
               iter.remove();
            }
            found = true;
         }
      }

      if (!found) {
         iter = _downloadingHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            final Map.Entry<URL, Downloader_WebGL_Handler> e = iter.next();
            final Downloader_WebGL_Handler handler = e.getValue();

            if (handler.cancelListenerForRequestId(requestID)) {
               found = true;
            }
         }
      }

      return found;
   }


   @Override
   public void cancelRequestsTagged(final String tag) {
      if ((tag == null) || tag.isEmpty()) {
         return;
      }

      _cancelsCounter++;

      for (final Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> iterator = _queuedHandlers.entrySet().iterator(); iterator.hasNext();) {
         final Map.Entry<URL, Downloader_WebGL_Handler> entry = iterator.next();
         final Downloader_WebGL_Handler handler = entry.getValue();

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
      final StringBuilder_WebGL sb = new StringBuilder_WebGL();

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


   public void removeDownloadingHandlerForUrl(final URL url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_WebGL_Handler getHandlerToRun() {
      if (_queuedHandlers.isEmpty()) {
         return null;
      }

      Downloader_WebGL_Handler selectedHandler = null;
      URL selectedURL = null;

      {
         long selectedPriority = Long.MIN_VALUE;
         for (final Map.Entry<URL, Downloader_WebGL_Handler> entry : _queuedHandlers.entrySet()) {
            final Downloader_WebGL_Handler candidateHandler = entry.getValue();
            final long candidatePriority = candidateHandler.getPriority();

            if (candidatePriority > selectedPriority) {
               final URL url = entry.getKey();
               selectedPriority = candidatePriority;
               selectedHandler = candidateHandler;
               selectedURL = url;
            }
         }
      }

      if (selectedHandler != null) {
         // move the selected handler to _downloadingHandlers collection
         _queuedHandlers.remove(selectedURL);
         _downloadingHandlers.put(selectedURL, selectedHandler);
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
   public void initialize(final G3MContext context,
                          final FrameTasksExecutor frameTasksExecutor) {

   }


}
