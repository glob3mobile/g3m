

package org.glob3.mobile.specific;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public abstract class G3MBaseActivity
         extends
            Activity {

   protected abstract void initializeWidget(G3MWidget_Android widget);

   private G3MWidget_Android _widgetAndroid = null;


   /** Called when the activity is first created. */
   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d("Demo", "Activity created");

      if (_widgetAndroid == null) { //Just the first time
         _widgetAndroid = new G3MWidget_Android(this);

         initializeWidget(_widgetAndroid);

         setContentView(_widgetAndroid);
      }
   }


   @Override
   protected void onStart() {
      super.onStart();
      Log.d("Demo", "Activity started");
   }


   @Override
   protected void onRestart() {
      super.onRestart();
      Log.d("Demo", "Activity restarted");

   }


   @Override
   protected void onResume() {
      super.onResume();
      Log.d("Demo", "Activity resumed");
      if (_widgetAndroid != null) {
         _widgetAndroid.onResume();
      }
   }


   @Override
   protected void onPause() {
      super.onPause();
      Log.d("Demo", "Activity paused");
      if (_widgetAndroid != null) {
         _widgetAndroid.onPause();
      }
   }


   @Override
   protected void onStop() {
      super.onStop();
      Log.d("Demo", "Activity stopped");
   }


   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.d("Demo", "Activity destroyed");
   }

}
