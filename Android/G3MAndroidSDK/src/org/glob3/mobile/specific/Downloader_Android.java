

package org.glob3.mobile.specific;


import android.content.Context;
import android.util.Log;

import org.glob3.mobile.generated.FrameTasksExecutor;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public final class Downloader_Android
   extends
      IDownloader {

   final static String                                   TAG                  = "Downloader_Android";

   private final int                                     _maxConcurrentOperationCount;
   private int                                           _requestIDCounter    = 1;
   private long                                          _requestsCounter     = 0;
   private long                                          _cancelsCounter      = 0;
   private final List<Downloader_Android_WorkerThread>   _workers;
   private final Map<String, Downloader_Android_Handler> _downloadingHandlers = new HashMap<String, Downloader_Android_Handler>();
   private final Map<String, Downloader_Android_Handler> _queuedHandlers      = new HashMap<String, Downloader_Android_Handler>();
   private final TimeInterval                            _connectTimeout;
   private final TimeInterval                            _readTimeout;
   private final Context                                 _appContext;

   private boolean                                       _started             = false;
   private final Object                                  _startStopMutex      = new Object();

   private G3MContext                                    _context;


   Downloader_Android(final int maxConcurrentOperationCount,
                      final TimeInterval connectTimeout,
                      final TimeInterval readTimeout,
                      final Context appContext) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _appContext = appContext;
      _workers = new ArrayList<Downloader_Android_WorkerThread>(maxConcurrentOperationCount);

      _connectTimeout = connectTimeout;
      _readTimeout = readTimeout;
   }


   @Override
   public void start() {
      synchronized (_startStopMutex) {
         if (!_started) {
            for (int i = 0; i < _maxConcurrentOperationCount; i++) {
               final Downloader_Android_WorkerThread da = new Downloader_Android_WorkerThread(this, i);
               _workers.add(da);
            }

            for (final Downloader_Android_WorkerThread worker : _workers) {
               worker.initialize(_context);
            }

            for (final Downloader_Android_WorkerThread worker : _workers) {
               worker.start();
            }

            _started = true;
            Log.i(TAG, "Downloader started");
         }
      }
   }


   @Override
   public void stop() {
      synchronized (_startStopMutex) {
         if (_started) {
            for (final Downloader_Android_WorkerThread worker : _workers) {
               worker.stopWorkerThread();
            }
            _started = false;


            //            boolean allWorkersStopped;
            //            do {
            //               allWorkersStopped = true;
            //               for (final Downloader_Android_WorkerThread worker : _workers) {
            //                  if (!worker.isStopped()) {
            //                     allWorkersStopped = false;
            //                     try {
            //                        Thread.sleep(2);
            //                     }
            //                     catch (final InterruptedException e) {
            //                     }
            //                     break;
            //                  }
            //               }
            //            }
            //            while (!allWorkersStopped);


            _workers.clear();
            Log.i(TAG, "Downloader stopped");
         }
      }
   }


   @Override
   public long requestBuffer(final URL url,
                             final long priority,
                             final TimeInterval timeToCache,
                             final boolean readExpired,
                             final IBufferDownloadListener listener,
                             final boolean deleteListener,
                             final String tag) {
      long requestID;

      synchronized (this) {
         _requestsCounter++;
         requestID = _requestIDCounter++;
         final String path = url._path;
         Downloader_Android_Handler handler = _downloadingHandlers.get(path);
         if (handler == null) {
            handler = _queuedHandlers.get(path);
            if (handler == null) {
               // new handler, queue it
               handler = new Downloader_Android_Handler(url, listener, deleteListener, priority, requestID, tag, _connectTimeout,
                        _readTimeout);
               _queuedHandlers.put(path, handler);
            }
            else {
               // the URL is queued for future download, just add the new listener
               handler.addListener(listener, deleteListener, priority, requestID, tag);
            }
         }
         else {
            // the URL is being downloaded, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
      }

      return requestID;
   }


   @Override
   public long requestImage(final URL url,
                            final long priority,
                            final TimeInterval timeToCache,
                            final boolean readExpired,
                            final IImageDownloadListener listener,
                            final boolean deleteListener,
                            final String tag) {
      long requestID;

      synchronized (this) {
         _requestsCounter++;
         requestID = _requestIDCounter++;
         final String path = url._path;
         Downloader_Android_Handler handler = _downloadingHandlers.get(path);
         if (handler == null) {
            handler = _queuedHandlers.get(path);
            if (handler == null) {
               // new handler, queue it
               handler = new Downloader_Android_Handler(url, listener, deleteListener, priority, requestID, tag, _connectTimeout,
                        _readTimeout);
               _queuedHandlers.put(path, handler);
            }
            else {
               // the URL is queued for future download, just add the new listener
               handler.addListener(listener, deleteListener, priority, requestID, tag);
            }
         }
         else {
            // the URL is being downloaded, just add the new listener
            handler.addListener(listener, deleteListener, priority, requestID, tag);
         }
      }

      return requestID;
   }


   @Override
   public boolean cancelRequest(final long requestID) {
      if (requestID < 0) {
         return false;
      }

      boolean found = false;

      synchronized (this) {
         _cancelsCounter++;

         Iterator<Map.Entry<String, Downloader_Android_Handler>> iter = _queuedHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            final Map.Entry<String, Downloader_Android_Handler> e = iter.next();
            final String url = e.getKey();
            final Downloader_Android_Handler handler = e.getValue();

            if (handler.removeListenerForRequestId(requestID)) {

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

               if (handler.cancelListenerForRequestId(requestID)) {
                  found = true;
               }
            }
         }
      }

      return found;
   }


   @Override
   public void cancelRequestsTagged(final String tag) {
      if (tag.isEmpty()) {
         return;
      }


      synchronized (this) {
         _cancelsCounter++;

         for (final Iterator<Entry<String, Downloader_Android_Handler>> iterator = _queuedHandlers.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry<String, Downloader_Android_Handler> entry = iterator.next();

            final Downloader_Android_Handler handler = entry.getValue();
            if (handler.removeListenersTagged(tag)) {
               if (!handler.hasListener()) {
                  iterator.remove();
               }
            }
         }

         for (final Downloader_Android_Handler handler : _downloadingHandlers.values()) {
            handler.cancelListenersTagged(tag);
         }
      }
   }


   public synchronized void removeDownloadingHandlerForUrl(final String url) {
      _downloadingHandlers.remove(url);
   }


   @Override
   public synchronized void initialize(final G3MContext context,
                                       final FrameTasksExecutor frameTasksExecutor) {
      _context = context;
      for (final Downloader_Android_WorkerThread worker : _workers) {
         worker.initialize(_context);
      }
   }


   Downloader_Android_Handler getHandlerToRun() {
      Downloader_Android_Handler selectedHandler = null;

      synchronized (this) {
         if (_context == null) {
            return null;
         }

         if (_queuedHandlers.isEmpty()) {
            return null;
         }

         long selectedPriority = Long.MIN_VALUE;
         String selectedURL = null;
         for (final Map.Entry<String, Downloader_Android_Handler> e : _queuedHandlers.entrySet()) {
            //            final Map.Entry<String, Downloader_Android_Handler> e = it.next();
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

      sb.addString("Downloader_Android(downloading=");
      sb.addInt(_downloadingHandlers.size());
      sb.addString(", queued=");
      sb.addInt(_queuedHandlers.size());
      sb.addString(", totalRequests=");
      sb.addLong(_requestsCounter);
      sb.addString(", totalCancels=");
      sb.addLong(_cancelsCounter);

      return sb.getString();
   }


   //   public TimeInterval getConnectTimeout() {
   //      return _connectTimeout;
   //   }
   //
   //
   //   public TimeInterval getReadTimeout() {
   //      return _readTimeout;
   //   }


   @Override
   public void onResume(final G3MContext context) {
      start();
   }


   @Override
   public void onPause(final G3MContext context) {
      stop();
   }


   public Context getAppContext() {
      return _appContext;
   }


   @Override
   public void onDestroy(final G3MContext context) {
      stop();
   }


}
