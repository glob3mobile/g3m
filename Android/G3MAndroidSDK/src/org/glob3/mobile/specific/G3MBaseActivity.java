

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDownloader;

import android.app.Activity;
import android.util.Log;


public abstract class G3MBaseActivity
         extends
            Activity {


   protected abstract G3MWidget_Android getWidgetAndroid();


   @Override
   protected void onResume() {
      Log.i(getClass().toString(), "Starting the downloader");
      IDownloader.instance().start();
      super.onResume();
   }


   @Override
   protected void onPause() {
      if (isFinishing()) {
         getWidgetAndroid().onDestroy();
      }
      else {
         getWidgetAndroid().onPause();
      }
      super.onPause();
   }


   @Override
   protected void onDestroy() {
      Log.i(getClass().toString(), "Killing process");
      android.os.Process.killProcess(android.os.Process.myPid());
      super.onDestroy();
   }

}
