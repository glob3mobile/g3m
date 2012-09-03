

package org.glob3.mobile.specific;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.URL;


public class Downloader_Android
         implements
            IDownloader {

   final static String                                      TAG = "Downloader_Android";

   private boolean                                          _started;
   private final int                                        _maxConcurrentOperationCount;
   private int                                              _requestIdCounter;
   private long                                             _requestsCounter;
   private long                                             _cancelsCounter;
   private final ArrayList<Downloader_Android_WorkerThread> _workers;
   private final Map<String, Downloader_Android_Handler>    _downloadingHandlers;
   private final Map<String, Downloader_Android_Handler>    _queuedHandlers;


   public Downloader_Android(final int maxConcurrentOperationCount) {
      _started = false;
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIdCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      // TODO String or Url as key??
      _downloadingHandlers = new HashMap<String, Downloader_Android_Handler>();
      _queuedHandlers = new HashMap<String, Downloader_Android_Handler>();
      _workers = new ArrayList<Downloader_Android_WorkerThread>(maxConcurrentOperationCount);

      for (int i = 0; i < _maxConcurrentOperationCount; i++) {
         final Downloader_Android_WorkerThread da = new Downloader_Android_WorkerThread(this);
         _workers.add(da);
      }
   }


   @Override
   public void start() {
      if (!_started) {
         final Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
         while (iter.hasNext()) {
            final Downloader_Android_WorkerThread worker = iter.next();
            worker.start();
         }
         _started = true;
      }
   }


   @Override
   public void stop() {
      if (_started) {
         final Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
         while (iter.hasNext()) {
            final Downloader_Android_WorkerThread worker = iter.next();
            worker.stopWorkerThread();
         }
         _started = false;
      }
   }


   @Override
   public long request(final URL url,
                       final long priority,
                       final IDownloadListener listener,
                       final boolean deleteListener) {

      Downloader_Android_Handler handler = null;
      long requestId;

      synchronized (this) {
         _requestsCounter++;
         requestId = _requestIdCounter++;
         handler = _downloadingHandlers.get(url.getPath());

         if (handler != null) {
            // the URL is being downloaded, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            handler = _queuedHandlers.get(url.getPath());
            if (handler != null) {
               // the URL is queued for future download, just add the new listener
               handler.addListener(listener, priority, requestId);
            }
            else {
               // new handler, queue it
               handler = new Downloader_Android_Handler(url, listener, priority, requestId);
               _queuedHandlers.put(url.getPath(), handler);
            }
         }
      }

      return requestId;
   }


   @Override
   public void cancelRequest(final long requestId) {
      if (requestId < 0) {
         return;
      }

      synchronized (this) {
         _cancelsCounter++;

         boolean found = false;
         Iterator<Map.Entry<String, Downloader_Android_Handler>> iter = _queuedHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            final Map.Entry<String, Downloader_Android_Handler> e = iter.next();
            final String url = e.getKey();
            final Downloader_Android_Handler handler = e.getValue();

            if (handler.removeListenerForRequestId(requestId)) {

               if (!handler.hasListener()) {
                  _queuedHandlers.remove(url);
               }
               found = true;
            }
         }

         if (!found) {
            iter = _downloadingHandlers.entrySet().iterator();

            while (iter.hasNext() && !found) {
               final Map.Entry<String, Downloader_Android_Handler> e = iter.next();
               final Downloader_Android_Handler handler = e.getValue();

               if (handler.cancelListenerForRequestId(requestId)) {
                  found = true;
               }
            }
         }
      }
   }


   public synchronized void removeDownloadingHandlerForUrl(final String url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_Android_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_Android_Handler selectedHandler = null;
      String selectedURL = null;

      synchronized (this) {
         final Iterator<Map.Entry<String, Downloader_Android_Handler>> it = _queuedHandlers.entrySet().iterator();

         while (it.hasNext()) {
            final Map.Entry<String, Downloader_Android_Handler> e = it.next();
            final String url = e.getKey();
            final Downloader_Android_Handler handler = e.getValue();
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
      }

      return selectedHandler;
   }


   @Override
   public String statistics() {
      final StringBuilder_Android sb = new StringBuilder_Android();

      sb.add("Downloader_Android(downloading=").add(_downloadingHandlers.size());
      sb.add(", queued=").add(_queuedHandlers.size());
      sb.add(", totalRequests=").add(_requestsCounter);
      sb.add(", totalCancels=").add(_cancelsCounter);

      return sb.getString();
   }

}
