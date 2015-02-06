

package org.glob3.mobile.client;

import java.util.ArrayList;

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
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TiledVectorLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private final String         _g3mWidgetHolderId = "g3mWidgetHolder";

   private static G3MWidget_WebGL      _widget            = null;

   private final boolean        _markersParsed     = false;
   private MarksRenderer        _markersRenderer;
   private final ShapesRenderer _shapesRenderer    = new ShapesRenderer();


   private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
   }-*/;


   @Override
   public void onModuleLoad() {
      //initCustomizedWithBuilder();

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));
      
      _widget = builder.createWidget();
      g3mWidgetHolder.add(_widget);
      
      exportJS();
   }
   
   public static void setLatLonHeight(double lat, double lon, double height){
	    _widget.setCameraPosition(Geodetic3D.fromDegrees(lat, lon, height));
   }
   
   private native void exportJS() /*-{
		$wnd.setCameraLatLonHeight= function(lat, lon, height){
			@org.glob3.mobile.client.G3MWebGLTestingApplication::setLatLonHeight(DDD)(lat, lon, height);
		}
   }-*/;


   private LayerSet createLayerSet() {
	      final LayerSet layerSet = new LayerSet();
	      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

	     // layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));
	      
	      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));


	      final String urlTemplate = "http://192.168.1.2/ne_10m_admin_0_countries-Levels10/{level}/{y}/{x}.geojson";
	      final int firstLevel = 2;
	      final int maxLevel = 10;

	      final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();

	      final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
	               symbolizer, //
	               urlTemplate, //
	               Sector.fullSphere(), // sector
	               firstLevel, //
	               maxLevel, //
	               TimeInterval.fromDays(30), // timeToCache
	               true, // readExpired
	               1, // transparency
	               null // condition
	      );
	     // layerSet.addLayer(tiledVectorLayer);


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


      private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(final GEOGeometry geometry) {
         final JSONObject properties = geometry.getFeature().getProperties();
         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
         final Color color = FROM_COLOR.wheelStep(7, colorIndex);
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
