package org.glob3.mobile.specific;

import android.os.AsyncTask;
import android.util.Log;

public class Downloader_Android_WorkerThread extends AsyncTask<Void, Boolean, Void> {

   final static String        TAG = "Downloader_Android_WorkerThread";

   Downloader_Android         _downloader;
   Downloader_Android_Handler _handler;
   boolean                    _stopping;


   Downloader_Android_WorkerThread(Downloader_Android downloader) {
      _downloader = downloader;
      _handler = null;
      _stopping = false;
      // TODO: setPriority??
   }


   @Override
   protected Void doInBackground(Void... params) {
      Log.i(TAG, "doInBackground: worker=" + this.toString());
      boolean result = false;
      while (!isStopping()) {
         _handler = _downloader.getHandlerToRun();
         if (_handler != null) {
            result = _handler.runWithDownloader(_downloader);
            publishProgress(result);
         }
         else {
            // sleep for 25 milliseconds
            try {
//               Log.w(TAG, "runWithDownloader: worker=" + this.toString() + " waiting...");
               synchronized (this) {
                  wait(25);
               }
            }
            catch (InterruptedException e) {
               Log.e(TAG, "InterruptedException worker=" + this.toString());
               e.printStackTrace();
            }
         }
         if (isCancelled()) {
//            Log.i(TAG, "waiting to be cancelled  worker=" + this.toString());
            break;
         }
      }
      return null;
   }


   @Override
   protected void onProgressUpdate(Boolean... result) {
//      Log.i(TAG, "onProgressUpdate: worker=" + this.toString() + " result=" + result[0]);
      _handler.processResponse(result[0]);
   }


   public void stop() {
      synchronized (this) {
//         Log.e(TAG, "STOP  worker=" + this.toString());
         _stopping = true;
      }

      cancel(true);
   }


   private synchronized boolean isStopping() {
      return _stopping;
   }

}
