

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class G3MSimplestGlob3Activity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final G3MBuilder glob3Builder = new G3MBuilder();
      _widgetAndroid = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      setContentView(_widgetAndroid);
   }


   @Override
   protected void onResume() {
      IDownloader.instance().start();
      super.onResume();

   }


   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.d("Demo", "Activity destroyed");
      //TODO HACK TO CLOSE SQL DB
      _widgetAndroid.closeStorage();
      android.os.Process.killProcess(android.os.Process.myPid());

   }
}
