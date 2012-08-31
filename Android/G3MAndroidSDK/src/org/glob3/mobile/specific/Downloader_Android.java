package org.glob3.mobile.specific;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.URL;

public class Downloader_Android implements IDownloader {

   final static String                                TAG = "Downloader_Android";

   private boolean                                    _started;
   private int                                        _maxConcurrentOperationCount;
   private int                                        _requestIdCounter;
   private long                                       _requestsCounter;
   private long                                       _cancelsCounter;
   private ArrayList<Downloader_Android_WorkerThread> _workers;
   private Map<String, Downloader_Android_Handler>    _downloadingHandlers;
   private Map<String, Downloader_Android_Handler>    _queuedHandlers;


   public Downloader_Android(int maxConcurrentOperationCount) {
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
         Downloader_Android_WorkerThread da = new Downloader_Android_WorkerThread(this);
         _workers.add(da);
      }
   }


   @Override
   public void start() {
      if (!_started) {
         Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
         while (iter.hasNext()) {
            Downloader_Android_WorkerThread worker = iter.next();
            worker.start();
         }
         _started = true;
      }
   }


   @Override
   public void stop() {
      if (_started) {
         Iterator<Downloader_Android_WorkerThread> iter = _workers.iterator();
         while (iter.hasNext()) {
            Downloader_Android_WorkerThread worker = iter.next();
            worker.stopWorkerThread();
         }
         _started = false;
      }
   }


   @Override
   public long request(URL url,
                      long priority,
                      IDownloadListener listener,
                      boolean deleteListener) {

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
   public void cancelRequest(long requestId) {
      if (requestId < 0) {
         return;
      }

      synchronized (this) {
         _cancelsCounter++;

         boolean found = false;
         Iterator<Map.Entry<String, Downloader_Android_Handler>> iter = _queuedHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            Map.Entry<String, Downloader_Android_Handler> e = iter.next();
            String url = e.getKey();
            Downloader_Android_Handler handler = e.getValue();

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
               Map.Entry<String, Downloader_Android_Handler> e = iter.next();
               Downloader_Android_Handler handler = e.getValue();

               if (handler.cancelListenerForRequestId(requestId)) {
                  found = true;
               }
            }
         }
      }
   }


   public synchronized void removeDownloadingHandlerForUrl(String url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_Android_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_Android_Handler selectedHandler = null;
      String selectedURL = null;

      synchronized (this) {
         Iterator<Map.Entry<String, Downloader_Android_Handler>> it = _queuedHandlers.entrySet().iterator();

         while (it.hasNext()) {
            Map.Entry<String, Downloader_Android_Handler> e = it.next();
            String url = e.getKey();
            Downloader_Android_Handler handler = e.getValue();
            long priority = handler.getPriority();

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
      StringBuilder_Android sb = new StringBuilder_Android();

      sb.add("Downloader_Android(downloading=").add(_downloadingHandlers.size());
      sb.add(", queued=").add(_queuedHandlers.size());
      sb.add(", totalRequests=").add(_requestsCounter);
      sb.add(", totalCancels=").add(_cancelsCounter);

      return sb.getString();
   }

}
