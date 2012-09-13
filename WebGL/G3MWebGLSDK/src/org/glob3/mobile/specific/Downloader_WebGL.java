

package org.glob3.mobile.specific;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.URL;

import com.google.gwt.user.client.Timer;


public class Downloader_WebGL
         implements
            IDownloader {

   final private int                                _maxConcurrentOperationCount;
   private long                                     _requestIdCounter;
   private long                                     _requestsCounter;
   private final long                               _cancelsCounter;
   private final Map<URL, Downloader_WebGL_Handler> _downloadingHandlers;
   private final Map<URL, Downloader_WebGL_Handler> _queuedHandlers;


   public Downloader_WebGL(final int maxConcurrentOperationCount) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIdCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      _downloadingHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _queuedHandlers = new HashMap<URL, Downloader_WebGL_Handler>();

      //      jsDefineURLObject();
   }


   @Override
   public void start() {
      sendRequest();
   }


   @Override
   public void stop() {
      // TODO Auto-generated method stub

   }


   @Override
   public long requestBuffer(final URL url,
                             final long priority,
                             final IBufferDownloadListener listener,
                             final boolean deleteListener) {

      final long requestId;
      Downloader_WebGL_Handler handler = null;

      _requestsCounter++;
      requestId = _requestIdCounter++;
      handler = _downloadingHandlers.get(url);

      if (handler != null) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, priority, requestId);
      }
      else {
         handler = _queuedHandlers.get(url);
         if (handler != null) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            // new handler, queue it
            handler = new Downloader_WebGL_Handler(url, listener, priority, requestId);
            _queuedHandlers.put(url, handler);
         }
      }

      return requestId;
   }


   @Override
   public void cancelRequest(final long requestId) {
      // TODO Auto-generated method stub

   }


   @Override
   public String statistics() {
      final StringBuilder_WebGL sb = new StringBuilder_WebGL();

      sb.add("Downloader_WebGL(downloading=").add(_downloadingHandlers.size());
      sb.add(", queued=").add(_queuedHandlers.size());
      sb.add(", totalRequests=").add(_requestsCounter);
      sb.add(", totalCancels=").add(_cancelsCounter);

      return sb.getString();
   }


   public void removeDownloadingHandlerForUrl(final URL url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_WebGL_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_WebGL_Handler selectedHandler = null;
      URL selectedURL = null;

      final Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> it = _queuedHandlers.entrySet().iterator();

      while (it.hasNext()) {
         final Map.Entry<URL, Downloader_WebGL_Handler> e = it.next();
         final URL url = e.getKey();
         final Downloader_WebGL_Handler handler = e.getValue();
         final long priority = handler.getPriority();

         if (priority > selectedPriority) {
            selectedPriority = priority;
            selectedHandler = handler;
            selectedURL = url;
         }
      }

      if (selectedHandler != null) {
         // move the selected handler to _downloadingHandlers collection
         _queuedHandlers.remove(selectedURL);
         _downloadingHandlers.put(selectedURL, selectedHandler);
      }

      return selectedHandler;
   }


   private void sendRequest() {
      final Downloader_WebGL thisDownloader = this;

      final Timer timer = new Timer() {
         @Override
         public void run() {
            if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
               final Downloader_WebGL_Handler handler = getHandlerToRun();

               if (handler != null) {
                  handler.runWithDownloader(thisDownloader);
                  sendRequest();
               }
            }

         }
      };

      timer.schedule(100);
   }


   @Override
   public long requestImage(final URL url,
                            final long priority,
                            final IImageDownloadListener listener,
                            final boolean deleteListener) {
      // TODO Auto-generated method stub
      return 0;
   }


}
