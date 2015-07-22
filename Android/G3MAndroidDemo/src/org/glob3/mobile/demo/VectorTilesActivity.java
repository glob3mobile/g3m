

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GEO2DLineRasterStyle;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonData;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO2DSurfaceRasterStyle;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbolizer;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Info;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TiledVectorLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;


public class VectorTilesActivity
extends
Activity {


   private G3MWidget_Android _g3mWidget;
   private static int        _colorIndex = 50;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_vector_tiles);

      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      _g3mWidget = builder.createWidget();


      //   _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(47.39987642274313d, 8.544022519285223d, 1300),
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(55.9531d, -3.1889d, 1300), TimeInterval.fromSeconds(5));
      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      placeHolder.addView(_g3mWidget);

      final Button changeSymbologyButton = (Button) findViewById(R.id.changeSymbologyButton);
      changeSymbologyButton.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(final View v) {
            _colorIndex = _colorIndex + 10;

         }
      });


      ILogger.instance().logError("processors:" + Runtime.getRuntime().availableProcessors());
   }


   private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));


      //      layerSet.addLayer(new BingMapsLayer(BingMapType.AerialWithLabels(),
      //               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", TimeInterval.fromDays(30)));

      layerSet.addLayer(new MapBoxLayer("examples.map-cnkhv76j", TimeInterval.fromDays(30), true, 2));

      //  final String urlTemplate = "http://192.168.1.15/vectorial/swiss-buildings/{level}/{x}/{y}.geojson";
      //final String urlTemplate = "http://glob3mobile.dyndns.org/vectorial/swiss-buildings-bson-new/{level}/{x}/{y}.bson";

      final String urlTemplate = "http://192.168.1.12:8000/vectorial/scotland_buildings/GEOJSON/{level}/{x}/{y}.geojson";

      final int firstLevel = 2;
      final int maxLevel = 17;


      // SWITZERLAND
      //      final Geodetic2D lower = new Geodetic2D( //
      //               Angle.fromDegrees(45.8176852), //
      //               Angle.fromDegrees(5.956216));
      //      final Geodetic2D upper = new Geodetic2D( //
      //               Angle.fromDegrees(47.803029), //
      //               Angle.fromDegrees(10.492264));
      //


      //SCOTLAND
      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(54.7226296), //
               Angle.fromDegrees(-7.6536084));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(60.855646), //
               Angle.fromDegrees(-0.7279944));

      final Sector sector = new Sector(lower, upper);

      final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();


      final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
               symbolizer, //
               urlTemplate, //
               sector, // sector
               firstLevel, //
               maxLevel, //
               TimeInterval.fromDays(30), // timeToCache
               true, // readExpired
               1, // transparency
               new LevelTileCondition(10, 21), // condition
               new ArrayList<Info>() // disclaimerInfo
               );
      layerSet.addLayer(tiledVectorLayer);


      return layerSet;
   }


   private static class SampleRasterSymbolizer
   extends
   GEORasterSymbolizer {

      private static final Color FROM_COLOR = Color.fromRGBA(0.7f, 0, 0, 0.5f);


      private static GEO2DLineRasterStyle createPolygonLineRasterStyle(final GEOGeometry geometry) {
         final JSONObject properties = geometry.getFeature().getProperties();
         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
         final Color color = FROM_COLOR.wheelStep(7, colorIndex).muchLighter().muchLighter();
         final float dashLengths[] = {};
         final int dashCount = 0;
         return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
      }


      private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(@SuppressWarnings("unused")
      final GEOGeometry geometry) {
         //          final JSONObject properties = geometry.getFeature().getProperties();
         //         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
         final Color color = FROM_COLOR.wheelStep(7, _colorIndex);
         return new GEO2DSurfaceRasterStyle(color);
      }


      @Override
      public GEORasterSymbolizer copy() {
         return new SampleRasterSymbolizer();
      }


      @Override
      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPointGeometry geometry) {
         return null;
      }


      @Override
      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
         return null;
      }


      @Override
      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
         return null;
      }


      @Override
      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
         final GEOPolygonRasterSymbol symbol = new GEOPolygonRasterSymbol( //
                  geometry.getPolygonData(), //
                  createPolygonLineRasterStyle(geometry), //
                  createPolygonSurfaceRasterStyle(geometry) //
                  );
         symbols.add(symbol);
         return symbols;
      }


      @Override
      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();

         final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
         final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

         for (final GEO2DPolygonData polygonData : geometry.getPolygonsData()) {
            symbols.add(new GEOPolygonRasterSymbol(polygonData, lineStyle, surfaceStyle));
         }

         return symbols;
      }
   }

}
