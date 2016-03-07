

package org.glob3.mobile.client;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.NonOverlappingMarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PyramidElevationDataProvider;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SphericalPlanet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _g3mWidget         = null;


   private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
   }-*/;


   @Override
   public void onModuleLoad() {
      /*final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      _g3mWidget = createWidgetPlanetDebug();
      g3mWidgetHolder.add(_g3mWidget);


      // // Buenos Aires, there we go!
      // _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));

      // Canarias
      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));*/
	   
	   initPyramidDemo();
   }


   private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("/g3m-marker.png");
      final URL anchorBitmapURL = new URL("/anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private static NonOverlappingMark createMark(final String label,
                                                final Geodetic3D position) {
      final URL markBitmapURL = new URL("/g3m-marker.png");
      final URL anchorBitmapURL = new URL("/anchorWidget.png");

      final ColumnLayoutImageBuilder imageBuilderWidget = new ColumnLayoutImageBuilder( //
               new DownloaderImageBuilder(markBitmapURL), //
               new LabelImageBuilder(label, GFont.monospaced()) //
      );

      return new NonOverlappingMark( //
               imageBuilderWidget, //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }


   private static G3MWidget_WebGL createWidget() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));


      final NonOverlappingMarksRenderer renderer = new NonOverlappingMarksRenderer(30);
      builder.addRenderer(renderer);

      renderer.addMark(createMark("Label #1", Geodetic3D.fromDegrees(28.131817, -15.440219, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.947345, -13.523105, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.473802, -13.859360, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.467706, -16.251426, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.701819, -17.762003, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(28.086595, -17.105796, 0)));
      renderer.addMark(createMark(Geodetic3D.fromDegrees(27.810709, -17.917639, 0)));


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

                     final int steps = 8;
                     final float leftStep = (float) width / steps;
                     final float topStep = (float) height / steps;

                     canvas.setLineWidth(1);
                     canvas.setFillColor(Color.fromRGBA(0f, 0f, 0f, 0.75f));
                     for (int i = 1; i < steps; i++) {
                        canvas.fillRectangle(0, topStep * i, width, 1);
                        canvas.fillRectangle(leftStep * i, 0, 1, height);
                     }

                     canvas.setFont(GFont.monospaced());
                     canvas.setFillColor(Color.black());
                     // canvas.fillText("0,0", 0, 0);
                     // canvas.fillText("w,h", width, height);
                     for (int i = 0; i < steps; i++) {
                        canvas.fillText("Hellow World", leftStep * i, topStep * i);
                     }


                     final float width8 = (float) width / 8;
                     final float height8 = (float) height / 8;
                     canvas.drawImage(image, width8, height8); // ok
                     canvas.drawImage(image, width8 * 3, height8, 0.5f); // ok

                     final int imageWidth = image.getWidth();
                     final int imageHeight = image.getHeight();
                     canvas.drawImage(image, width8, height8 * 3, imageWidth * 2, imageHeight * 2); // ok
                     canvas.drawImage(image, width8 * 3, height8 * 3, imageWidth * 2, imageHeight * 2, 0.5f); //ok

                     // ok
                     canvas.drawImage(image, //
                              0, 0, imageWidth, imageHeight, //
                              width8 * 5, height8 * 5, imageWidth * 2, imageHeight * 2);
                     // ok
                     canvas.drawImage(image, //
                              0, 0, imageWidth, imageHeight, //
                              width8 * 7, height8 * 7, imageWidth * 2, imageHeight * 2, //
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
                        new URL("/g3m-marker.png"), //
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


   private static G3MWidget_WebGL createWidgetPlanetDebug() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);
      builder.getPlanetRendererBuilder().setRenderDebug(true);

      final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));


      return builder.createWidget();
   }
   
   
   
   private void initPyramidDemo(){
	   jsCalls();
   }
   
   private static G3MWidget_WebGL _widget;
   
   public static native void jsCalls()/*-{
		$wnd.G3M = {}
		$wnd.G3M.loadGlobe = @org.glob3.mobile.client.G3MWebGLTestingApplication::loadGlobe(IZ);
		$wnd.G3M.clearGlobe = @org.glob3.mobile.client.G3MWebGLTestingApplication::clearGlobe();
		$wnd.G3M.setPitch = @org.glob3.mobile.client.G3MWebGLTestingApplication::setPitch(D);
		$wnd.G3M.goTo = @org.glob3.mobile.client.G3MWebGLTestingApplication::goTo(DDD);
}-*/;

public static void clearGlobe(){
     if (_widget != null) {
   	  _widget.cancelCameraAnimation();
   	  _widget.unsinkEvents(Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
   	  clearResizeEventHandler();
     }
     _widget = null;
}

public static native void clearResizeEventHandler()/*-{
		clearInterval($wnd.g3mWidgetResizeChecker);
}-*/;

public static void setPitch (double pitch){
  if (_widget != null){
	   _widget.setCameraPitch(Angle.fromDegrees(pitch));
  }
}

public static void goTo (double lat, double lon, double hgt){
  if (_widget != null){
	   _widget.setCameraPosition(Geodetic3D.fromDegrees(lat,lon,hgt));
  }
}

public static void loadGlobe(int layer, boolean wireframe){
  final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
     
     LayerSet ls = new LayerSet();
     
     ls.addLayer(new BingMapsLayer(BingMapType.Aerial(),
       "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
       TimeInterval.fromDays(30)));
     
     MeshRenderer _meshRenderer = new MeshRenderer();
     builder.addRenderer(_meshRenderer);
     
     final Planet planet = SphericalPlanet.createEarth();
	  builder.setPlanet(planet);
     
     builder.getPlanetRendererBuilder().setLayerSet(ls);
     builder.getPlanetRendererBuilder().setIncrementalTileQuality(true);
     builder.getPlanetRendererBuilder().setRenderDebug(wireframe);
     String layerServer;
     Sector layerSector = Sector.fullSphere();
     switch(layer) {
	      case 0: 
	    	  layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/fix-16/";
	    	  break;
	      case 1:
	    	  layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/var-16/";
	    	  break;
	      case 2:
	    	  layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/fix-euro-16/";
	    	  layerSector = Sector.fromDegrees(34,-10,70,52);
	    	  break;
	      case 3:
	    	  //layerServer = "http://193.145.147.50:8080/DemoElevs/elevs/var-euro-16/";
	    	  layerServer = "http://www.elnublo.net/temporal/var-euro-16/";
	    	  layerSector = Sector.fromDegrees(34,-10,70,52);
	    	  break;
	      default:
	    	  layerServer = "";
	    	  break;
     };
     builder.getPlanetRendererBuilder().setElevationDataProvider(new PyramidElevationDataProvider(layerServer,layerSector));
     builder.getPlanetRendererBuilder().setVerticalExaggeration(2);
     //builder.getPlanetRendererBuilder().setElevationDataProvider(new BilPyramidElevationDataProvider("elevs/fusion/",Sector.fullSphere(),true,false));
    
     boolean showPrimarySectors = wireframe;
	   if (showPrimarySectors){
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, -180, 90, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, -90, 90, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, 0, 90, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(50, 90, 90, 180), planet);
		  
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, -180, 50, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, -90, 50, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, 0, 50, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(0, 90, 50, 180), planet);
		   
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, -180, 0, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, -90, 0, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, 0, 0, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-50, 90, 0, 180), planet);
		   
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, -180, -50, -90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, -90, -50, 0), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, 0, -50, 90), planet);
		   addSectorMesh(_meshRenderer, Sector.fromDegrees(-90, 90, -50, 180), planet);
	   
	   }
     
     _widget = builder.createWidget();
     
     //_widget.setCameraPosition(Geodetic3D.fromDegrees(28,-15.60, 100000));
     //(lat=d, lon=d, height=)
     //_widget.setCameraPosition(Geodetic3D.fromDegrees(28.311267430222475,-16.464517200904726,87196.6538574849));
     final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
     g3mWidgetHolder.add(_widget);
     
}


private static void addSectorMesh(MeshRenderer renderer, Sector sector, Planet planet){
  final double POINT_DIV = 100;
  
  FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(
		   planet);
  fbb.add(sector._upper, 5);
  //Delta instruction here
  if (sector._upper._latitude._degrees != 90){
	   double delta = (sector._upper._longitude._radians - sector._lower._longitude._radians) / POINT_DIV;
	   double lonRads = sector._upper._longitude._radians;
	   for (int i=0; i<POINT_DIV; i++){
		   lonRads -= delta;
		   fbb.add(sector._upper._latitude,Geodetic2D.fromRadians(sector._upper._latitude._radians,lonRads)._longitude, 5);
	   }
  }
  else fbb.add(sector._upper._latitude, sector._lower._longitude, 5);
  
  double delta = (sector._upper._latitude._radians - sector._lower._latitude._radians) / POINT_DIV;
  double latRads = sector._upper._latitude._radians;
  for (int i=0; i<POINT_DIV; i++){
	   latRads -= delta;
	   fbb.add(Geodetic2D.fromRadians(latRads,sector._lower._longitude._radians)._latitude,sector._lower._longitude, 5);
  } 
  
  //Delta instruction here
  if (sector._lower._latitude._degrees != -90){
	   delta = (sector._upper._longitude._radians - sector._lower._longitude._radians) / POINT_DIV;
	   double lonRads = sector._lower._longitude._radians;
	   for (int i=0; i<POINT_DIV; i++){
		   lonRads += delta;
		   fbb.add(sector._lower._latitude,Geodetic2D.fromRadians(sector._lower._latitude._radians,lonRads)._longitude, 5);
	   }
  }
  else fbb.add(sector._lower._latitude,sector._upper._longitude, 5);
  
  delta = (sector._upper._latitude._radians - sector._lower._latitude._radians) / POINT_DIV;
  latRads = sector._lower._latitude._radians;
  for (int i=0; i<POINT_DIV; i++){
	   latRads += delta;
	   fbb.add(Geodetic2D.fromRadians(latRads,sector._upper._longitude._radians)._latitude,sector._upper._longitude, 5);
  } 
  
  renderer.addMesh(new DirectMesh(GLPrimitive.lineStrip(), true, fbb.getCenter(), fbb.create(), 6.0f, 1.0f, 
		   Color.yellow(), null, 0.0f, false));
}


}
