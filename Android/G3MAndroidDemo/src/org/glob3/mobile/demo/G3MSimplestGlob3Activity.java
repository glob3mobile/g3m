

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
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.SingleBillElevationDataProvider;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
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
      final boolean processRotation = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
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

      final LayerTilesRenderParameters params = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 1, 19,
               LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(),
               false);
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

      final TileRendererBuilder tlBuilder = new TileRendererBuilder();

      final ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider( //
               new URL("file:///full-earth-2048x1024.bil", false), //
               Sector.fullSphere(), //
               new Vector2I(2048, 1024) //
      );
      tlBuilder.setElevationDataProvider(elevationDataProvider);

      tlBuilder.setVerticalExaggeration(20);


      tlBuilder.setLayerSet(layerSet);
      tlBuilder.setRenderDebug(false);
      final TileRenderer tileRenderer = tlBuilder.create();
      mainRenderer.addRenderer(tileRenderer);


      final ShapesRenderer shapesRenderer = new ShapesRenderer();
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
            /*
            final URL url = new URL("ws://192.168.0.103:8888/tube/scene/2g59wh610g6c1kmkt0l", false);
            final IWebSocketListener listener = new IWebSocketListener() {
               @Override
               public void onOpen(final IWebSocket ws) {
                  ILogger.instance().logError(ws + " opened!");
               }


               @Override
               public void onMesssage(final IWebSocket ws,
                                      final String message) {
                  ILogger.instance().logError(ws + " message \"" + message + "\"");
               }


               @Override
               public void onError(final IWebSocket ws,
                                   final String error) {
                  ILogger.instance().logError(ws + " error \"" + error + "\"");
               }


               @Override
               public void onClose(final IWebSocket ws) {
                  ILogger.instance().logError(ws + " closed!");
               }
            };
            context.getFactory().createWebSocket(url, listener, true, true);
            */
         }


         @Override
         public final boolean isDone(final G3MContext context) {
            return true;
         }
      };

      final boolean autoDeleteInitializationTask = true;

      final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

      final WidgetUserData userData = null;

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
               userData);

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
