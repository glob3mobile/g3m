package org.glob3.mobile.specific;

import android.os.AsyncTask;
import android.util.Log;

public class Downloader_Android_WorkerThread extends AsyncTask<Void, Boolean, Void> {

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
   //   protected Boolean doInBackground(Void... params) {
   protected Void doInBackground(Void... params) {
      Log.e("Worker", "doInBackground " + this.toString());
      boolean result = false;
      while (!isStopping()) {
         _handler = _downloader.getHandlerToRun();
         if (_handler != null) {
            Log.e("Worker", "runWithDownloader " + this.toString());
            result = _handler.runWithDownloader(_downloader);
            Log.e("Worker", "runWithDownloader " + this.toString() + " result=" + result);
            publishProgress(result);
         }
         else {
            // sleep for 25 milliseconds
            try {
               Log.e("Worker", this.toString() + " waiting...");
               synchronized (this) {
                  wait(25);
               }
//               Thread.sleep(25);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
      //      return result;
      return null;
   }


   @Override
   protected void onProgressUpdate(Boolean... result) {
      Log.e("Worker", "onProgressUpdate " + this.toString() + " " + result[0]);
      _handler.processResponse(result[0]);
   }


   //   @Override
   //   protected void onPostExecute(Boolean result) {
   //      Log.e("Worker", "onPostExecute"  + this.toString());
   //      _handler.processResponse(result);
   //   };


   public void stop() {
      synchronized (this) {
         _stopping = true;
      }

      cancel(true);
   }


   private synchronized boolean isStopping() {
      return _stopping;
   }

}
