package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLBuildingTessellator;
import org.glob3.mobile.generated.CityGMLRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeElevationDataProvider;
import org.glob3.mobile.generated.CompositeMesh;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.EllipsoidalPlanet;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian3D;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MEventContext;
import org.glob3.mobile.generated.GEOVectorLayer;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.IndexedMesh;
import org.glob3.mobile.generated.LayerBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.MutableVector3D;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.ShortBufferBuilder;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TerrainTouchListener;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   public static G3MWidget_WebGL     _g3mWidget         = null;
   
   class CityGMLModelFile {
	   public String _fileName;
	   public boolean _needsToBeFixedOnGround;
   };
   
   public static CityGMLRenderer cityGMLRenderer;
   public static MyEDCamConstrainer camConstrainer;
   private ArrayList<CityGMLModelFile> _cityGMLFiles = new ArrayList<>();
   private static ShapesRenderer shapesRenderer;
   private static MeshRenderer pipeMeshRenderer;
   private static MeshRenderer holeRenderer;
   private static MeshRenderer meshRenderer;
   
   public static CompositeElevationDataProvider combo;
   private static ElevationData elevData = null;
   private static ElevationData holeElevData = null; //hole change.
   private boolean isUsingDem;
   
   private int _modelsLoadedCounter;

   @Override
   public void onModuleLoad() {
      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      _g3mWidget = createWidgetBuildings(false);
      initTask(true);
      g3mWidgetHolder.add(_g3mWidget);
      NativeUtils.generateG3MCalls();

      // // Buenos Aires, there we go!
      // _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000));

      // Canarias
      //_g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(49.0159538369538, 8.39245743376133, 69.1385));
   }
   
   private G3MWidget_WebGL createWidgetBuildings(boolean useDem) {
	   
	   //Previous initializations
	   _modelsLoadedCounter = 0;
	   NativeUtils.callNativeLog("Loading GML files ...");
	   /*addCityGMLFile("innenstadt_ost_4326_lod2.gml",false);
	   addCityGMLFile("innenstadt_west_4326_lod2.gml",false);
	   addCityGMLFile("technologiepark_WGS84.gml",true);
	 //  [self addCityGMLFile:"file:///AR_demo_with_buildings.gml" needsToBeFixOnGround:true]; //NOT WORKING
	   addCityGMLFile("hagsfeld_4326_lod2.gml",false);
	   addCityGMLFile("durlach_4326_lod2_PART_1.gml",false);
	   addCityGMLFile("durlach_4326_lod2_PART_2.gml",false);
	   addCityGMLFile("hohenwettersbach_4326_lod2.gml",false);
	   addCityGMLFile("bulach_4326_lod2.gml",false);
	   addCityGMLFile("daxlanden_4326_lod2.gml",false);
	   addCityGMLFile("knielingen_4326_lod2_PART_1.gml",false);
	   addCityGMLFile("knielingen_4326_lod2_PART_2.gml",false);*/
	   addCityGMLFile("knielingen_4326_lod2_PART_3.gml",false);
	   //addCityGMLFile("building-1.gml",false);
	   //addCityGMLFile("building-2.gml",false);
	   
	   //Let's start!
	   NativeUtils.callNativeLog("Configuring globe...");
	   final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

	   final LayerSet layerSet = new LayerSet();
	   layerSet.addLayer(LayerBuilder.createBingLayer(true));
	   final Planet planet = EllipsoidalPlanet.createEarth();
	   builder.setPlanet(planet);
	   builder.getPlanetRendererBuilder().setLayerSet(layerSet);

	   final String proxy = null; // "http://galileo.glob3mobile.com/" + "proxy.php?url="
	   builder.setDownloader(new Downloader_WebGL( //
	               8, // maxConcurrentOperationCount
	               10, // delayMillis
	               proxy));
	      
	      
	   //Renderers who matter
	   meshRenderer = new MeshRenderer();
	   builder.addRenderer(meshRenderer);
	   final MeshRenderer meshRendererPointCloud = new MeshRenderer();
	   builder.addRenderer(meshRendererPointCloud);
	   pipeMeshRenderer = new MeshRenderer();
	   builder.addRenderer(pipeMeshRenderer);
	   
	   holeRenderer = new MeshRenderer();
	   builder.addRenderer(holeRenderer);
	   final MarksRenderer marksRenderer = new MarksRenderer(false);
	   builder.addRenderer(marksRenderer);
	   shapesRenderer = new ShapesRenderer();
	   builder.addRenderer(shapesRenderer);
	      
	   //Showing Footprints
	   /*GEOVectorLayer vectorLayer = new GEOVectorLayer(2,18,
	                                       0,18,
	                                       1.0f,
	                                       new LevelTileCondition(17, 18));
	   layerSet.addLayer(vectorLayer);*/
	      
	   cityGMLRenderer = new CityGMLRenderer(meshRenderer,
	                                            null /* marksRenderer */,
	                                            null);
	      
	   cityGMLRenderer.setTouchListener(new MyCityGMLBuildingTouchedListener());
	   builder.addRenderer(cityGMLRenderer);
	   //builder.setInitializationTask(new MyInitTask(this,builder, useDem));   
	   camConstrainer = new MyEDCamConstrainer(null,""); //Wait for ED to arrive
	   builder.addCameraConstraint(camConstrainer);
	   builder.setBackgroundColor(new Color(Color.fromRGBA255(0, 0, 0, 0)));
	   //builder.getPlanetRendererBuilder().setRenderDebug(true);
	   return builder.createWidget();
   }
   
   private void initTask(boolean useDem){
	   isUsingDem = useDem;
	   if (useDem){
		   Geodetic3D pipeCenter = Geodetic3D.fromDegrees(49.01664816, 8.43442120,0);
			Planet p = _g3mWidget.getG3MContext().getPlanet();
			Vector3D v1= p.toCartesian(pipeCenter);
			Vector3D v2 = new Vector3D(v1._x - 10, v1._y - 10, v1._z);
			Vector3D v3 = new Vector3D(v1._x + 10, v1._y + 10, v1._z);
			Geodetic3D l = p.toGeodetic3D(v2);
			Geodetic3D u = p.toGeodetic3D(v3);
			Geodetic2D lower = Geodetic2D.fromDegrees(
					Math.min(l._latitude._degrees, u._latitude._degrees), 
					Math.min(l._longitude._degrees, u._longitude._degrees));
			Geodetic2D upper = Geodetic2D.fromDegrees(
					Math.max(l._latitude._degrees, u._latitude._degrees), 
					Math.max(l._longitude._degrees, u._longitude._degrees));
			
			Sector holeSector = new Sector(lower,upper);
			
			SingleBilElevationDataProvider holeEdp = new SingleBilElevationDataProvider(new URL("hole3.bil"),
		                                                                             holeSector,
		                                                                             new Vector2I(11, 11));
		    
		    holeEdp.requestElevationData(holeSector, new Vector2I(11, 11),new MyHoleListener(this), true);
		   
		   
			Sector karlsruheSector = Sector.fromDegrees(48.9397891179, 8.27643508429, 49.0930546874, 8.5431344933);
		    SingleBilElevationDataProvider edp = new SingleBilElevationDataProvider(new URL("ka_31467.bil"),
		                                                                             karlsruheSector,
		                                                                             new Vector2I(308, 177));
		    
		    edp.requestElevationData(karlsruheSector, new Vector2I(308, 177),new MyEDListener(this), true);
		    
		    combo = new CompositeElevationDataProvider();
		    combo.addElevationDataProvider(holeEdp);
		    combo.addElevationDataProvider(edp);
		    _g3mWidget.getG3MWidget().getPlanetRenderer().setElevationDataProvider(combo,true);
		    _g3mWidget.getG3MWidget().getPlanetRenderer().addTerrainTouchListener(new TerrainTouchListener(){

				@Override
				public void dispose() {}

				@Override
				public boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position,
						Tile tile) {
					changeHole(position);
					return false;
				}
		    	
		    });;
		    camConstrainer.setEDP(combo);
		    camConstrainer.setThreadUtils(_g3mWidget.getG3MContext().getThreadUtils());
		}
		else{
			loadCityModel();
		}
   }

   
   public static void changeHole(Geodetic3D position){
	   Planet planet = _g3mWidget.getG3MContext().getPlanet();
	   IThreadUtils _tUtils = _g3mWidget.getG3MContext().getThreadUtils();
	   
	   Vector3D v1= planet.toCartesian(position);
		Vector3D v2 = new Vector3D(v1._x - 10, v1._y - 10, v1._z);
		Vector3D v3 = new Vector3D(v1._x + 10, v1._y + 10, v1._z);
		Geodetic3D l = planet.toGeodetic3D(v2);
		Geodetic3D u = planet.toGeodetic3D(v3);
		Geodetic2D lower = Geodetic2D.fromDegrees(
					Math.min(l._latitude._degrees, u._latitude._degrees), 
					Math.min(l._longitude._degrees, u._longitude._degrees));
		Geodetic2D upper = Geodetic2D.fromDegrees(
					Math.max(l._latitude._degrees, u._latitude._degrees), 
					Math.max(l._longitude._degrees, u._longitude._degrees));
		final Sector holeSector = new Sector(lower,upper);
		//generateHoleCover(holeSector);
		HoleCoverHelper.generateHoleCover(holeSector, v1, planet, elevData, holeRenderer);
		
		ILogger.instance().logError(holeSector.description());
		final SingleBilElevationDataProvider holeEdp = new SingleBilElevationDataProvider(new URL("hole3.bil"),
                holeSector,
                new Vector2I(11, 11));
	    //holeEdp.requestElevationData(holeSector, new Vector2I(11, 11),new MyHoleListener(this), true);


		_tUtils.invokeInRendererThread(new GTask(){
			
			@Override
			public void run(G3MContext context) {
				// TODO Auto-generated method stub
				holeElevData.setSector(holeSector);
				combo.changeFirstEDP(holeEdp);
			}
		}, true);
   }
   
   public void loadCityModel(){
	  if ((isUsingDem && holeElevData != null && elevData != null) || (!isUsingDem)) {
	   
		  for (int i = 0; i < _cityGMLFiles.size(); i++) {
			  NativeUtils.assignHtmlToElementId(_modelsLoadedCounter+" out of "+_cityGMLFiles.size()+" models loaded (0 %)","LoadingDetails");
		   
			  cityGMLRenderer.addBuildingsFromURL(new URL(_cityGMLFiles.get(i)._fileName),
					  _cityGMLFiles.get(i)._needsToBeFixedOnGround,
					  new MyCityGMLRendererListener(this),
					  true);
		  }
	   
		  PipesModel.addMeshes("pipesCoords.csv", _g3mWidget.getG3MContext().getPlanet(), pipeMeshRenderer, elevData, -4.0);
		  PipesModel.addMeshes("pipesCoordsMetzt.csv", _g3mWidget.getG3MContext().getPlanet(), pipeMeshRenderer, null, -4.0);
	  }
   }
   
   public void setElevationData(ElevationData ed){
	   elevData = ed;
   }
   
   public void setHoleElevationData(ElevationData ed){
	   holeElevData = ed;
   }
   
   public boolean areAllPipesLoaded(){
	   return (PipesModel.loadedFiles == 2);
   }
   
   private void addCityGMLFile(String fileName, boolean needsToBeFixOnGround){
	   CityGMLModelFile m = new CityGMLModelFile();
	   m._fileName = fileName;
	   m._needsToBeFixedOnGround = needsToBeFixOnGround;
	   _cityGMLFiles.add(m);
	 }


   public void onCityModelLoaded(){
	   _modelsLoadedCounter++;
	   if (_modelsLoadedCounter == _cityGMLFiles.size()){
	     cityGMLRenderer.addBuildingDataFromURL(new URL("karlsruhe_data.geojson"));
	   }
	   onProgress();
   }
   
   public void onProgress() {
	   float p = (_cityGMLFiles.size() == 0) ? 1: (float)(_modelsLoadedCounter) / ((float)_cityGMLFiles.size());
	   NativeUtils.assignHtmlToElementId(_modelsLoadedCounter+" out of "+_cityGMLFiles.size()+" models loaded ("+p*100+" %)","LoadingDetails");
	   
	   if (p == 1){
	     onAllDataLoaded();
	     NativeUtils.changeDisplayStatus("none", "startScreen");
	     NativeUtils.changeDisplayStatus("inline-block", "menuIcon");
	   }
   }
   
   public void onAllDataLoaded(){
	   ILogger.instance().logInfo("City Model Loaded");
	   
	   final boolean includeCastleModel = true;
	   if (includeCastleModel){
		   shapesRenderer.loadJSONSceneJS(new URL("k_s/k_schloss.json"),
	                                     "k_s/",
	                                     false, // isTransparent
	                                     new Geodetic3D(Geodetic3D.fromDegrees(49.013500, 8.404249, 117.82)), //
	                                     AltitudeMode.ABSOLUTE,
	                                     new SchlossListener());
	     
	     String v[] = {"91214493", "23638639", "15526553", "15526562", "15526550", "13956101", "156061723", "15526578"};
	     
	     for (int i = 0; i < 8; i++){
	       CityGMLBuilding b = cityGMLRenderer.getBuildingWithName(v[i]);
	       if (b != null){
	         CityGMLBuildingTessellator.changeColorOfBuildingInBoundedMesh(b, Color.transparent());
	       }
	     }
	     
	     
	   }
	   
	   //Whole city!
	   _g3mWidget.getG3MContext().getThreadUtils().invokeInRendererThread(new GTask(){

		@Override
		public void run(G3MContext context) {
			// TODO Auto-generated method stub
			_g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(5),
                    Geodetic3D.fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                    Angle.fromDegrees(-109.452892),
                    Angle.fromDegrees(-44.938813)
                    );
		}
		   
	   }, true);
	   
	   
	 /*
	 //METZ
	   [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
	                                                 Geodetic3D::fromDegrees(49.1034419341, 6.2225732157,  100.0),//1000
	                                                 Angle::fromDegrees(0.0),//0
	                                                 Angle::fromDegrees(-15.0) //-90
	                                                 );
	  */
	   
	  Timer t = new Timer() {
		  @Override
		  public void run() {
			  NativeUtils.activateMenu();
			  camConstrainer.shouldCaptureMotion(true, pipeMeshRenderer, PipesModel.cylinderInfo);
		  }
	   };
	   // Schedule the timer to run once in 5 seconds.
	   t.schedule(5000);
	   //NO WAITING ANYMORE
	   //_waitingMessageView.hidden = TRUE;
	   
	  // printf("N OF WALLS: %d\n", numberOfWalls);
	   //printf("N OF TESSELLATED WALLS: %d\n", numberOfP3D);
	   //printf("N OF TESSELLATED WALLS_4: %d\n", numberOfP3D_4);
	 }
   
   public static void activatePipes(boolean active){
	   pipeMeshRenderer.setEnable(active);
	   camConstrainer.shouldCaptureMotion(active, pipeMeshRenderer, PipesModel.cylinderInfo);
   }
   
   public static void activateBuildings(boolean active){
	   cityGMLRenderer.setEnable(active);
	   meshRenderer.setEnable(active);
   }
   
   public static void setProximity(double proximity){
	   Cylinder.setProximity(proximity);
   }
   
   public static void setAlphaMethod(int method){
	   Cylinder.setDistanceMethod(method);
   }
   

   /*private static NonOverlappingMark createMark(final Geodetic3D position) {
      final URL markBitmapURL = new URL("/g3m-marker.png");
      final URL anchorBitmapURL = new URL("/anchorWidget.png");

      return new NonOverlappingMark( //
               new DownloaderImageBuilder(markBitmapURL), //
               new DownloaderImageBuilder(anchorBitmapURL), //
               position);
   }*/


  /*private static NonOverlappingMark createMark(final String label,
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
   }*/

   /*private class AnimateHUDWidgetsTask
   extends
   GTask {

      LabelImageBuilder _labelBuilder;
      G3MWidget         _widget;


      public AnimateHUDWidgetsTask(final G3MWidget widget,
                                   final LabelImageBuilder labelBuilder) {
         _labelBuilder = labelBuilder;
         _widget = widget;
      }


      @Override
      public void run(final G3MContext context) {
         // TODO Auto-generated method stub
         _labelBuilder.setText("H: " + _widget.getCurrentCamera().getHeading() + "P: " + _widget.getCurrentCamera().getPitch()
                  + "R: " + _widget.getCurrentCamera().getRoll());
      }

   }*/


   /*private G3MWidget_WebGL createWidgetVR() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      final CameraRenderer cr = new CameraRenderer();
      cr.addHandler(new DeviceAttitudeCameraHandler(true));
      builder.setCameraRenderer(cr);

      final HUDRenderer hudRenderer = new HUDRenderer();
      builder.addRenderer(hudRenderer);
      final LabelImageBuilder labelBuilder = new LabelImageBuilder("glob3", // text
               GFont.monospaced(38), // font
               6, // margin
               Color.yellow(), // color
               Color.black(), // shadowColor
               3, // shadowBlur
               1, // shadowOffsetX
               -1, // shadowOffsetY
               Color.red(), // backgroundColor
               4, // cornerRadius
               true // mutable
               );

      final HUDQuadWidget label = new HUDQuadWidget(labelBuilder, new HUDAbsolutePosition(10), new HUDAbsolutePosition(10),
               new HUDRelativeSize(1, HUDRelativeSize.Reference.BITMAP_WIDTH), new HUDRelativeSize(1,
                        HUDRelativeSize.Reference.BITMAP_HEIGHT));
      hudRenderer.addWidget(label);

      final G3MWidget_WebGL widget = builder.createWidget();

      builder.addPeriodicalTask(new PeriodicalTask(TimeInterval.fromMilliseconds(200), new AnimateHUDWidgetsTask(
               widget.getG3MWidget(), labelBuilder)));


      return widget;
   }*/

   /*private static G3MWidget_WebGL createWidget() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final String proxy = null;
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy));

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      builder.addRenderer(createMarksRenderer());


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
                     ILogger.instance().logError("ERROR DOWNLOADING CITYGML");
                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IImage image,
                                         final boolean expired) {
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
   }*/


}