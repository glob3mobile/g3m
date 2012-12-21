

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.PeriodicalTask;

import android.app.Activity;
import android.os.Bundle;


public class G3MShowMarkersActivity
         extends
            Activity {


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);


      setContentView(R.layout.bar_glob3_template);

      final G3MBuilder glob3Builder = new G3MBuilder();

      //      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      initializeToolbar();

      final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();


      //
      //      final Geodetic3D position = new Geodetic3D(G3MGlob3Constants.SAN_FRANCISCO_POSITION, 5000000);
      //      _widgetAndroid = glob3Builder.getRenderersAnimationGlob3InitialPosition(getApplicationContext(), renderers,
      //               periodicalTasks, position);
      //      final LinearLayout layout = (LinearLayout) findViewById(R.id.glob3);
      //      layout.addView(_widgetAndroid);


   }


   private void initializeToolbar() {
      // TODO Auto-generated method stub

   }


}
