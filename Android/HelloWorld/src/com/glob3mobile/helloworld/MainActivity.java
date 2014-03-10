

package com.glob3mobile.helloworld;

import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;


public class MainActivity
         extends
            Activity {

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final G3MWidget_Android g3mWidget = builder.createWidget();

      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      layout.addView(g3mWidget);

   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
