/**
 * 
 */


package org.glob3.mobile.demo;

import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


/**
 * @author mdelacalle
 * 
 */
public class G3MDoubleGlob3Activity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroidUp   = null;
   private G3MWidget_Android _widgetAndroidDown = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      //Don't forget to assign the layout and inflate it
      setContentView(R.layout.double_glob3_activity);


      final G3MBuilder glob3Builder = new G3MBuilder();

      //This is the way to put any globe in any layout.
      _widgetAndroidUp = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      final FrameLayout layoutUp = (FrameLayout) findViewById(R.id.glob3up);
      layoutUp.addView(_widgetAndroidUp);

      _widgetAndroidDown = glob3Builder.getSimpleOSMGlob3(getApplicationContext());
      final FrameLayout layoutDown = (FrameLayout) findViewById(R.id.glob3down);
      layoutDown.addView(_widgetAndroidDown);


   }


   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.d("Demo", "Activity destroyed");
      //TODO HACK TO CLOSE SQL DB
      _widgetAndroidUp.closeStorage();
      _widgetAndroidDown.closeStorage();
   }

}
