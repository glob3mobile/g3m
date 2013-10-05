

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class ScenarioTerrainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_scenario_terrain);

      final float _VerticalExaggeration = 6f;
      // final double DELTA_HEIGHT = -758.905;
      final double DELTA_HEIGHT = 0;


      final LayerSet layerset = SimpleRasterLayerBuilder.createLayerset();
      layerset.disableAllLayers();
      layerset.getLayerByTitle("Map Box Terrain").setEnable(true);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.setPlanet(Planet.createSphericalEarth());
      builder.getPlanetRendererBuilder().setLayerSet(layerset);


      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(40.15419), //
               Angle.fromDegrees(-5.5165));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(40.3423), //
               Angle.fromDegrees(-5.86648));

      final Sector demSector = new Sector(lower, upper);


      // NROWS          1335
      // NCOLS          2516
      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///0576.bil", false), demSector,
               new Vector2I(2516, 1335), DELTA_HEIGHT);

      builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      builder.getPlanetRendererBuilder().setVerticalExaggeration(_VerticalExaggeration);

      builder.setShownSector(demSector);


      _g3mWidget = builder.createWidget();
      //  _g3mWidget.setAnimatedCameraPosition(new Geodetic3D(demSector.getCenter(), 10000), TimeInterval.fromSeconds(5));
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.scenario_terrain, menu);
      return true;
   }

}
