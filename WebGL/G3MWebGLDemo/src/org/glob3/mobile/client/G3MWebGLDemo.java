

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;
import org.glob3.mobile.specific.NativeGL_WebGL;
import org.glob3.mobile.specific.ThreadUtils_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLDemo
         implements
            EntryPoint {

   private final String    _g3mWidgetHolderId = "g3mWidgetHolder";


   private G3MWidget_WebGL _widget            = null;


   @Override
   public void onModuleLoad() {
      if (_widget == null) {

         // initialize a customized widget without using any builder
         //         initWithoutBuilder();

         // initialize a default widget by using a builder
         initDefaultWithBuilder();

         // initialize a customized widget by using a builder
         //         initCustomizedWithBuilder();

         final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
         g3mWidgetHolder.add(_widget);
      }
   }


   public void initDefaultWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      _widget = builder.createWidget();
   }


   public void initCustomizedWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final Color backgroundColor = Color.fromRGBA(0, (float) 0.3, (float) 0.2, 1);
      builder.setBackgroundColor(backgroundColor);

      final String proxy = "";
      final Downloader_WebGL downloader = new Downloader_WebGL(8, 10, proxy);
      builder.setDownloader(downloader);

      _widget = builder.createWidget();
   }


   public void initWithoutBuilder() {

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      g3mWidgetHolder.setWidth("400px");
      g3mWidgetHolder.setHeight("300px");

      _widget = new G3MWidget_WebGL();

      if (_widget.isSupported()) {

         final INativeGL nativeGL = new NativeGL_WebGL(_widget.getWebGLContext());

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
         final boolean processRotation = true;
         final boolean processZoom = true;
         cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
         cameraRenderer.addHandler(new CameraRotationHandler());
         cameraRenderer.addHandler(new CameraDoubleTapHandler());

         final CompositeRenderer mainRenderer = new CompositeRenderer();
         final LayerSet layerSet = new LayerSet();
         final WMSLayer bing = new WMSLayer( //
                  "ve", //
                  new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), //
                  WMSServerVersion.WMS_1_1_0, //
                  Sector.fullSphere(), //
                  "image/png", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  null);
         layerSet.addLayer(bing);
         final TileRendererBuilder tlBuilder = new TileRendererBuilder();
         tlBuilder.setLayerSet(layerSet);
         final TileRenderer tileRenderer = tlBuilder.create();
         mainRenderer.addRenderer(tileRenderer);

         final BusyMeshRenderer busyRenderer = new BusyMeshRenderer();

         final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

         final boolean logFPS = false;

         final boolean logDownloaderStatistics = false;

         final GTask initializationTask = null;

         final boolean autoDeleteInitializationTask = true;

         final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

         final UserData userData = null;


         _widget.initWidget(//
                  nativeGL, //
                  storage, // 
                  downloader, //
                  threadUtils, //
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
      }


      /*      
            final LayerSet layerSet = new LayerSet();
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
                        new LevelTileCondition(0, 2));

               layerSet.addLayer(bing);
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
                        null);
               layerSet.addLayer(pnoa);
            }

            final boolean useOSMLatLon = true;
            if (useOSMLatLon) {
               //         final WMSLayer osm = new WMSLayer( //
               //                  "osm", //
               //                  new URL("http://wms.latlon.org/", false), //
               //                  WMSServerVersion.WMS_1_1_0, //
               //                  Sector.fromDegrees(-85.05, -180.0, 85.5, 180.0), //
               //                  "image/jpeg", //
               //                  "EPSG:4326", //
               //                  "", //
               //                  false, //
               //                  null);
               //         layerSet.addLayer(osm);

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
                        null);
               layerSet.addLayer(osm);
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
                        null);
               layerSet.addLayer(ayto);
            }


            final ArrayList<Renderer> renderers = new ArrayList<Renderer>();
            //      if (true) {
            // dummy renderer with final a simple box
            //      final DummyRenderer dum = new DummyRenderer();
            //      renderers.add(dum);
            //
            //      }
            //      renderers.add(new GLErrorRenderer());


            final boolean useMarkers = true;
            if (useMarkers) {
               // marks renderer
               final boolean readyWhenMarksReady = false;
               final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);
               renderers.add(marksRenderer);

               marksRenderer.setMarkTouchListener(new MarkTouchListener() {
                  @Override
                  public boolean touchedMark(final Mark mark) {
                     Window.alert("Touched on mark: " + mark.getName());
                     return true;
                  }
               }, true);


               final Mark m1 = new Mark(//
                        "Fuerteventura", //
                        new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
               //m1->addTouchListener(listener);
               marksRenderer.addMark(m1);

               final Mark m2 = new Mark( //
                        "Las Palmas", //
                        new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
               //m2->addTouchListener(listener);
               marksRenderer.addMark(m2);

               final boolean randomMarkers = false;
               if (randomMarkers) {
                  final Random random = new Random();
                  for (int i = 0; i < 500; i++) {
                     final Angle latitude = Angle.fromDegrees((random.nextInt() % 180) - 90);
                     final Angle longitude = Angle.fromDegrees((random.nextInt() % 360) - 180);
                     //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());

                     marksRenderer.addMark(new Mark( //
                              "Random", //
                              new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                              new Geodetic3D(latitude, longitude, 0)));
                  }
               }

            }


            final boolean useQuadShapes = true;
            if (useQuadShapes) {
               final ShapesRenderer shapesRenderer = new ShapesRenderer();

               //         final String textureFileName = "g3m-marker.png";
               //         final IImage textureImage = IFactory.instance().createImageFromFileName(textureFileName);
               //
               //         final QuadShape quad = new QuadShape( //
               //                  new Geodetic3D(Angle.fromDegrees(37.78333333), //
               //                           Angle.fromDegrees(-122.41666666666667), //
               //                           10000), //
               //                  textureImage, //
               //                  true, //
               //                  textureFileName, //
               //                  500000, //
               //                  500000);
               //         quad.setHeading(Angle.fromDegrees(0));
               //         quad.setPitch(Angle.fromDegrees(0));
               //         shapesRenderer.addShape(quad);

               final Geodetic3D circlePosition = new Geodetic3D( //
                        Angle.fromDegrees(37.78333333), //
                        Angle.fromDegrees(-122.41666666666667), //
                        8000);
               final int circleRadius = 50000;
               final Color circleColor = Color.newFromRGBA(1, 1, 0, 1);
               final CircleShape circle = new CircleShape(circlePosition, circleRadius, circleColor);

               //circle.setHeading(Angle.fromDegrees(45));
               //circle.setPitch(Angle.fromDegrees(45));
               //circle.setScale(2.0, 0.5, 1);
               //circle.setRadius(circleRadius);

               shapesRenderer.addShape(circle);

               renderers.add(shapesRenderer);
            }


            final UserData userData = null;

            //      _widget.initWidget(cameraConstraints, layerSet, renderers, userData);

            final GTask initializationTask = null;
            final ArrayList<PeriodicalTask> periodicalTasks = null;
            final boolean incrementalTileQuality = true;
            
            */
   }
}
