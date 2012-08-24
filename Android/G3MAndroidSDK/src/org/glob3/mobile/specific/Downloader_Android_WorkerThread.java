package org.glob3.mobile.specific;

import android.os.AsyncTask;

public class Downloader_Android_WorkerThread extends AsyncTask<Void, Void, Void> {

	Downloader_Android _downloader = null;
   boolean     _stopping;


   Downloader_Android_WorkerThread(Downloader_Android downloader) {
      _downloader = downloader;
      _stopping = false;
      // TODO: setPriority??
   }


   @Override
   protected Void doInBackground(Void... params) {
      while (!isStopping()) {
         Downloader_Android_Handler handler = _downloader.getHandlerToRun();
         if (handler != null) {
            handler.runWithDownloader(_downloader);
         }
         else {
            // sleep for 25 milliseconds
            try {
               Thread.sleep(25);
            }
            catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
      return null;
   }


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
