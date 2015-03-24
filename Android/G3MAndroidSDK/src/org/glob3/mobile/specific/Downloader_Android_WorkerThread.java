

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.G3MContext;

import android.util.Log;


public final class Downloader_Android_WorkerThread
         extends
            Thread {


   private final Downloader_Android _downloader;
   private boolean                  _stopping;

   private boolean                  _isStopped = false;

   private final int                _id;
   private G3MContext               _context;


   Downloader_Android_WorkerThread(final Downloader_Android downloader,
                                   final int id) {
      _downloader = downloader;
      _stopping = false;
      _id = id;

      setName("Downloader_WorkerThread #" + _id);

      setPriority(Thread.NORM_PRIORITY - 1);
   }


   public synchronized void stopWorkerThread() {
      _stopping = true;
   }


   public synchronized boolean isStopping() {
      return _stopping;
   }


   @Override
   public synchronized void start() {
      super.start();
      Log.i(getClass().getName(), "Downloader-WorkerThread #" + _id + " started");
   }


   @Override
   public void run() {
      while (!isStopping()) {
         final Downloader_Android_Handler handler = _downloader.getHandlerToRun();

         if (handler != null) {
            handler.runWithDownloader(_downloader, _context);
         }
         else {
            try {
               Thread.sleep(250);
            }
            catch (final InterruptedException e) {
               Log.e(getClass().getName(), "InterruptedException worker: " + this.toString());
               e.printStackTrace();
            }
         }
      }
      synchronized (this) {
         _isStopped = true;
      }
      Log.i(getClass().getName(), "Downloader-WorkerThread #" + _id + " stopped");
   }


   public synchronized boolean isStopped() {
      return _isStopped;
   }


   public void initialize(final G3MContext context) {
      _context = context;
   }

}
