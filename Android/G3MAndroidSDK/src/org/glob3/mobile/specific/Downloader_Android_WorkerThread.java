package org.glob3.mobile.specific;

import android.os.AsyncTask;

public class Downloader_Android_WorkerThread extends AsyncTask<Void, Void, Boolean> {

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
   protected Boolean doInBackground(Void... params) {
      boolean result = false;
      while (!isStopping()) {
         _handler = _downloader.getHandlerToRun();
         if (_handler != null) {
            result = _handler.runWithDownloader(_downloader);
         }
         else {
            // sleep for 25 milliseconds
            try {
               Thread.sleep(25);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
      return result;
   }


   @Override
   protected void onPostExecute(Boolean result) {
      _handler.processResponse(result);
   };


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
