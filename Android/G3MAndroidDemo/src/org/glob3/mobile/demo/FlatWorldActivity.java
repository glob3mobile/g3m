

package org.glob3.mobile.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class FlatWorldActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_flat_world);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      builder.setPlanet(Planet.createFlatEarth());

      final LayerSet layerSet = new LayerSet();
      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 2);
      layerSet.addLayer(mboxTerrainLayer);
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      builder.setBackgroundColor(Color.fromRGBA255(185, 221, 209, 255).muchDarker());

      final ElevationDataProvider dem = new SingleBilElevationDataProvider(new URL("file:///full-earth-2048x1024.bil", false),
               Sector.fullSphere(), new Vector2I(2048, 1024), 0);
      builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      builder.getPlanetRendererBuilder().setVerticalExaggeration(3f);


      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

}
