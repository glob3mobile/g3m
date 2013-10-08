

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class ShapeSymbolizerActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;
   private GEORenderer       _vectorialRenderer;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_shape_symbolizer);

      //xM’n,yM’n -174.2,17.7504 : xM‡x,yM‡x -64.75,71.2906

      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(17.7504), //
               Angle.fromDegrees(-174.2));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(71.2906), //
               Angle.fromDegrees(-64.75));
      final Sector demSector = new Sector(lower, upper);


      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      builder.setPlanet(Planet.createFlatEarth());

      final LayerSet layerSet = new LayerSet();
      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 2);
      layerSet.addLayer(mboxTerrainLayer);
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      builder.setBackgroundColor(Color.fromRGBA255(185, 221, 209, 255).muchDarker());

      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///full-earth-2048x1024.bil", false),
               demSector, new Vector2I(2048, 1024), 0);

      _vectorialRenderer = builder.createGEORenderer(Symbology.USCitiesSymbolizer);
      _vectorialRenderer.loadJSON(new URL("file:///uscities.geojson"));
      builder.addRenderer(_vectorialRenderer);


      builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      builder.getPlanetRendererBuilder().setVerticalExaggeration(3f);
      builder.setShownSector(demSector);

      _g3mWidget = builder.createWidget();

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(39.12787093899339d, -77.59965772558118d, 500000),
               TimeInterval.fromSeconds(8));


      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);


   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }
}
