

package org.glob3.mobile.demo;

import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;


public class G3MSimplestGlob3Activity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      _widgetAndroid = Glob3s.simpleBingGlob3(this);
      setContentView(_widgetAndroid);


   }

}
