

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.NASAElevationDataProvider;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.PointCloudsRenderer;
import org.glob3.mobile.generated.PointCloudsRenderer.ColorPolicy;
import org.glob3.mobile.generated.PointCloudsRenderer.PointCloudMetadataListener;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
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
   private RelativeLayout    _placeHolder;

   G3MBuilder_Android        builder       = null;
   MarksRenderer             marksRenderer = new MarksRenderer(false);


   @Override
   protected void onCreate(final Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);
      /*
      	   final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      	   final Planet planet = Planet.createEarth();
      	   //const Planet* planet = Planet::createSphericalEarth();
      	   //final Planet planet = Planet.createFlatEarth();
      	   builder.setPlanet(planet);

      	   // set camera handlers
      	   //CameraRenderer cameraRenderer = createCameraRenderer();
      	   MeshRenderer meshRenderer = new MeshRenderer();
      	   builder.addRenderer( meshRenderer );
      	   //cameraRenderer.setDebugMeshRenderer(meshRenderer);
      	   //builder.setCameraRenderer(cameraRenderer);
      	   
      	   // create shape
      	   ShapesRenderer shapesRenderer = new ShapesRenderer();
      	   Shape box = new BoxShape(new Geodetic3D(Angle.fromDegrees(28.4),
      			   Angle.fromDegrees(-16.4),
      			   0),
      			   AltitudeMode.ABSOLUTE,
      			   new Vector3D(3000, 3000, 20000),
      			   2,
      			   Color.fromRGBA(1.0f, 0.2f, 0.0f, 0.5f),
      			   Color.newFromRGBA(0.0f, 0.75f, 0.0f, 0.75f));
      	   shapesRenderer.addShape(box);
      	   builder.addRenderer(shapesRenderer);

      	   // create elevations for Tenerife from bil file
      	   Sector sector = Sector.fromDegrees (27.967811065876,                  // min latitude
      			   -17.0232177085356,                // min longitude
      			   28.6103464294992,                 // max latitude
      			   -16.0019401695656);               // max longitude
      	   Vector2I extent = new Vector2I(256, 256);                             // image resolution
      	   URL url = new URL("file:///Tenerife-256x256.bil", false);
      	   ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
      	   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
      	   builder.getPlanetRendererBuilder().setVerticalExaggeration(4.0f);

      	   _g3mWidget = builder.createWidget();  
      */
      _g3mWidget = createWidget();
      //_g3mWidget = createWidgetStreamingElevations();
      //_g3mWidget = createWidgetVR();

      // set camera looking at Tenerife
      final Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
      _g3mWidget.setCameraPosition(position);
      _g3mWidget.setCameraPitch(Angle.fromDegrees(-50.0));

      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      //_placeHolder.addView(_g3mWidget);
      _placeHolder.addView(_g3mWidget);
   }


   /*
   super.onCreate(savedInstanceState);

   requestWindowFeature(Window.FEATURE_NO_TITLE);
   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

   setContentView(R.layout.activity_main);

   _g3mWidget = createWidgetVR();

   final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);

   placeHolder.addView(_g3mWidget);

   _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));

   //      // Buenos Aires, there we go!
   //      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));
   }*/


   private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private static NonOverlappingMark createMark(final String label,
                                                final Geodetic3D position) {
      final URL markBitmapURL = new URL("file:///g3m-marker.png");
      final URL anchorBitmapURL = new URL("file:///anchorWidget.png");


      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
               new DownloaderImageBuilder(markBitmapURL), //
               new LabelImageBuilder(label, GFont.monospaced()) //
               );

      return new NonOverlappingMark( //
               imageBuilderWidget, //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private G3MWidget_Android createWidgetVR() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final CameraRenderer cr = new CameraRenderer();
      cr.addHandler(new DeviceAttitudeCameraHandler(true));
      builder.setCameraRenderer(cr);

      return builder.createWidget();
   }


   private G3MWidget_Android createWidgetStreamingElevations() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final NASAElevationDataProvider edp = new NASAElevationDataProvider();

      builder.getPlanetRendererBuilder().setElevationDataProvider(edp);

      return builder.createWidget();
   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      final PointCloudsRenderer pcr = new PointCloudsRenderer();

      final URL serverURL = new URL("http://glob3mobile.dyndns.org:8080");
      //  final String cloudName = "Loudoun-VA_simplified2_LOD";
      final String cloudName = "Loudoun-VA_fragment_LOD";
      final long downloadPriority = DownloadPriority.LOWER;
      final TimeInterval timeToCache = TimeInterval.zero();
      final boolean readExpired = false;
      final float pointSize = 2;
      final float verticalExaggeration = 1;
      final double deltaHeight = 0;
      final PointCloudMetadataListener metadataListener = null;
      final boolean deleteListener = true;

      pcr.addPointCloud( //
               serverURL, //
               cloudName, //
               downloadPriority, //
               timeToCache, //
               readExpired, //
               ColorPolicy.MIN_AVERAGE3_HEIGHT, //
               pointSize, //
               verticalExaggeration, //
               deltaHeight, //
               metadataListener, //
               deleteListener);


      return builder.createWidget();

   }

   //    private LayerSet createLayerSet() {
   //      final LayerSet layerSet = new LayerSet();
   //      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
   //
   //
   //      final BingMapsLayer rasterLayer = new BingMapsLayer( //
   //               BingMapType.AerialWithLabels(), //
   //               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", //
   //               TimeInterval.fromDays(30));
   //      layerSet.addLayer(rasterLayer);
   //
   //
   //
   //
   //      final String urlTemplate = "http://192.168.1.2/ne_10m_admin_0_countries-Levels10/{level}/{y}/{x}.geojson";
   //
   //      //final String urlTemplate = "http://glob3mobile.dyndns.org/vectorial/swiss-buildings/{level}/{x}/{y}.geojson";
   //
   //      final int firstLevel = 2;
   //      final int maxLevel = 17;
   //
   //      final Geodetic2D lower = new Geodetic2D( //
   //               Angle.fromDegrees(45.8176852), //
   //               Angle.fromDegrees(5.956216));
   //      final Geodetic2D upper = new Geodetic2D( //
   //               Angle.fromDegrees(47.803029), //
   //               Angle.fromDegrees(10.492264));
   //
   //      final Sector sector = new Sector(lower, upper);
   //
   //      //final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();
   ///*
   //      final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
   //               symbolizer, //
   //               urlTemplate, //
   //               sector, // sector
   //               firstLevel, //
   //               maxLevel, //
   //               TimeInterval.fromDays(30), // timeToCache
   //               true, // readExpired
   //               1, // transparency
   //               new LevelTileCondition(15, 21), // condition
   //               "" // disclaimerInfo
   //      );
   //      layerSet.addLayer(tiledVectorLayer);*/
   //
   //
   //      return layerSet;
   //   }
   /*=======
         final boolean testCanvas = false;
         if (testCanvas) {
            final ShapesRenderer shapesRenderer = new ShapesRenderer();
            builder.addRenderer(shapesRenderer);


            builder.setInitializationTask(new GInitializationTask() {
               @Override
               public void run(final G3MContext context) {


                  final IImageDownloadListener listener = new IImageDownloadListener() {
                     @Override
                     public void onError(final URL url) {
                     }


                     @Override
                     public void onDownload(final URL url,
                                            final IImage image,
                                            final boolean expired) {

                        final ICanvas canvas = context.getFactory().createCanvas();
                        final int width = 1024;
                        final int height = 1024;
                        canvas.initialize(width, height);

                        canvas.setFillColor(Color.fromRGBA(1f, 1f, 0f, 0.5f));
                        canvas.fillRectangle(0, 0, width, height);
                        canvas.setLineWidth(4);
                        canvas.setLineColor(Color.black());
                        canvas.strokeRectangle(0, 0, width, height);
   >>>>>>> purgatory

                        final int steps = 8;
                        final float leftStep = (float) width / steps;
                        final float topStep = (float) height / steps;

   <<<<<<< HEAD
   <<<<<<< HEAD
   	@Override
   	public boolean onCreateOptionsMenu(final Menu menu) {
   		// Inflate the menu; this adds items to the action bar if it is present.
   		getMenuInflater().inflate(R.menu.main, menu);
   		return true;
   	}
   	
   	public CameraRenderer createCameraRenderer()
   	{
   	  CameraRenderer cameraRenderer = new CameraRenderer();
   	  final boolean useInertia = true;
   	  cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
   	  final boolean allowRotationInDoubleDrag = true;
   	  cameraRenderer.addHandler(new CameraDoubleDragHandler(allowRotationInDoubleDrag));
   	  //cameraRenderer.addHandler(new CameraZoomAndRotateHandler());

   	  cameraRenderer.addHandler(new CameraRotationHandler());
   	  cameraRenderer.addHandler(new CameraDoubleTapHandler());
   	  
   	  return cameraRenderer;
   	}


   =======
   >>>>>>> wheel-handler
   =======
                        canvas.setLineWidth(1);
                        canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f, 0.75f));
                        for (int i = 1; i < steps; i++) {
                           canvas.fillRectangle(0, topStep * i, width, 1);
                           canvas.fillRectangle(leftStep * i, 0, 1, height);
                        }

                        canvas.setFont(GFont.monospaced());
                        canvas.setFillColor(Color.black());
                        //                  canvas.fillText("0,0", 0, 0);
                        //                  canvas.fillText("w,h", width, height);
                        for (int i = 0; i < steps; i++) {
                           canvas.fillText("Hellow World", leftStep * i, topStep * i);
                        }

                        //                  canvas.drawImage(image, width / 4, height / 4); // ok

                        canvas.drawImage(image, width / 8, height / 8); // ok
                        canvas.drawImage(image, (width / 8) * 3, height / 8, 0.5f); // ok

                        canvas.drawImage(image, width / 8, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2); // ok
                        canvas.drawImage(image, (width / 8) * 3, (height / 8) * 3, image.getWidth() * 2, image.getHeight() * 2, 0.5f); //ok

                        // ok
                        canvas.drawImage(image, //
                                 0, 0, image.getWidth(), image.getHeight(), //
                                 (width / 8) * 5, (height / 8) * 5, image.getWidth() * 2, image.getHeight() * 2);
                        // ok
                        canvas.drawImage(image, //
                                 0, 0, image.getWidth(), image.getHeight(), //
                                 (width / 8) * 7, (height / 8) * 7, image.getWidth() * 2, image.getHeight() * 2, //
                                 0.5f);


                        canvas.createImage(new IImageListener() {
                           @Override
                           public void imageCreated(final IImage canvasImage) {
                              final QuadShape quad = new QuadShape( //
                                       Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 1000), //
                                       AltitudeMode.ABSOLUTE, //
                                       canvasImage, //
                                       canvasImage.getWidth() * 15.0f, //
                                       canvasImage.getHeight() * 15.0f, //
                                       true);

                              shapesRenderer.addShape(quad);
                           }
                        }, true);

                        canvas.dispose();

                        image.dispose();
                     }


                     @Override
                     public void onCanceledDownload(final URL url,
                                                    final IImage image,
                                                    final boolean expired) {
                     }


                     @Override
                     public void onCancel(final URL url) {
                     }
                  };


                  context.getDownloader().requestImage( //
                           new URL("file:///g3m-marker.png"), //
                           1, // priority, //
                           TimeInterval.zero(), //
                           false, //
                           listener, //
                           true);
               }


               @Override
               public boolean isDone(final G3MContext context) {
                  return true;
               }
            });
         }


         return builder.createWidget();
      }
   >>>>>>> purgatory
   */
}
