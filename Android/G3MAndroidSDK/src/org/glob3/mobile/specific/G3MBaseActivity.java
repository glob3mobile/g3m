

package org.glob3.mobile.specific;

import android.app.Activity;


public abstract class G3MBaseActivity
         extends
            Activity {


   protected abstract G3MWidget_Android getWidgetAndroid();


   @Override
   final protected void onResume() {
      super.onResume();
      getWidgetAndroid().onResume();
   }


   @Override
   final protected void onPause() {
      if (isFinishing()) {
         getWidgetAndroid().onDestroy();
      }
      else {
         getWidgetAndroid().onPause();
      }
      super.onPause();
   }


   @Override
   final protected void onDestroy() {
      super.onDestroy();
   }

}
