package org.glob3.mobile.specific;

import android.util.Log;


public class Downloader_Android_WorkerThread extends Thread {

   final static String TAG = "Downloader_Android_WorkerThread";

   Downloader_Android  _downloader;
   boolean             _stopping;


   public Downloader_Android_WorkerThread(Downloader_Android downloader) {
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
         Downloader_Android_Handler handler = _downloader.getHandlerToRun();

         if (handler != null) {
            handler.runWithDownloader(_downloader);
         }
         else {
            try {
               Thread.sleep(25);
               //               Log.i(TAG, "awake");
            }
            catch (InterruptedException e) {
               Log.e(TAG, "InterruptedException worker=" + this.toString());
               e.printStackTrace();
            }
         }
      }
      Log.i(TAG, "finish run");
   }

}