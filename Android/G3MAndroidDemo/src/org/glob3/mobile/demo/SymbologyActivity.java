

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEOLineRasterSymbol;
import org.glob3.mobile.generated.GEOMarkSymbol;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class SymbologyActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;
   private GEORenderer       _vectorialRenderer;


   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_symbology);

      // N 43.7885865186124
      // W 7.36351850323685
      // S 43.6920077815877
      // E 7.48617349925817

      final float _VerticalExaggeration = 2f;
      final double DELTA_HEIGHT = 0;
      final LayerSet layerSet = new LayerSet();


      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 13);
      layerSet.addLayer(mboxTerrainLayer);


      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);


      builder.setBackgroundColor(Color.fromRGBA255(185, 221, 209, 255).muchDarker());


      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(43.69200778158779), //
               Angle.fromDegrees(7.36351850323685));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(43.7885865186124), //
               Angle.fromDegrees(7.48617349925817));

      final Sector demSector = new Sector(lower, upper);


      //NROWS          13
      //NCOLS          16
      final ElevationDataProvider dem = new SingleBilElevationDataProvider(new URL("file:///monaco-dem.bil", false), demSector,
               new Vector2I(16, 13), DELTA_HEIGHT);

      builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      builder.getPlanetRendererBuilder().setVerticalExaggeration(_VerticalExaggeration);


      _vectorialRenderer = builder.createGEORenderer(Symbolizer);
      _vectorialRenderer.loadJSON(new URL("file:///buildings_monaco.geojson"));
      _vectorialRenderer.loadJSON(new URL("file:///roads_monaco.geojson"));
      _vectorialRenderer.loadJSON(new URL("file:///restaurants_monaco.geojson"));


      //The sector is shrinked to adjust the projection of
      builder.setShownSector(demSector.shrinkedByPercent(0.1f));


      _g3mWidget = builder.createWidget();

      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);


   }


   @Override
   public void onBackPressed() {
      System.exit(0);
   }

   GEOSymbolizer Symbolizer = new GEOSymbolizer() {

                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
                                  final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
                                  symbols.add(new GEOPolygonRasterSymbol(geometry.getPolygonData(),
                                           Symbology.createPolygonLineRasterStyle(geometry),
                                           Symbology.createPolygonSurfaceRasterStyle(geometry)));

                                  return symbols;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
                                  // TODO Auto-generated method stub
                                  return null;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
                                  final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
                                  symbols.add(new GEOLineRasterSymbol(geometry.getCoordinates(),
                                           Symbology.createLineRasterStyle(geometry)));
                                  return symbols;
                               }


                               @Override
                               public ArrayList<GEOSymbol> createSymbols(final GEO2DPointGeometry geometry) {

                                  final ArrayList<GEOSymbol> result = new ArrayList<GEOSymbol>();

                                  final JSONObject properties = geometry.getFeature().getProperties();

                                  final String name = properties.getAsString("name", "");


                                  final MarkTouchListener markListener = new MarkTouchListener() {
                                     @Override
                                     public boolean touchedMark(final Mark mark) {
                                        runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                              new AlertDialog.Builder(SymbologyActivity.this) //
                                              .setTitle("Restaurant Name") //
                                              .setMessage(name) //
                                              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(final DialogInterface dialog,
                                                                     final int which) {
                                                    // continue with delete
                                                 }
                                              }) //
                                              .show();
                                           }
                                        });

                                        return true;
                                     }
                                  };

                                  final Mark mark = new Mark( //
                                           new URL("file:///restaurant-24@2x.png"), //
                                           new Geodetic3D(geometry.getPosition(), 0), //
                                           AltitudeMode.RELATIVE_TO_GROUND, //
                                           5000, //
                                           null, //
                                           false, // 
                                           markListener, //
                                           true);


                                  result.add(new GEOMarkSymbol(mark));
                                  return result;

                               }
                            };

}
