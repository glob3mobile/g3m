

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      // builder.getPlanetRendererBuilder().setRenderDebug(true);


      // if (false) { // Testing lights
      // shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0),
      // AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      // 1000000, 1000000, 1000000), (float) 1.0, Color.red(), Color.black(),
      // true)); // With normals
      //
      // shapesRenderer.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180,
      // 0), AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(
      // 1000000, 1000000, 1000000), (float) 1.0, Color.blue(), Color.black(),
      // true)); // With normals
      //
      // }


      //      final boolean testingTransparencies = false;
      //
      //      if (testingTransparencies) {
      //         final LayerSet layerSet = new LayerSet();
      //
      //         final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
      //                  WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(
      //                           0, 6), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0,
      //                           6, LayerTilesRenderParameters.defaultTileTextureResolution(),
      //                           LayerTilesRenderParameters.defaultTileMeshResolution(), false));
      //         layerSet.addLayer(blueMarble);
      //
      //         final WMSLayer i3Landsat = new WMSLayer("esat", new URL("http://data.worldwind.arc.nasa.gov/wms?", false),
      //                  WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(
      //                           7, 100), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0,
      //                           12, LayerTilesRenderParameters.defaultTileTextureResolution(),
      //                           LayerTilesRenderParameters.defaultTileMeshResolution(), false));
      //         layerSet.addLayer(i3Landsat);
      //
      //         final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false),
      //                  WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null,
      //                  TimeInterval.fromDays(30), true, null, (float) 0.5);
      //         layerSet.addLayer(pnoa);
      //
      //         builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      //      }

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      //      marksRenderer.addMark(new Mark("HIGH MARK", Geodetic3D.fromDegrees(0, 0, 100000), AltitudeMode.RELATIVE_TO_GROUND));
      //      marksRenderer.addMark(new Mark("LOW MARK", Geodetic3D.fromDegrees(0, 0, 100), AltitudeMode.RELATIVE_TO_GROUND));

      _g3mWidget = builder.createWidget();
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);

   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}
