

package org.glob3.mobile.specific;

import android.util.Log;


public final class Downloader_Android_WorkerThread
         extends
            Thread {

   final static String              TAG        = "Downloader_Android_WorkerThread";

   private final Downloader_Android _downloader;
   private boolean                  _stopping;

   private boolean                  _isStopped = false;


   public Downloader_Android_WorkerThread(final Downloader_Android downloader) {
      _downloader = downloader;
      _stopping = false;

      this.setPriority(9);
   }


   public synchronized void stopWorkerThread() {
      _stopping = true;
   }


   public synchronized boolean isStopping() {
      return _stopping;
   }


   @Override
   public void run() {
      while (!isStopping()) {
         final Downloader_Android_Handler handler = _downloader.getHandlerToRun();

         if (handler != null) {
            handler.runWithDownloader(_downloader);
         }
         else {
            try {
               Thread.sleep(25);
               //               Log.i(TAG, "awake");
            }
            catch (final InterruptedException e) {
               Log.e(TAG, "InterruptedException worker=" + this.toString());
               e.printStackTrace();
            }
         }
      }
      _isStopped = true;
      Log.i(TAG, "finish run");
   }


   public synchronized boolean isStopped() {
      return _isStopped;
   }

}
