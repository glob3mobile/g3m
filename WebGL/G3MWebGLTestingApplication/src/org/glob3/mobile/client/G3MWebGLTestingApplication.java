

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.*;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;
import org.glob3.mobile.specific.ThreadUtils_WebGL;
import org.glob3.mobile.client.MyG3MWidget_WebGL;
import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeElevationDataProvider;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEO2DLineRasterStyle;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonData;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO2DSurfaceRasterStyle;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOJSONParser;
import org.glob3.mobile.generated.GEOLine2DMeshSymbol;
import org.glob3.mobile.generated.GEOLine2DStyle;
import org.glob3.mobile.generated.GEOObject;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbolizer;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapBoxLayer;
//import org.glob3.mobile.generated.GEORasterLineSymbol;
//import org.glob3.mobile.generated.GEORasterPolygonSymbol;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TiledVectorLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
//import org.glob3.mobile.generated.GEORasterLineSymbol;
//import org.glob3.mobile.generated.GEORasterPolygonSymbol;


public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private final String         _g3mWidgetHolderId = "g3mWidgetHolder";

   private G3MWidget_WebGL      _widget            = null;

   private final boolean        _markersParsed     = false;
   private MarksRenderer        _markersRenderer;
   private final ShapesRenderer _shapesRenderer    = new ShapesRenderer();


   private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
   }-*/;


   @Override
   public void onModuleLoad() {
	   /*
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

         @Override
         public void execute() {
            runUserPlugin();
         }

      });*/


      if (_widget == null) {

         // initialize a customized widget without using any builder
         // initWithoutBuilder();

         // initialize a default widget by using a builder
         //initDefaultWithBuilder();
    	  
    	  //testBranch_zrender_touchhandlers();
    	  //testBILGC();
    	  //testBandama();
    	  //testGeacron();
    	  testPTE05Layers();
    	  
         // initialize a customized widget by using a builder
         //initCustomizedWithBuilder();

         final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
         g3mWidgetHolder.add(_widget);
      }
   }

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      //      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOpenAerial(TimeInterval.fromDays(30)));
      //      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      /*
      final MeshRenderer meshRenderer = new MeshRenderer();
      meshRenderer.addMesh(createPointsMesh(builder.getPlanet()));
      builder.addRenderer(meshRenderer);*/

      //		if (true) {
      //			final Sector spain = Sector.fromDegrees(27.3174927, -18.5284423, 45.0299024, 5.4084426);
      //			builder.setShownSector(spain);
      //		}

      /*
      final boolean useMarkers = true;
      if (useMarkers) {
         // marks renderer
         final boolean readyWhenMarksReady = false;
         final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);

         marksRenderer.setMarkTouchListener(new MarkTouchListener() {
            @Override
            public boolean touchedMark(final Mark mark) {
               Window.alert("Touched on mark: " + mark.getLabel());
               return true;
            }
         }, true);

         final Mark m1 = new Mark("Label", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0), AltitudeMode.RELATIVE_TO_GROUND);
         // m1->addTouchListener(listener);
         marksRenderer.addMark(m1);

         final Mark m2 = new Mark( //
                  "Las Palmas", //
                  new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0), //
                  AltitudeMode.RELATIVE_TO_GROUND, 0, //
                  false);
         // m2->addTouchListener(listener);
         marksRenderer.addMark(m2);

         final Mark m3 = new Mark( //
                  "Washington, DC", //
                  new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
                           100), //
                  AltitudeMode.RELATIVE_TO_GROUND, 0);
         marksRenderer.addMark(m3);

         final boolean randomMarkers = false;
         if (randomMarkers) {
            for (int i = 0; i < 500; i++) {
               final Angle latitude = Angle.fromDegrees((Random.nextInt() % 180) - 90);
               final Angle longitude = Angle.fromDegrees((Random.nextInt() % 360) - 180);
               // NSLog(@"lat=%f, lon=%f", latitude.degrees(),
               // longitude.degrees());

               marksRenderer.addMark(new Mark(
               //
                        "Random", //
                        new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                        new Geodetic3D(latitude, longitude, 0), AltitudeMode.RELATIVE_TO_GROUND));
            }
         }
         builder.addRenderer(marksRenderer);
      }

      // builder.setInitializationTask(createMarkersInitializationTask());

      final String proxy = "";
      final Downloader_WebGL downloader = new Downloader_WebGL(8, 10, proxy);
      builder.setDownloader(downloader);

      // test bson parser and 3D model
      final ShapesRenderer shapeRenderer = new ShapesRenderer();
      builder.addRenderer(shapeRenderer);

      final QuadShape quad = new QuadShape(new Geodetic3D(Angle.fromDegrees(28.116956), Angle.fromDegrees(-15.440453), 2000), //
               AltitudeMode.RELATIVE_TO_GROUND, 10000, 10000, Color.red(), true);

      final QuadShape quad2 = new QuadShape(
               new Geodetic3D(Angle.fromDegrees(28), Angle.fromDegrees(-15.440453), 2000), //
               AltitudeMode.RELATIVE_TO_GROUND, new URL("http://serdis.dis.ulpgc.es/~a044526/android/glob3.png", false), 10000,
               10000, true);

      shapeRenderer.addShape(quad);
      shapeRenderer.addShape(quad2);

      quad.setPitch(Angle.fromDegrees(90));
      quad.setHeading(Angle.fromDegrees(0));
      quad.setAnimatedPosition( //
               TimeInterval.fromSeconds(60), //
               new Geodetic3D(Angle.fromDegrees(28.127222), Angle.fromDegrees(-15.431389), 10000), //
               Angle.fromDegrees(90), //
               Angle.fromDegrees(720), Angle.zero());

      quad2.setPitch(Angle.fromDegrees(90));
      quad2.setHeading(Angle.fromDegrees(0));
      quad2.setAnimatedPosition( //
               TimeInterval.fromSeconds(60), //
               new Geodetic3D(Angle.fromDegrees(28), Angle.fromDegrees(-15.431389), 10000), //
               Angle.fromDegrees(90), //
               Angle.fromDegrees(720), //
               Angle.zero());*/

 /*     final GEOSymbolizer defaultSymbolizer = new GEOSymbolizer() {
         @Override
         public ArrayList<GEOSymbol> createSymbols(final GEO2DPointGeometry geometry) {
            return new ArrayList<GEOSymbol>(0);
         }


         private GEO2DLineRasterStyle createLineRasterStyle(final GEOGeometry geometry) {
            final JSONObject properties = geometry.getFeature().getProperties();

            final String type = properties.getAsString("type", "");

            final float dashLengths[] = { 1, 12 };
            final int dashCount = 2;

            final Color color = type.equalsIgnoreCase("Water Indicator") //
                                                                        ? Color.fromRGBA(1, 1, 1, 0.9f) //
                                                                        : Color.fromRGBA(1, 1, 0, 0.9f);

            return new GEO2DLineRasterStyle( //
                     color, //
                     8, //
                     StrokeCap.CAP_ROUND, //
                     StrokeJoin.JOIN_ROUND, //
                     1, dashLengths, //
                     dashCount, //
                     0);
         }


         @Override
         public ArrayList<GEOSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
            symbols.add(new GEORasterLineSymbol(geometry.getCoordinates(), createLineRasterStyle(geometry)));
            return symbols;
         }


         @Override
         public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
            symbols.add(new GEOMultiLineRasterSymbol(geometry.getCoordinatesArray(), createLineRasterStyle(geometry)));
            return symbols;
         }


         private GEO2DLineRasterStyle createPolygonLineRasterStyle(final GEOGeometry geometry) {
            final JSONObject properties = geometry.getFeature().getProperties();

            final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);

            final Color color = Color.fromRGBA(0.7f, 0, 0, 0.5f).wheelStep(7, colorIndex).muchLighter().muchLighter();

            final float dashLengths[] = {};
            final int dashCount = 0;

            return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
         }


         private GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(final GEOGeometry geometry) {
            final JSONObject properties = geometry.getFeature().getProperties();

            final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);

            final Color color = Color.fromRGBA(0.7f, 0, 0, 0.5f).wheelStep(7, colorIndex);

            return new GEO2DSurfaceRasterStyle(color);
         }


         @Override
         public ArrayList<GEOSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>(0);

            symbols.add(new GEORasterPolygonSymbol(geometry.getPolygonData(), createPolygonLineRasterStyle(geometry),
                     createPolygonSurfaceRasterStyle(geometry)));

            return symbols;
         }


         @Override
         public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>(0);

            final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
            final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

            final ArrayList<GEO2DPolygonData> polygonsData = geometry.getPolygonsData();
            final int polygonsDataSize = polygonsData.size();

            for (int i = 0; i < polygonsDataSize; i++) {
               final GEO2DPolygonData polygonData = polygonsData.get(i);
               symbols.add(new GEORasterPolygonSymbol(polygonData, lineStyle, surfaceStyle));

            }

            return symbols;
         }
      };
      
      
      final GEOTileRasterizer geoTileRasterizer = new GEOTileRasterizer();

      final ShapesRenderer shapesRenderer = null;
      final MarksRenderer marksRenderer = null;
      final GEORenderer geoRenderer = new GEORenderer( //
               defaultSymbolizer, //
               meshRenderer, //
               shapesRenderer, //
               marksRenderer, //
               geoTileRasterizer);

      builder.addRenderer(geoRenderer);

      builder.getPlanetRendererBuilder().addTileRasterizer(geoTileRasterizer);
      */


      //      final GInitializationTask initializationTask = new GInitializationTask() {
      //
      //         private boolean done = false;
      //
      //
      //         @Override
      //         public void run(final G3MContext context) {
      //            // meshRenderer.addMesh(createPointsMesh(context.getPlanet()));
      //
      //            context.getDownloader().requestBuffer( //
      //                     new URL("http://glob3m.glob3mobile.com/test/aircraft-A320/A320.bson", false), //
      //                     0, //
      //                     TimeInterval.forever(), //
      //                     true, //
      //                     new IBufferDownloadListener() {
      //
      //                        @Override
      //                        public void onError(final URL url) {
      //                           ILogger.instance().logError("error downloading A320.bson");
      //                           done = true;
      //                        }
      //
      //
      //                        @Override
      //                        public void onDownload(final URL url,
      //                                               final IByteBuffer buffer,
      //                                               final boolean expired) {
      //                           final Shape aircraft = SceneJSShapesParser.parseFromBSON( //
      //                                    buffer, //
      //                                    "http://glob3m.glob3mobile.com/test/aircraft-A320/textures-A320/", //
      //                                    false, //
      //                                    new Geodetic3D( //
      //                                             Angle.fromDegreesMinutesSeconds(38, 53, 42.24), //
      //                                             Angle.fromDegreesMinutesSeconds(-77, 2, 10.92), //
      //                                             10000), // Washington, DC
      //                                    AltitudeMode.ABSOLUTE);
      //
      //                           if (aircraft != null) {
      //                              final double scale = 200;
      //                              aircraft.setScale(scale, scale, scale);
      //                              aircraft.setPitch(Angle.fromDegrees(90));
      //                              shapeRenderer.addShape(aircraft);
      //                           }
      //                           done = true;
      //                        }
      //
      //
      //                        @Override
      //                        public void onCanceledDownload(final URL url,
      //                                                       final IByteBuffer data,
      //                                                       final boolean expired) {
      //                           done = true;
      //                        }
      //
      //
      //                        @Override
      //                        public void onCancel(final URL url) {
      //                           done = true;
      //                        }
      //                     }, false);
      //
      //            final IBufferDownloadListener listener = new IBufferDownloadListener() {
      //
      //               @Override
      //               public void onDownload(final URL url,
      //                                      final IByteBuffer buffer,
      //                                      final boolean expired) {
      //                  final GEOObject geoObject = GEOJSONParser.parseJSON(buffer);
      //                  if (geoObject != null) {
      //                     geoRenderer.addGEOObject(geoObject);
      //                  }
      //               }
      //
      //
      //               @Override
      //               public void onError(final URL url) {
      //                  ILogger.instance().logError("Error downloading: " + url.description());
      //               }
      //
      //
      //               @Override
      //               public void onCancel(final URL url) {
      //                  ILogger.instance().logError("Canceled download: " + url.description());
      //               }
      //
      //
      //               @Override
      //               public void onCanceledDownload(final URL url,
      //                                              final IByteBuffer buffer,
      //                                              final boolean expired) {
      //                  // do nothing
      //               }
      //            };
      //
      //            // final URL geoJSONURL = new
      //            // URL("http://127.0.0.1:8888/countries-50m.geojson", false);
      //            final URL geoJSONURL = new URL("/countries-50m.geojson", false);
      //            // final URL geoJSONURL = new
      //            // URL("file:///boundary_lines_land.geojson", false);
      //            context.getDownloader().requestBuffer( //
      //                     geoJSONURL, //
      //                     DownloadPriority.HIGHER, //
      //                     TimeInterval.fromDays(30), //
      //                     true, //
      //                     listener, //
      //                     true);
      //
      //            if (true) {
      //               final URL planeFilePath = new URL("http://serdis.dis.ulpgc.es/~a044526/seymour-plane.json", false);
      //               final IBufferDownloadListener listenerPlane = new IBufferDownloadListener() {
      //
      //                  @Override
      //                  public void onDownload(final URL url,
      //                                         final IByteBuffer buffer,
      //                                         final boolean expired) {
      //
      //                     final Shape plane = SceneJSShapesParser.parseFromJSON( //
      //                              buffer, //
      //                              "http://serdis.dis.ulpgc.es/~a044526/", //
      //                              false, //
      //                              new Geodetic3D(Angle.fromDegrees(28.127222), Angle.fromDegrees(-15.431389), 10000), //
      //                              AltitudeMode.ABSOLUTE);
      //
      //                     final double scale = 1000;
      //                     plane.setScale(scale, scale, scale);
      //                     plane.setPitch(Angle.fromDegrees(90));
      //                     plane.setHeading(Angle.fromDegrees(0));
      //                     plane.setAnimatedPosition( //
      //                              TimeInterval.fromSeconds(60), //
      //                              new Geodetic3D(Angle.fromDegrees(28.127222), Angle.fromDegrees(-15.431389), 10000), //
      //                              Angle.fromDegrees(90), //
      //                              Angle.fromDegrees(720), //
      //                              Angle.zero());
      //
      //                     _shapesRenderer.addShape(plane);
      //                     ILogger.instance().logInfo("PLANE SHOWN");
      //                  }
      //
      //
      //                  @Override
      //                  public void onError(final URL url) {
      //                     ILogger.instance().logError("NO SEYMOUR");
      //                  }
      //
      //
      //                  @Override
      //                  public void onCancel(final URL url) {
      //                     // DO Nothing
      //                  }
      //
      //
      //                  @Override
      //                  public void onCanceledDownload(final URL url,
      //                                                 final IByteBuffer data,
      //                                                 final boolean expired) {
      //                     // Do Nothing
      //                  }
      //               };
      //
      //               downloader.requestBuffer(planeFilePath, 1000, TimeInterval.fromHours(1.0), true, listenerPlane, true);
      //            }
      //         }
      //
      //
      //         @Override
      //         public boolean isDone(final G3MContext context) {
      //            return done;
      //         }
      //      };
      //      builder.setInitializationTask(initializationTask);

      /*
      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));

      _widget = builder.createWidget();
      g3mWidgetHolder.add(_widget);*/

/*
   private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));


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
      layerSet.addLayer(tiledVectorLayer);


      return layerSet;
   }*/

   /*
   private static class SampleRasterSymbolizer
            extends
               GEORasterSymbolizer {

   public void initWithoutBuilder() {

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      g3mWidgetHolder.setWidth("640px");
      g3mWidgetHolder.setHeight("480px");

      _widget = new G3MWidget_WebGL();

      if (_widget.isSupported()) {

         final IStorage storage = null;

         final int delayMillis = 10;
         final String proxy = "";
         final IDownloader downloader = new Downloader_WebGL(8, delayMillis, proxy);

         final IThreadUtils threadUtils = new ThreadUtils_WebGL(delayMillis);

         final Planet planet = Planet.createEarth();

         final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
         cameraConstraints.add(new SimpleCameraConstrainer());

         final CameraRenderer cameraRenderer = new CameraRenderer();
         final boolean useInertia = true;
         cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
         final boolean allowRotationInDoubleDrag = false;
         cameraRenderer.addHandler(new CameraDoubleDragHandler(allowRotationInDoubleDrag));
         cameraRenderer.addHandler(new CameraRotationHandler());
         cameraRenderer.addHandler(new CameraDoubleTapHandler());

         final CompositeRenderer mainRenderer = new CompositeRenderer();

         final LayerSet layerSet = new LayerSet();

         final boolean testingTransparencies = true;
         if (testingTransparencies) {
            final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
                     WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false,
                     new LevelTileCondition(0, 6), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(
                              Sector.fullSphere(), 2, 4, 0, 6, LayerTilesRenderParameters.defaultTileTextureResolution(),
                              LayerTilesRenderParameters.defaultTileMeshResolution(), false));
            layerSet.addLayer(blueMarble);

            final WMSLayer i3Landsat = new WMSLayer("esat", new URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                     WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false,
                     new LevelTileCondition(7, 100), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(
                              Sector.fullSphere(), 2, 4, 0, 12, LayerTilesRenderParameters.defaultTileTextureResolution(),
                              LayerTilesRenderParameters.defaultTileMeshResolution(), false));
            layerSet.addLayer(i3Landsat);

            final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false),
                     WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null,
                     TimeInterval.fromDays(30), true, null, (float) 0.5);
            layerSet.addLayer(pnoa);
         }

         final boolean blueMarble = false;
         if (blueMarble) {
            final WMSLayer blueMarbleL = new WMSLayer( //
                     "bmng200405", //
                     new URL("http://www.nasa.network.com/wms?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     Sector.fullSphere(), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     // new LevelTileCondition(0, 6),
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(blueMarbleL);
         }

         final boolean useOrtoAyto = false;
         if (useOrtoAyto) {

            final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19,
                     new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), false);

            final WMSLayer ortoAyto = new WMSLayer(
            //
                     "orto_refundida", //
                     new URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     new Sector(new Geodetic2D(Angle.fromDegrees(39.350228), Angle.fromDegrees(-6.508713)), new Geodetic2D(
                              Angle.fromDegrees(39.536351), Angle.fromDegrees(-6.25946))), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     new LevelTileCondition(4, 19), //
                     TimeInterval.fromDays(30), //
                     true, //
                     ltrp);
            layerSet.addLayer(ortoAyto);
         }

         final boolean useBing = false;
         if (useBing) {
            final WMSLayer bing = new WMSLayer( //
                     "ve", //
                     new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     Sector.fullSphere(), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(bing);
         }
         final boolean useOSMLatLon = false;
         if (useOSMLatLon) {
            // final WMSLayer osm = new WMSLayer( //
            // "osm", //
            // new URL("http://wms.latlon.org/", false), //
            // WMSServerVersion.WMS_1_1_0, //
            // Sector.fromDegrees(-85.05, -180.0, 85.5, 180.0), //
            // "image/jpeg", //
            // "EPSG:4326", //
            // "", //
            // false, //
            // null);
            // layerSet.addLayer(osm);

            final WMSLayer osm = new WMSLayer( //
                     "osm_auto:all", //
                     new URL("http://129.206.228.72/cached/osm", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     // Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                     Sector.fullSphere(), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     // new LevelTileCondition(3, 100));
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(osm);
         }

         final boolean usePnoa = false;
         if (usePnoa) {
            final WMSLayer pnoa = new WMSLayer( //
                     "PNOA", //
                     new URL("http://www.idee.es/wms/PNOA/PNOA", false), //
                     WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     true, //
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(pnoa);
         }

         final boolean testURLescape = false;
         if (testURLescape) {
            final WMSLayer ayto = new WMSLayer(URL.escape("Ejes de via"), //
                     new URL("http://sig.caceres.es/wms_callejero.mapdef?", false), //
                     WMSServerVersion.WMS_1_1_0,//
                     Sector.fullSphere(), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     true, //
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(ayto);
         }

         final PlanetRendererBuilder tlBuilder = new PlanetRendererBuilder();
         tlBuilder.setLayerSet(layerSet);
         tlBuilder.setRenderDebug(true);
         final PlanetRenderer planetRenderer = tlBuilder.create();
         mainRenderer.addRenderer(planetRenderer);

         final boolean useQuadShapes = true;
         if (useQuadShapes) {
            final ShapesRenderer shapesRenderer = new ShapesRenderer();

            // final String textureFileName = "g3m-marker.png";
            // final IImage textureImage =
            // IFactory.instance().createImageFromFileName(textureFileName);
            //
            // final QuadShape quad = new QuadShape( //
            // new Geodetic3D(Angle.fromDegrees(37.78333333), //
            // Angle.fromDegrees(-122.41666666666667), //
            // 10000), //
            // textureImage, //
            // true, //
            // textureFileName, //
            // 500000, //
            // 500000);
            // quad.setHeading(Angle.fromDegrees(0));
            // quad.setPitch(Angle.fromDegrees(0));
            // shapesRenderer.addShape(quad);

            final Geodetic3D circlePosition = new Geodetic3D( //
                     Angle.fromDegrees(37.78333333), //
                     Angle.fromDegrees(-122.41666666666667), //
                     8000);

            // circle.setHeading(Angle.fromDegrees(45));
            // circle.setPitch(Angle.fromDegrees(45));
            // circle.setScale(2.0, 0.5, 1);
            // circle.setRadius(circleRadius);

            final Color circleColor = Color.newFromRGBA(1, 1, 0, 0.5f);
            final Shape circle = new CircleShape(circlePosition, AltitudeMode.RELATIVE_TO_GROUND, 50000, circleColor);
            shapesRenderer.addShape(circle);

            final Geodetic3D boxPosition = new Geodetic3D(Angle.fromDegrees(37.78333333), //
                     Angle.fromDegrees(-122.41666666666667), //
                     45000);
            final Vector3D size = new Vector3D(20000, 30000, 50000);
            final Color boxColor = Color.newFromRGBA(0, 1, 0, 0.5f);
            final Color edgeColor = Color.newFromRGBA(0.75f, 0, 0, 0.75f);
            final Shape box = new BoxShape(boxPosition, AltitudeMode.RELATIVE_TO_GROUND, size, 2, boxColor, edgeColor);
            shapesRenderer.addShape(box);

      private static final Color FROM_COLOR = Color.fromRGBA(0.7f, 0, 0, 0.5f);




            final boolean testingImagesCombine = false;
            if (testingImagesCombine) {
               class DL
                        extends
                           IImageDownloadListener {

                  @Override
                  public void onDownload(final URL url,
                                         final IImage image,
                                         final boolean expired) {

                     final int w = image.getWidth();
                     final int h = image.getHeight();

                     final java.util.ArrayList<IImage> images = new ArrayList<IImage>();
                     images.add(image);
                     images.add(image);

                     final java.util.ArrayList<RectangleF> srcRs = new ArrayList<RectangleF>();
                     srcRs.add(new RectangleF(0, 0, image.getWidth(), image.getHeight()));
                     srcRs.add(new RectangleF(10, 0, image.getWidth() - 10, image.getHeight()));

                     final java.util.ArrayList<RectangleF> destRs = new ArrayList<RectangleF>();
                     destRs.add(new RectangleF(0, 0, 256, 256));
                     destRs.add(new RectangleF(50, 20, 256, 70));

                     final java.util.ArrayList<Float> transparencies = new ArrayList<Float>();
                     transparencies.add((float) 1.0);
                     transparencies.add((float) 0.5);

                     class QuadListener
                              extends
                                 IImageListener {
                        ShapesRenderer _sr;


                        public QuadListener(final ShapesRenderer sr) {
                           _sr = sr;
                        }


                        @Override
                        public void imageCreated(final IImage image2) {
                           final Shape quadImages = new QuadShape(new Geodetic3D(Angle.fromDegrees(28.410728),
                                    Angle.fromDegrees(-16.339417), 8000), AltitudeMode.RELATIVE_TO_GROUND, image2, 50000, 50000,
                                    true);

                           _sr.addShape(quadImages);
                        }
                     }

                     IImageUtils.combine(new Vector2I(256, 256), images, srcRs, destRs, transparencies, new QuadListener(
                              shapesRenderer), true);

                  }


                  @Override
                  public void onError(final URL url) {
                  }


                  @Override
                  public void onCancel(final URL url) {
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IImage image,
                                                 final boolean expired) {
                  }

               }

               downloader.requestImage( //
                        new URL(
                                 "http://www.nasa.network.com/wms?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&WIDTH=256&HEIGHT=256&BBOX=-45.0,-90.0,0.0,-45.0&LAYERS=bmng200405&FORMAT=image/jpeg&SRS=EPSG:4326&STYLES=&TRANSPARENT=FALSE",
                                 false), //
                        100000, //
                        TimeInterval.fromDays(1), //
                        true, //
                        new DL(), //
                        true);

            }

         }

         final BusyMeshRenderer busyRenderer = new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));

         final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

         final boolean logFPS = false;

         final boolean logDownloaderStatistics = false;

         final GInitializationTask initializationTask = null;

         final boolean autoDeleteInitializationTask = true;

         final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

         final WidgetUserData userData = null;

         final SceneLighting lighting = new FixedFocusSceneLighting();

         final ICameraActivityListener cameraActivityListener = null;

         final InitialCameraPositionProvider initialCameraPositionProvider = new SimpleInitialCameraPositionProvider();


         //            public static final G3MWidget create(final GL gl,
         //                                                 final IStorage storage,
         //                                                 final IDownloader downloader,
         //                                                 final IThreadUtils threadUtils,
         //                                                 final ICameraActivityListener cameraActivityListener,
         //                                                 final Planet planet,
         //                                                 final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
         //                                                 final CameraRenderer cameraRenderer,
         //                                                 final Renderer mainRenderer,
         //                                                 final Renderer busyRenderer,
         //                                                 final ErrorRenderer errorRenderer,
         //                                                 final Color backgroundColor,
         //                                                 final boolean logFPS,
         //                                                 final boolean logDownloaderStatistics,
         //                                                 final GInitializationTask initializationTask,
         //                                                 final boolean autoDeleteInitializationTask,
         //                                                 final java.util.ArrayList<PeriodicalTask> periodicalTasks,
         //                                                 final GPUProgramManager gpuProgramManager,
         //                                                 final SceneLighting sceneLighting,
         //                                                 final InitialCameraPositionProvider initialCameraPositionProvider);


         final ErrorRenderer errorRenderer = new HUDErrorRenderer();
         final Renderer hudRenderer = null;
         _widget.initWidget(//
                  storage, //
                  downloader, //
                  threadUtils, //
                  cameraActivityListener, //
                  planet, //
                  cameraConstraints, //
                  cameraRenderer, //
                  mainRenderer, //
                  busyRenderer, //
                  errorRenderer, //
                  hudRenderer, //
                  backgroundColor, //
                  logFPS, //
                  logDownloaderStatistics, //
                  initializationTask, //
                  autoDeleteInitializationTask, //
                  periodicalTasks, //
                  userData, //
                  lighting, //
                  initialCameraPositionProvider, null);

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



      //final LayerSet layerSet = new LayerSet();

      /*
       * final boolean blueMarble = false; if (blueMarble) { final WMSLayer
       * blueMarbleL = new WMSLayer( // "bmng200405", // new
       * URL("http://www.nasa.network.com/wms?", false), //
       * WMSServerVersion.WMS_1_1_0, // Sector.fullSphere(), // "image/jpeg",
       * // "EPSG:4326", // "", // false, // //new LevelTileCondition(0, 6),
       * null, // TimeInterval.fromDays(30), // true);
       * layerSet.addLayer(blueMarbleL);
       * blueMarbleL.addTerrainTouchEventListener(new
       * TerrainTouchEventListener() {
       * 
       * @Override public boolean onTerrainTouch(G3MEventContext context,
       * TerrainTouchEvent ev) { Window.alert("touching terrain blueMarble");
       * return false; }
       * 
       * @Override public void dispose() {} }); }
       */

  /*    final boolean useOrtoAyto = false;
      if (useOrtoAyto) {

         final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, new Vector2I(
                  256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), false);

         final WMSLayer ortoAyto = new WMSLayer( //
                  "orto_refundida", //
                  new URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false), //
                  WMSServerVersion.WMS_1_1_0, //
                  new Sector( //
                           new Geodetic2D(Angle.fromDegrees(39.350228), Angle.fromDegrees(-6.508713)), //
                           new Geodetic2D(Angle.fromDegrees(39.536351), Angle.fromDegrees(-6.25946))), //
                  "image/jpeg", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  new LevelTileCondition(4, 19), //
                  TimeInterval.fromDays(30), //
                  true, //
                  ltrp);
         layerSet.addLayer(ortoAyto);

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



      final boolean testingTransparencies = false;
      if (testingTransparencies) {
         final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
                  WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(
                           0, 6), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0,
                           6, LayerTilesRenderParameters.defaultTileTextureResolution(),
                           LayerTilesRenderParameters.defaultTileMeshResolution(), false));
         layerSet.addLayer(blueMarble);

         final WMSLayer i3Landsat = new WMSLayer("esat", new URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                  WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(
                           7, 100), TimeInterval.fromDays(30), true, new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0,
                           12, LayerTilesRenderParameters.defaultTileTextureResolution(),
                           LayerTilesRenderParameters.defaultTileMeshResolution(), false));
         layerSet.addLayer(i3Landsat);

         final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false),
                  WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null,
                  TimeInterval.fromDays(30), true, null, (float) 0.5);
         layerSet.addLayer(pnoa);
      }*/

      //builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      /*
       * final WMSLayer political = new WMSLayer("topp:cia", new
       * URL("http://worldwind22.arc.nasa.gov/geoserver/wms?", false),
       * WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png",
       * "EPSG:4326", "countryboundaries", true, null,
       * TimeInterval.fromDays(30), true); layerSet.addLayer(political);
       */

   /*
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
*/
   
	public CameraRenderer createCameraRenderer()
	{
	  CameraRenderer cameraRenderer = new CameraRenderer();
	  final boolean useInertia = true;
	  cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
	  final boolean allowRotationInDoubleDrag = false;
	  cameraRenderer.addHandler(new CameraDoubleDragHandler(allowRotationInDoubleDrag));
	  //cameraRenderer.addHandler(new CameraZoomAndRotateHandler());

	  cameraRenderer.addHandler(new CameraRotationHandler());
	  cameraRenderer.addHandler(new CameraDoubleTapHandler());
	  
	  return cameraRenderer;
	}
	
	   public void testBILGC() {
		   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		   //const Planet* planet = Planet::createEarth();
		   //const Planet* planet = Planet::createSphericalEarth();
		   final Planet planet = Planet.createFlatEarth();
		   builder.setPlanet(planet);
		   
		   // wms layer
			final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, 
					new Vector2I(256, 256), 
					new Vector2I(16,16), false);
		   LayerSet	layerSet = new LayerSet();
		   WMSLayer grafcanLIDAR = new WMSLayer("LIDAR_MTL",
				   new URL("http://idecan1.grafcan.es/ServicioWMS/MTL?", false),
				   WMSServerVersion.WMS_1_1_0,
				   Sector.fullSphere(),//gcSector,
				   "image/jpeg",
				   "EPSG:4326",
				   "",
				   false,
				   new LevelTileCondition(0, 17),
				   TimeInterval.fromDays(30),
				   true,
				   ltrp);
		   layerSet.addLayer(grafcanLIDAR);
		   builder.getPlanetRendererBuilder().setLayerSet(layerSet);
		   	   
		   // create elevations for GC
		   Sector sector = Sector.fromDegrees(27.7116484957735, -15.90589160041418, 28.225913322423995, -15.32910937385168 );
		   Vector2I extent = new Vector2I(1000, 1000);                             // bil resolution
		   URL url = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/Fiware/gc2.bil", false);
		   ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
		   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
		   builder.getPlanetRendererBuilder().setVerticalExaggeration(1.0f);

		   _widget = builder.createWidget();
		   
		   // set camera looking at Tenerife
		   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(28.070), Angle.fromDegrees(-15.478), 9291.1);
		   _widget.setCameraPosition(position);
		   _widget.setCameraHeading(Angle.fromDegrees(-35.65));
		   _widget.setCameraPitch(Angle.fromDegrees(-64.75));
	  }

	   public void testPTE05Layers() {
		   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		   //const Planet* planet = Planet::createEarth();
		   //const Planet* planet = Planet::createSphericalEarth();
		   final Planet planet = Planet.createFlatEarth();
		   builder.setPlanet(planet);
		   
		   // wms layer
			final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, 
					new Vector2I(256, 256), 
					new Vector2I(16,16), false);
		   LayerSet	layerSet = new LayerSet();
		   WMSLayer grafcanOrto = new WMSLayer("WMS_OrtoExpress",
	               new URL("http://idecan1.grafcan.es/ServicioWMS/OrtoExpress?", false),
	               WMSServerVersion.WMS_1_1_0,
	               Sector.fullSphere(),
	               "image/jpeg",
	               "EPSG:4326",
	               "",
	               false,
	               new LevelTileCondition(0, 19),
	               TimeInterval.fromDays(30),
	               true,
	               ltrp);
		   layerSet.addLayer(grafcanOrto);
		   
		   WMSLayer PTE05_03APM = new WMSLayer("O3_APM",
	               new URL("http://ide2.idegrancanaria.es/wms/PTE_05?", false),
	               WMSServerVersion.WMS_1_1_0,
	               Sector.fullSphere(),
	               "image/png",
	               "EPSG:4326",
	               "",
	               true,
	               new LevelTileCondition(0, 19),
	               TimeInterval.fromDays(30),
	               true,
	               ltrp);
		   layerSet.addLayer(PTE05_03APM);

		   WMSLayer PTE05_CATASE = new WMSLayer("CATASE",
	               new URL("http://ide2.idegrancanaria.es/wms/CAT_ASE?", false),
	               WMSServerVersion.WMS_1_1_0,
	               Sector.fullSphere(),
	               "image/png",
	               "EPSG:4326",
	               "",
	               true,
	               new LevelTileCondition(0, 19),
	               TimeInterval.fromDays(30),
	               true,
	               ltrp,
	               0.5f);
		   layerSet.addLayer(PTE05_CATASE);

		   builder.getPlanetRendererBuilder().setLayerSet(layerSet);
		   	   
		   // create elevations for GC
		   Sector sector = Sector.fromDegrees(27.7116484957735, -15.90589160041418, 28.225913322423995, -15.32910937385168 );
		   Vector2I extent = new Vector2I(1000, 1000);                             // bil resolution
		   URL url = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/Fiware/gc2.bil", false);
		   ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
		   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
		   builder.getPlanetRendererBuilder().setVerticalExaggeration(1.0f);

		   _widget = builder.createWidget();
		   
		   // set camera looking at Gran Canaria
		   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.976), Angle.fromDegrees(-15.591), 50000);
		   _widget.setCameraPosition(position);
		   //_widget.setCameraHeading(Angle.fromDegrees(-35.65));
		   //_widget.setCameraPitch(Angle.fromDegrees(-64.75));
	  }

	   public void testBandama() {
		   final MyG3MWidget_WebGL widgetJS = new MyG3MWidget_WebGL();
		   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL(widgetJS);

		   final Planet planet = Planet.createFlatEarth();
		   builder.setPlanet(planet);

		   // wms layer
		   final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, 
				   new Vector2I(256, 256), 
				   new Vector2I(16,16), false);
		   LayerSet	layerSet = new LayerSet();
		  
		   /* WMSLayer grafcanLIDAR = new WMSLayer("LIDAR_MTL",
				   new URL("http://idecan1.grafcan.es/ServicioWMS/MTL?", false),
				   WMSServerVersion.WMS_1_1_0,
				   Sector.fullSphere(),//gcSector,
				   "image/jpeg",
				   "EPSG:4326",
				   "",
				   false,
				   new LevelTileCondition(0, 17),
				   TimeInterval.fromDays(30),
				   true,
				   ltrp);
		   layerSet.addLayer(grafcanLIDAR);*/

		   WMSLayer grafcanOrto = new WMSLayer("WMS_OrtoExpress",
	               new URL("http://idecan1.grafcan.es/ServicioWMS/OrtoExpress?", false),
	               WMSServerVersion.WMS_1_1_0,
	               Sector.fullSphere(),
	               "image/jpeg",
	               "EPSG:4326",
	               "",
	               false,
	               new LevelTileCondition(0, 19),
	               TimeInterval.fromDays(30),
	               true,
	               ltrp);
		   layerSet.addLayer(grafcanOrto);
		   builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		   
		   // create elevations for GC
		   Sector sectorGC = Sector.fromDegrees(27.7116484957735, -15.90589160041418, 28.225913322423995, -15.32910937385168 );
		   Vector2I extentGC = new Vector2I(1000, 1000);                             // bil resolution
		   URL urlGC = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/SenderosGC/resources/gc.bil", false);
		   ElevationDataProvider elevationDataProviderGC = new SingleBilElevationDataProvider(urlGC, sectorGC, extentGC);

		   // create elevation for Bandama (5m/pixel)
		   Sector bandamaBilSector = Sector.fromDegrees(28.0186134922002,-15.466485021954,28.0501903939333,-15.4475303331328);
		   Vector2I extentBandama = new Vector2I(371, 702); 
		   URL urlBandama = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/SenderosGC/resources/mdt1_bandama.bil",false);
		   ElevationDataProvider elevationDataProviderBandama = new SingleBilElevationDataProvider(urlBandama, 
				   bandamaBilSector, extentBandama);
		   
		   /*// create elevation for Bandama (1m/pixel)
		   Sector bandamaBilSector = Sector.fromDegrees(28.0186134922002,-15.466485021954,28.0501903939333,-15.4475303331328);
		   Vector2I extentBandama = new Vector2I(1881, 3522); 
		   // this url format is for executing in local
		   //URL urlBandama = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/SenderosGC/resources/mdt.bil",false);
		   // this url format is for executing in web
		   URL urlBandama = new URL("mdt.bil", false);
		   ElevationDataProvider elevationDataProviderBandama = new SingleBilElevationDataProvider(urlBandama, 
				   bandamaBilSector, extentBandama);*/

		   // create composite elevation provider
		   CompositeElevationDataProvider elevationDataProvider = new CompositeElevationDataProvider();
		   elevationDataProvider.addElevationDataProvider(elevationDataProviderBandama);
		   elevationDataProvider.addElevationDataProvider(elevationDataProviderGC);
		   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
		   builder.getPlanetRendererBuilder().setVerticalExaggeration(1.0f);

		   _widget = builder.createWidget();
		   
		   // increase LOD detail in Bandama
		   widgetJS.setSectorForLODAugmented(bandamaBilSector);
		   widgetJS.setLODAugmentedFactor(3.0);

		   // set camera looking at GranCanaria
		   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(28.0196), Angle.fromDegrees(-15.4589), 603.9);
		   _widget.setCameraPosition(position);
		   _widget.setCameraHeading(Angle.fromDegrees(-15.40));
		   _widget.setCameraPitch(Angle.fromDegrees(-12.75));
	   }

	   
	   // ******************************
	   // GEACRON **********************
	   // ******************************
	   
		private static class SampleRasterSymbolizer extends GEORasterSymbolizer {

			private static final Color FROM_COLOR = Color
					.fromRGBA(0.7f, 0, 0, 0.5f);

			private static GEO2DLineRasterStyle createPolygonLineRasterStyle(
					final GEOGeometry geometry) {
				final JSONObject properties = geometry.getFeature().getProperties();
				final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
				final Color color = FROM_COLOR.wheelStep(7, colorIndex)
						.muchLighter().muchLighter();
				final float dashLengths[] = {};
				final int dashCount = 0;
				return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND,
						StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
			}

			private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(
					final GEOGeometry geometry) {
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
			public ArrayList<GEORasterSymbol> createSymbols(
					final GEO2DPointGeometry geometry) {
				return null;
			}

			@Override
			public ArrayList<GEORasterSymbol> createSymbols(
					final GEO2DLineStringGeometry geometry) {
				return null;
			}

			@Override
			public ArrayList<GEORasterSymbol> createSymbols(
					final GEO2DMultiLineStringGeometry geometry) {
				return null;
			}

			@Override
			public ArrayList<GEORasterSymbol> createSymbols(
					final GEO2DPolygonGeometry geometry) {
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
			public ArrayList<GEORasterSymbol> createSymbols(
					final GEO2DMultiPolygonGeometry geometry) {
				final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();

				final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
				final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

				for (final GEO2DPolygonData polygonData : geometry
						.getPolygonsData()) {
					symbols.add(new GEOPolygonRasterSymbol(polygonData, lineStyle,
							surfaceStyle));
				}

				return symbols;
			}
		}

	   public void testGeacron() {
		   final MyG3MWidget_WebGL widgetJS = new MyG3MWidget_WebGL();
		   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL(widgetJS);
		   
		   //const Planet* planet = Planet::createEarth();
		   //final Planet planet = Planet.createSphericalEarth();
		   final Planet planet = Planet.createFlatEarth();
		   builder.setPlanet(planet);


		   {
			   final LayerSet layerSet = new LayerSet();
			   // layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
			   layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval
					   .fromDays(30)));
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
			   layerSet.addLayer(tiledVectorLayer);
			   //builder.getPlanetRendererBuilder().setLayerSet(layerSet);
		   }

			final String proxy = null; // "http://galileo.glob3mobile.com/" +
										// "proxy.php?url="
			/*builder.setDownloader(new Downloader_WebGL( //
					8, // maxConcurrentOperationCount
					10, // delayMillis
					proxy));*/
			
			//For A1800DC
			GeaCronSymbolizer symbolizer = new GeaCronSymbolizer();
			MeshRenderer mr1 = new MeshRenderer();
			final GEORenderer renderer1800 = new GEORenderer(symbolizer, mr1, null, null,null);
			builder.addRenderer(renderer1800);
			builder.addRenderer(mr1);
			
			//For 1900
			MeshRenderer mr2 = new MeshRenderer();
			final GEORenderer renderer1900 = new GEORenderer(symbolizer, mr2, null, null,null);
			builder.addRenderer(renderer1900);
			builder.addRenderer(mr2);
			
			class GeaCronBDL extends IBufferDownloadListener {
				
				GEORenderer _renderer1800;
				GEORenderer _renderer1900;
				
				public GeaCronBDL(GEORenderer renderer1800, GEORenderer renderer1900){
					_renderer1800 = renderer1800;
					_renderer1900 = renderer1900;
				}

				@Override
				public void onDownload(URL url, IByteBuffer buffer,
						boolean expired) {
					String s = buffer.getAsString();
					if (url.getPath().contains("1800")){
						GEOObject geoObject = GEOJSONParser.parseJSON(s);
						_renderer1800.addGEOObject(geoObject);
					}
					if (url.getPath().contains("1900")){
						GEOObject geoObject = GEOJSONParser.parseJSON(s);
						_renderer1900.addGEOObject(geoObject);
					}
				}

				@Override
				public void onError(URL url) {
					//Window.alert("E");
				}

				@Override
				public void onCancel(URL url) {
					//Window.alert("C");
				}

				@Override
				public void onCanceledDownload(URL url, IByteBuffer buffer,
						boolean expired) {
					//Window.alert("CD");
				}
				
			};
			
			
			builder.setInitializationTask(new GInitializationTask() {
				
				@Override
				public void run(G3MContext context) {
					GeaCronBDL listener = new GeaCronBDL(renderer1800, renderer1900);
					context.getDownloader().requestBuffer(new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/Geacron/A1800DC.json"), 100000000, TimeInterval.forever(), true, listener, false);
					context.getDownloader().requestBuffer(new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/Geacron/A1900DC.json"), 100000000, TimeInterval.forever(), true, listener, false);
				}
				
				@Override
				public boolean isDone(G3MContext context) {
					return true;
				}
			});
			
			class GeaCronTask extends GTask {
				Renderer _r1, _r2;
				public GeaCronTask(Renderer r1, Renderer r2) {
					_r1 = r1;
					_r2 = r2;
				}

				@Override
				public void run(G3MContext context) {
					boolean x = _r1.isEnable();
					_r1.setEnable(!x);
					_r2.setEnable(x);
				}
			};

			builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromMilliseconds(1000), new GeaCronTask(mr1, mr2)));

		   _widget = builder.createWidget();
	   }

	   
		class GeaCronSymbolizer extends GEOSymbolizer {

			@Override
			public ArrayList<GEOSymbol> createSymbols(GEO2DPointGeometry geometry) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ArrayList<GEOSymbol> createSymbols(
					GEO2DLineStringGeometry geometry) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ArrayList<GEOSymbol> createSymbols(
					GEO2DMultiLineStringGeometry geometry) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ArrayList<GEOSymbol> createSymbols(GEO2DPolygonGeometry geometry) {

				JSONObject properties = geometry.getFeature().getProperties();

				
				String color = properties.getAsString("FILL_COLOR", "");
				int c1 = color.indexOf(",");
				int c2 = color.indexOf(",", c1+1);
				
				String rs = color.substring(color.indexOf("(") + 1, c1);
				String gs = color.substring(c1 + 1, c2);
				String bs = color.substring(c2 + 1, color.length()-1);
				
				int r = Integer.parseInt(rs);
				int g = Integer.parseInt(gs);
				int b = Integer.parseInt(bs);
				

				Color lineColor = Color.fromRGBA((float) (r / 255.0),
						(float) (g / 255.0), (float) (b / 255.0), (float) 1.0);

				GEOLine2DMeshSymbol line = new GEOLine2DMeshSymbol(
						geometry.getCoordinates(),
						new GEOLine2DStyle(lineColor, 3), 1.0);

				ArrayList<GEOSymbol> v = new ArrayList<GEOSymbol>();
				v.add(line);
				return v;
			}

			@Override
			public ArrayList<GEOSymbol> createSymbols(
					GEO2DMultiPolygonGeometry geometry) {
				// TODO Auto-generated method stub
				return null;
			}

		};

    public void testBranch_zrender_touchhandlers() {
	   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

	   //const Planet* planet = Planet::createEarth();
	   final Planet planet = Planet.createSphericalEarth();
	   //final Planet planet = Planet.createFlatEarth();
	   builder.setPlanet(planet);
	   	   
	   LayerSet	layerSet = new LayerSet();
		  
	   /* WMSLayer grafcanLIDAR = new WMSLayer("LIDAR_MTL",
			   new URL("http://idecan1.grafcan.es/ServicioWMS/MTL?", false),
			   WMSServerVersion.WMS_1_1_0,
			   Sector.fullSphere(),//gcSector,
			   "image/jpeg",
			   "EPSG:4326",
			   "",
			   false,
			   new LevelTileCondition(0, 17),
			   TimeInterval.fromDays(30),
			   true,
			   ltrp);
	   layerSet.addLayer(grafcanLIDAR);*/

	   WMSLayer grafcanOrto = new WMSLayer("WMS_OrtoExpress",
               new URL("http://idecan1.grafcan.es/ServicioWMS/OrtoExpress?", false),
               WMSServerVersion.WMS_1_1_0,
               Sector.fullSphere(),
               "image/jpeg",
               "EPSG:4326",
               "",
               false,
               new LevelTileCondition(0, 19),
               TimeInterval.fromDays(30),
               true);
	   layerSet.addLayer(grafcanOrto);
	   builder.getPlanetRendererBuilder().setLayerSet(layerSet);

	   // create shape
	   ShapesRenderer shapesRenderer = new ShapesRenderer();
	   Shape box = new BoxShape(new Geodetic3D(Angle.fromDegrees(28.4),
			   Angle.fromDegrees(-16.4),
			   0),
			   AltitudeMode.ABSOLUTE,
			   new Vector3D(3000, 3000, 20000),
			   2,
			   Color.fromRGBA(1.0f, 1.0f, 0.0f, 0.5f),
			   Color.newFromRGBA(0.0f, 0.75f, 0.0f, 0.75f));
	   shapesRenderer.addShape(box);
	   builder.addRenderer(shapesRenderer);

	   // create elevations for Tenerife from bil file
	   Sector sector = Sector.fromDegrees (27.967811065876,                  // min latitude
			   -17.0232177085356,                // min longitude
			   28.6103464294992,                 // max latitude
			   -16.0019401695656);               // max longitude
	   Vector2I extent = new Vector2I(256, 256);                             // image resolution
	   URL url = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Tenerife-256x256.bil", false);
	   ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
	   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
	   builder.getPlanetRendererBuilder().setVerticalExaggeration(4.0f);

	   _widget = builder.createWidget();
	   
	   // set camera looking at Tenerife
	   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
	   _widget.setCameraPosition(position);
	   _widget.setCameraPitch(Angle.fromDegrees(-50.0));
  }

}
