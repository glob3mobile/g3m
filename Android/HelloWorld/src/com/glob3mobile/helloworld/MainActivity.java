

package com.glob3mobile.helloworld;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
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

      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(43.532822), //
               Angle.fromDegrees(1.350360));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(43.668522), //
               Angle.fromDegrees(1.515350));

      final Sector demSector = new Sector(lower, upper);

      final LayerSet layerSet = new LayerSet();
      final WMSLayer franceRaster4000K = new WMSLayer("Raster4000k", new URL("http://www.geosignal.org/cgi-bin/wmsmap?", false),
               WMSServerVersion.WMS_1_1_0, demSector, "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 18),
               TimeInterval.fromDays(30), true);

      layerSet.addLayer(franceRaster4000K);
      //     builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      //      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 11);
      //      layerSet.addLayer(mboxTerrainLayer);


      final G3MWidget_Android g3mWidget = builder.createWidget();

      //  g3mWidget.setCameraPosition(new Geodetic3D(demSector.getCenter(), 10000));

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
