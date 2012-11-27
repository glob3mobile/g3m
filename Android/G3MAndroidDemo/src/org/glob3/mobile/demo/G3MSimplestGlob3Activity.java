

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.os.Bundle;


public class G3MSimplestGlob3Activity
         extends
            G3MBaseActivity {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);

      _widgetAndroid = g3mBuilder.createWidget();
      setContentView(_widgetAndroid);

      //      final G3MBuilder glob3Builder = new G3MBuilder();
      //      _widgetAndroid = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      //      setContentView(_widgetAndroid);
   }


   @Override
   protected G3MWidget_Android getWidgetAndroid() {
      return _widgetAndroid;
   }
}
