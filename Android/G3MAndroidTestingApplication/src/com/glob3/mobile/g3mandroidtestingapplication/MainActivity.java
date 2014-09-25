

package com.glob3.mobile.g3mandroidtestingapplication;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;


   //   private RelativeLayout    _placeHolder;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);

      _g3mWidget = createWidget();

      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      placeHolder.addView(_g3mWidget);
   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      builder.getPlanetRendererBuilder().setForceFirstLevelTilesRenderOnStart(false);
      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);

      return builder.createWidget();
   }


   //   private static class SampleRasterSymbolizer
   //            extends
   //               GEORasterSymbolizer {
   //
   //      private static final Color FROM_COLOR = Color.fromRGBA(0.7f, 0, 0, 0.5f);
   //
   //
   //      private static GEO2DLineRasterStyle createPolygonLineRasterStyle(final GEOGeometry geometry) {
   //         final JSONObject properties = geometry.getFeature().getProperties();
   //         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
   //         final Color color = FROM_COLOR.wheelStep(7, colorIndex).muchLighter().muchLighter();
   //         final float dashLengths[] = {};
   //         final int dashCount = 0;
   //         return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
   //      }
   //
   //
   //      private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(final GEOGeometry geometry) {
   //         final JSONObject properties = geometry.getFeature().getProperties();
   //         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
   //         final Color color = FROM_COLOR.wheelStep(7, colorIndex);
   //         return new GEO2DSurfaceRasterStyle(color);
   //      }
   //
   //
   //      @Override
   //      public GEORasterSymbolizer copy() {
   //         return new SampleRasterSymbolizer();
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPointGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
   //         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
   //         final GEOPolygonRasterSymbol symbol = new GEOPolygonRasterSymbol( //
   //                  geometry.getPolygonData(), //
   //                  createPolygonLineRasterStyle(geometry), //
   //                  createPolygonSurfaceRasterStyle(geometry) //
   //         );
   //         symbols.add(symbol);
   //         return symbols;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
   //         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
   //
   //         final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
   //         final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);
   //
   //         for (final GEO2DPolygonData polygonData : geometry.getPolygonsData()) {
   //            symbols.add(new GEOPolygonRasterSymbol(polygonData, lineStyle, surfaceStyle));
   //         }
   //
   //         return symbols;
   //      }
   //   }


   private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      //layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));


      //final String urlTemplate = "http://111.111.1.1";

      //final int firstLevel = 2;
      //final int maxLevel = 10;

      //final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();

      final WMSLayer layer = new WMSLayer("bmng200405", new URL("http://www.nix.de", false), WMSServerVersion.WMS_1_1_0,
               Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 18),
               TimeInterval.fromDays(30), true);

      //      final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
      //               symbolizer, //
      //               urlTemplate, //
      //               Sector.fullSphere(), // sector
      //               firstLevel, //
      //               maxLevel, //
      //               TimeInterval.fromDays(30), // timeToCache
      //               true, // readExpired
      //               1, // transparency
      //               null, // condition
      //               "" // disclaimerInfo
      //      );
      layerSet.addLayer(layer);


      return layerSet;
   }


   //   @Override
   //   public boolean onCreateOptionsMenu(final Menu menu) {
   //      // Inflate the menu; this adds items to the action bar if it is present.
   //      getMenuInflater().inflate(R.menu.main, menu);
   //      return true;
   //   }

}
