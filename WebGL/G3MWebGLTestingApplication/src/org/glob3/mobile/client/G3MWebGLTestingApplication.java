

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEORasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbolizer;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TiledVectorLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
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

   private static final String    _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL        _g3mWidget         = null;

   private static G3MWidget_WebGL _widget            = null;

   private final boolean          _markersParsed     = false;
   private MarksRenderer          _markersRenderer;
   private final ShapesRenderer   _shapesRenderer    = new ShapesRenderer();


   @Override
   public void onModuleLoad() {
      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      _g3mWidget = createWidget();

      g3mWidgetHolder.add(_g3mWidget);

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));
   }


   private static G3MWidget_WebGL createWidget() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final String proxy = null;
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));

      final LayerSet ls = new LayerSet();

      final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?"),
               WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0,
                        18), TimeInterval.fromDays(30), true);

      ls.addLayer(blueMarble);

      builder.getPlanetRendererBuilder().setLayerSet(ls);

      _widget = builder.createWidget();

      exportJS();

      return _widget;
   }


   public static void setLatLonHeight(final double lat,
                                      final double lon,
                                      final double height) {
      //_widget.setCameraPosition(Geodetic3D.fromDegrees(lat, lon, height));

      _widget.getNextCamera().setGeodeticPosition(Geodetic3D.fromDegrees(lat, lon, height));
   }


   public static Geodetic3D getCameraData() {
      return _widget.getG3MWidget().getCurrentCamera().getGeodeticPosition();
   }


   private native static void exportJS() /*-{
		$wnd.setCameraLatLonHeight = function(lat, lon, height) {
			debugger;
			@org.glob3.mobile.client.G3MWebGLTestingApplication::setLatLonHeight(DDD)(lat, lon, height);
		}

		$wnd.getCameraData = function() {

			var g = @org.glob3.mobile.client.G3MWebGLTestingApplication::getCameraData()();
			var result = new Object();
			result.latitude = g.@org.glob3.mobile.generated.Geodetic3D::_latitude.@org.glob3.mobile.generated.Angle::_degrees;
			result.longitude = g.@org.glob3.mobile.generated.Geodetic3D::_longitude.@org.glob3.mobile.generated.Angle::_degrees;
			result.height = g.@org.glob3.mobile.generated.Geodetic3D::_height;

			return result;
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

      final GEORasterSymbolizer symbolizer = new GEORasterSymbolizer() {

         @Override
         public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
         }


         @Override
         public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
         }


         @Override
         public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
         }


         @Override
         public ArrayList<GEORasterSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
         }


         @Override
         public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPointGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
         }


         @Override
         public GEORasterSymbolizer copy() {
            // TODO Auto-generated method stub
            return null;
         }
      };

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


   private static MarksRenderer createMarksRenderer() {
      final MarksRenderer marksRenderer = new MarksRenderer(false);

      marksRenderer.setMarkTouchListener(new MarkTouchListener() {
         @Override
         public boolean touchedMark(final Mark touchedMark) {
            Window.alert("click on mark: " + touchedMark);
            return true;
         }
      }, true);


      final Mark mark = new Mark( //
               new URL("g3m-marker.png"), //
               Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 0), //
               AltitudeMode.ABSOLUTE, //
               0 // minDistanceToCamera
               );
      marksRenderer.addMark(mark);


      return marksRenderer;
   }


}
