

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.ILogger;


public final class Downloader_JavaDesktop_WorkerThread
         extends
            Thread {


   private final Downloader_JavaDesktop _downloader;
   private boolean                      _stopping;

   private boolean                      _isStopped = false;

   private final int                    _id;
   private G3MContext                   _context;


   Downloader_JavaDesktop_WorkerThread(final Downloader_JavaDesktop downloader,
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
      ILogger.instance().logInfo(getClass().getName(), "Downloader-WorkerThread #" + _id + " started");
   }


   @Override
   public void run() {
      while (!isStopping()) {
         final Downloader_JavaDesktop_Handler handler = _downloader.getHandlerToRun();

         if (handler != null) {
            handler.runWithDownloader(_downloader, _context);
         }
         else {
            try {
               Thread.sleep(25);
            }
            catch (final InterruptedException e) {
               ILogger.instance().logError(getClass().getName(), "InterruptedException worker: " + this.toString());
               e.printStackTrace();
            }
         }
      }
      synchronized (this) {
         _isStopped = true;
      }
      ILogger.instance().logInfo(getClass().getName(), "Downloader-WorkerThread #" + _id + " stopped");
   }


   public synchronized boolean isStopped() {
      return _isStopped;
   }


   public void initialize(final G3MContext context) {
      _context = context;
   }

}
