

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PlanetRenderer;
import org.glob3.mobile.generated.PlanetRendererBuilder;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.TileVisitorCache_Android;
import org.glob3.mobile.specific.TileVisitorListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;


public class G3MSimpleTileCacheExampleActivity
         extends
            Activity
         implements
            TileVisitorListener {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.bar_glob3_template);
      final G3MBuilder_Android builder = new G3MBuilder_Android(getApplicationContext());
      builder.setLogFPS(true);

      final Planet planet = Planet.createSphericalEarth();
      builder.setPlanet(planet);

      final PlanetRendererBuilder planetRendererBuilder = builder.getPlanetRendererBuilder();
      final LayerSet layerSet = new LayerSet();

      final WMSLayer osm = new WMSLayer( //
               "osm_auto:all", //
               new URL("http://129.206.228.72/cached/osm", false), //
               WMSServerVersion.WMS_1_1_0, //
               //Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
               Sector.fullSphere(), //
               "image/jpeg", //
               "EPSG:4326", //
               "", //
               false, //
               null, //
               TimeInterval.fromDays(30), //
               true);
      layerSet.addLayer(osm);

      planetRendererBuilder.setLayerSet(layerSet);
      planetRendererBuilder.setRenderDebug(false);


      final PlanetRenderer pr = planetRendererBuilder.create();
      builder.addRenderer(pr);
      // builder.setInitializationTask(getTileVisitorTask(pr));

      //Always after setting params
      _widgetAndroid = builder.createWidget();


      final TileVisitorCache_Android tvc = new TileVisitorCache_Android(_widgetAndroid.getG3MContext(),
               G3MSimpleTileCacheExampleActivity.this);
      // Are cached the first two levels of the world
      _widgetAndroid.getG3MWidget().getPlanetRenderer().acceptTileVisitor(tvc, Sector.fullSphere(), 0, 2);
      // Sector specified cached at the indicated levels

      _widgetAndroid.getG3MWidget().getPlanetRenderer().acceptTileVisitor(tvc,
               new Sector(new Geodetic2D(Angle.fromDegrees(39.31), Angle.fromDegrees(-6.72)),
                        new Geodetic2D(Angle.fromDegrees(39.38), Angle.fromDegrees(-6.64))),
               2, 14);


      _widgetAndroid.getG3MContext().getLogger().logInfo("Precaching has been completed");

      final LinearLayout g3mLayout = (LinearLayout) findViewById(R.id.glob3);
      g3mLayout.addView(_widgetAndroid);

   }


   @Override
   public void onTileDownloaded() {
      // TODO Auto-generated method stub

   }


   @Override
   public void onStartedProccess() {
      // TODO Auto-generated method stub

   }


   @Override
   public void onFinishedProcess() {
      // TODO Auto-generated method stub

   }


   @Override
   public void onPetition(final String url) {
      // TODO Auto-generated method stub

   }


   //   private GInitializationTask getTileVisitorTask(final PlanetRenderer pr) {
   //
   //      // PRECACHING
   //      final GInitializationTask initializationTask = new GInitializationTask() {
   //         @Override
   //         public void run(final G3MContext ctx) {
   //            final TileVisitorCache_Android tvc = new TileVisitorCache_Android(ctx);
   //            // Are cached the first two levels of the world
   //            pr.acceptTileVisitor(tvc, Sector.fullSphere(), 0, 2);
   //            // Sector specified cached at the indicated levels
   //
   //            pr.acceptTileVisitor(tvc, new Sector(new Geodetic2D(Angle.fromDegrees(39.31), Angle.fromDegrees(-6.72)),
   //                     new Geodetic2D(Angle.fromDegrees(39.38), Angle.fromDegrees(-6.64))), 2, 14);
   //
   //
   //            ctx.getLogger().logInfo("Precaching has been completed");
   //         }
   //
   //
   //         @Override
   //         public boolean isDone(final G3MContext context1) {
   //            return true;
   //         }
   //      };
   //
   //      return initializationTask;
   //   }
}
