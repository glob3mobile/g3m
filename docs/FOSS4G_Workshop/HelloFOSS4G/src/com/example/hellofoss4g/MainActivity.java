

package com.example.hellofoss4g;

import java.util.ArrayList;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GEO2DLineRasterStyle;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO2DSurfaceRasterStyle;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


public class MainActivity
extends
Activity {

   private GEORenderer _vectorialRenderer;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //The view where the globe is rendered
      final RelativeLayout glob3View = (RelativeLayout) findViewById(R.id.glob3);

      //The buider, here is where you can set and customize your map
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      //The layerset is used to add raster layers to the map
      final LayerSet layerSet = new LayerSet();

      //This is the mapquest layer definition.
      //there are several contructors to add new raster layers, see:
      //  https://github.com/glob3mobile/g3m/blob/purgatory/Android/G3MAndroidDemo/src/org/glob3/mobile/demo/SimpleRasterLayerBuilder.java
      final MapQuestLayer mqOSM = MapQuestLayer.newOSM(TimeInterval.fromDays(30));
      mqOSM.setEnable(true);
      mqOSM.setTitle("MapQuest OSM");


      //Add the layer to the layerset
      layerSet.addLayer(mqOSM);

      //Add the layerset to the builder
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      //This is the way to create a vectoria renderer to paint vector layers
      //We have to pass a symbolizer that is defined below this method
      _vectorialRenderer = builder.createGEORenderer(Symbolizer);
      //The layer is in assets's folder, could be in wherever
      _vectorialRenderer.loadJSON(new URL("file:///gz_2010_us_050_00_20m.json"));

      //we create the widget and we add the view
      glob3View.addView(builder.createWidget());


   }

   //The symbolizer, you only have to code the methods depending of your vectorial layer
   //in this case we have polygons, we need to code the method -->   createSymbols(final GEO2DPolygonGeometry geometry)
   GEOSymbolizer Symbolizer = new GEOSymbolizer() {

                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DPointGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {

                                  // We have to fill an array of symbols, depending of our daya
                                  final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
                                  //We use the 2 below methods to define the Symbology
                                  symbols.add(new GEOPolygonRasterSymbol(geometry.getPolygonData(),
                                           createPolygonLineRasterStyle(geometry), createPolygonSurfaceRasterStyle(geometry)));

                                  return symbols;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }

   };


   //We are definind the border of polygons in this case a black line.
   public static GEO2DLineRasterStyle createPolygonLineRasterStyle(@SuppressWarnings("unused")
   final GEOGeometry geometry) {

      final Color color = Color.fromRGBA(0, 0, 0, 0.5f);
      final float dashLengths[] = {};
      final int dashCount = 0;

      return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
   }


   // Here we tell to de rendere how to fill the polygons, in this case we do that depending of the counties's area.
   public static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(final GEOGeometry geometry) {

      final JSONObject properties = geometry.getFeature().getProperties();

      Log.d("County Area:", "" + properties.getAsNumber("CENSUSAREA").value());

      final double area = properties.getAsNumber("CENSUSAREA").value();

      int colorIndex = 0;
      if (area < 1000) {
         colorIndex = 2;
      }
      else if ((area > 1000) && (area < 2000)) {
         colorIndex = 4;
      }
      else if ((area > 2000) && (area < 3000)) {
         colorIndex = 6;
      }
      else if ((area > 3000) && (area < 4000)) {
         colorIndex = 8;
      }
      else if (area > 4000) {
         colorIndex = 10;
      }

      final Color color = Color.fromRGBA(0.7f, 0, 0, 0.5f).wheelStep(7, colorIndex);

      return new GEO2DSurfaceRasterStyle(color);
   }


   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }


   @Override
   public boolean onOptionsItemSelected(final MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      final int id = item.getItemId();
      if (id == R.id.action_settings) {
         return true;
      }
      return super.onOptionsItemSelected(item);
   }
}
