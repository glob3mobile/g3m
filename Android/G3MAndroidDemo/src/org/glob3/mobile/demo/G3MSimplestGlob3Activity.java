

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DefaultSceneLighting;
import org.glob3.mobile.generated.DownloadPriority;
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
import org.glob3.mobile.generated.GEOMultiLineRasterSymbol;
import org.glob3.mobile.generated.GEOObject;
import org.glob3.mobile.generated.GEORasterLineSymbol;
import org.glob3.mobile.generated.GEORasterPolygonSymbol;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.GEOTileRasterizer;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PlanetRendererBuilder;
import org.glob3.mobile.generated.SceneLighting;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.Downloader_Android;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.SQLiteStorage_Android;
import org.glob3.mobile.specific.ThreadUtils_Android;

import android.os.Bundle;


public class G3MSimplestGlob3Activity

         extends
            G3MBaseActivity {

   private G3MWidget_Android _widgetAndroid = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // initialize a default widget by using a builder
      //      final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);
      //
      //      _widgetAndroid = g3mBuilder.createWidget();
      //      setContentView(_widgetAndroid);


      // initialize a customized widget without using any builder
      _widgetAndroid = new G3MWidget_Android(this);

      final IStorage storage = new SQLiteStorage_Android("g3m.cache", this);

      final TimeInterval connectTimeout = TimeInterval.fromSeconds(20);
      final TimeInterval readTimeout = TimeInterval.fromSeconds(30);
      final boolean saveInBackground = true;
      final IDownloader downloader = new CachedDownloader( //
               new Downloader_Android(8, connectTimeout, readTimeout, this), //
               storage, //
               saveInBackground);

      final IThreadUtils threadUtils = new ThreadUtils_Android(_widgetAndroid);
      final ICameraActivityListener cameraActivityListener = null;

      //      final Planet planet = Planet.createEarth();
      final Planet planet = Planet.createSphericalEarth();

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      cameraConstraints.add(new SimpleCameraConstrainer());

      final CameraRenderer cameraRenderer = new CameraRenderer();
      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
      cameraRenderer.addHandler(new CameraDoubleDragHandler());
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final CompositeRenderer mainRenderer = new CompositeRenderer();
      final LayerSet layerSet = new LayerSet();
      //      final WMSLayer osm = new WMSLayer( //
      //               "osm_auto:all", //
      //               new URL("http://129.206.228.72/cached/osm", false), //
      //               WMSServerVersion.WMS_1_1_0, //
      //               //Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
      //               Sector.fullSphere(), //
      //               "image/jpeg", //
      //               "EPSG:4326", //
      //               "", //
      //               false, //
      //               null, //
      //               TimeInterval.fromDays(30), //
      //               true);
      //      layerSet.addLayer(osm);

      //      final LayerTilesRenderParameters params = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 1, 19,
      //               LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(),
      //               false);
      //final Sector bbox = new Sector(new Geodetic2D(Angle.fromDegrees(-6.858), Angle.fromDegrees(39.182)), new Geodetic2D(
      //         Angle.fromDegrees(-6.089), Angle.fromDegrees(39.657)));


      //      final WMSLayer aytoLayer = new WMSLayer( //
      //               "sigaytocc:AytoCC", //
      //               new URL("http://195.57.27.86:8080/geoserver/gwc/service/wms?", false), //
      //               WMSServerVersion.WMS_1_1_0, //
      //               //Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
      //               Sector.fullSphere(), //
      //               "image/jpeg", //
      //               "EPSG:4326", //
      //               "", //
      //               false, //
      //               null, //
      //               TimeInterval.fromDays(30), //
      //               true, params);
      //
      //      layerSet.addLayer(aytoLayer);


      //      final WMSLayer blueMarbleL = new WMSLayer( //
      //               "bmng200405", //
      //               new URL("http://www.nasa.network.com/wms?", false), //
      //               WMSServerVersion.WMS_1_1_0, //
      //               Sector.fullSphere(), //
      //               "image/jpeg", //
      //               "EPSG:4326", //
      //               "", //
      //               false, //
      //               //new LevelTileCondition(0, 6),
      //               null, //
      //               TimeInterval.fromDays(30), //
      //               new LayerTilesRenderParameters( //
      //                        Sector.fullSphere(), //
      //                        2, //
      //                        4, //
      //                        0, //
      //                        8, //
      //                        LayerTilesRenderParameters.defaultTileTextureResolution(), //
      //                        LayerTilesRenderParameters.defaultTileMeshResolution(), //
      //                        false));
      //      layerSet.addLayer(blueMarbleL);

      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));


      final MeshRenderer meshRenderer = null;
      final MarksRenderer marksRenderer = null;
      final GEOTileRasterizer geoTileRasterizer = new GEOTileRasterizer();


      final ShapesRenderer shapesRenderer = new ShapesRenderer();


      final GEOSymbolizer defaultSymbolizer = new GEOSymbolizer() {
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


      final GEORenderer geoRenderer = new GEORenderer( //
               defaultSymbolizer, //
               meshRenderer, //
               shapesRenderer, //
               marksRenderer, //
               geoTileRasterizer);


      final PlanetRendererBuilder planetRendererBuilder = new PlanetRendererBuilder();


      final ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider( //
               new URL("file:///full-earth-2048x1024.bil", false), //
               Sector.fullSphere(), //
               new Vector2I(2048, 1024) //
      );
      planetRendererBuilder.setElevationDataProvider(elevationDataProvider);

      planetRendererBuilder.setVerticalExaggeration(20);


      planetRendererBuilder.setLayerSet(layerSet);
      planetRendererBuilder.setRenderDebug(false);
      planetRendererBuilder.setTileRasterizer(geoTileRasterizer);


      mainRenderer.addRenderer(planetRendererBuilder.create());

      mainRenderer.addRenderer(geoRenderer);
      mainRenderer.addRenderer(shapesRenderer);

      //      final boolean testingImagesCombine = true;
      //      if (testingImagesCombine) {
      //         final Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.play);
      //         final Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
      //         final Image_Android image1 = new Image_Android(b1, null);
      //         final Image_Android image2 = new Image_Android(b2, null);
      //
      //         final java.util.ArrayList<IImage> images = new ArrayList<IImage>();
      //         images.add(image2);
      //         images.add(image1);
      //
      //         final java.util.ArrayList<RectangleF> srcRs = new ArrayList<RectangleF>();
      //         srcRs.add(new RectangleF(0, 0, 640, 960));
      //         srcRs.add(new RectangleF(0, 0, 48, 48));
      //
      //         final java.util.ArrayList<RectangleF> destRs = new ArrayList<RectangleF>();
      //         destRs.add(new RectangleF(0, 0, 256, 256));
      //         destRs.add(new RectangleF(0, 128, 130, 130));
      //
      //         class QuadListener
      //                  extends
      //                     IImageListener {
      //            ShapesRenderer _sr;
      //
      //
      //            public QuadListener(final ShapesRenderer sr) {
      //               _sr = sr;
      //
      //            }
      //
      //
      //            @Override
      //            public void imageCreated(final IImage image) {
      //               final Shape quadImages = new QuadShape(new Geodetic3D(Angle.fromDegrees(28.410728), Angle.fromDegrees(-16.339417),
      //                        8000), image, 49000, 38000);
      //
      //               _sr.addShape(quadImages);
      //            }
      //         }
      //
      //
      //         IImageUtils.combine(new Vector2I(256, 256), images, srcRs, destRs, new QuadListener(shapesRenderer), true);
      //      }


      //      final MarksRenderer marksRenderer = new MarksRenderer(false);
      //      final Mark m1 = new Mark("Fuerteventura", //
      //               new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
      //               new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0), //
      //               false);
      //      marksRenderer.addMark(m1);
      //
      //      final Mark m3 = new Mark(
      //               "Washington, DC", //
      //               new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92), 100), //
      //               0);
      //      marksRenderer.addMark(m3);
      //      mainRenderer.addRenderer(marksRenderer);


      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));

      final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

      final boolean logFPS = true;

      final boolean logDownloaderStatistics = false;

      // final GInitializationTask initializationTask = null;


      final GInitializationTask initializationTask = new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {
            final IBufferDownloadListener listener = new IBufferDownloadListener() {

               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {
                  final GEOObject geoObject = GEOJSONParser.parse(buffer);
                  if (geoObject != null) {
                     geoRenderer.addGEOObject(geoObject);
                  }
               }


               @Override
               public void onError(final URL url) {
                  ILogger.instance().logError("Error downloading: " + url.description());
               }


               @Override
               public void onCancel(final URL url) {
                  ILogger.instance().logError("Canceled download: " + url.description());
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer buffer,
                                              final boolean expired) {
                  // do nothing
               }
            };

            final URL geoJSONURL = new URL("file:///countries-50m.geojson", false);
            // final URL geoJSONURL = new URL("file:///boundary_lines_land.geojson", false);
            context.getDownloader().requestBuffer( //
                     geoJSONURL, //
                     DownloadPriority.HIGHER, //
                     TimeInterval.fromDays(30), //
                     true, //
                     listener, //
                     true);
         }


         @Override
         public final boolean isDone(final G3MContext context) {
            return true;
         }
      };

      final boolean autoDeleteInitializationTask = true;

      final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

      final WidgetUserData userData = null;

      final SceneLighting lighting = new DefaultSceneLighting();

      _widgetAndroid.initWidget(//
               storage, // 
               downloader, //
               threadUtils, //
               cameraActivityListener,//
               planet, //
               cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               backgroundColor, //
               logFPS, //
               logDownloaderStatistics, //
               initializationTask, //
               autoDeleteInitializationTask, //
               periodicalTasks, //
               userData, lighting);

      setContentView(_widgetAndroid);


      //      final G3MBuilder glob3Builder = new G3MBuilder();
      //      _widgetAndroid = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      //      setContentView(_widgetAndroid);
   }


   @Override
   protected G3MWidget_Android getWidgetAndroid() {
      return _widgetAndroid;
   }
}
