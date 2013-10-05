

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class PlaneFlyingActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_plane_flying);

      final LayerSet layerset = SimpleRasterLayerBuilder.createLayerset();

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.setPlanet(Planet.createSphericalEarth());

      builder.getPlanetRendererBuilder().setLayerSet(layerset);

      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);


   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
