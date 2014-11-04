

package com.glob3.mobile.g3mandroidtestingapplication;

<<<<<<< HEAD

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.CameraZoomAndRotateHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.HUDQuadWidget;
import org.glob3.mobile.generated.HUDRelativePosition;
import org.glob3.mobile.generated.HUDRelativeSize;
import org.glob3.mobile.generated.HUDRenderer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
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
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TiledVectorLayer;
=======
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.PointCloudsRenderer;
import org.glob3.mobile.generated.PointCloudsRenderer.PointCloudMetadataListener;
>>>>>>> purgatory
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
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


<<<<<<< HEAD
   //   private RelativeLayout    _placeHolder;

   G3MBuilder_Android builder = null;
   MarksRenderer marksRenderer = new MarksRenderer(false);

=======
>>>>>>> purgatory
   @Override
   protected void onCreate(final Bundle savedInstanceState) {

	   super.onCreate(savedInstanceState);

	   requestWindowFeature(Window.FEATURE_NO_TITLE);
	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	   setContentView(R.layout.activity_main);

	   final G3MBuilder_Android builder = new G3MBuilder_Android(this);

	   //const Planet* planet = Planet::createEarth();
	   //const Planet* planet = Planet::createSphericalEarth();
	   final Planet planet = Planet.createFlatEarth();
	   builder.setPlanet(planet);

	   // set camera handlers
	   CameraRenderer cameraRenderer = createCameraRenderer();
	   MeshRenderer meshRenderer = new MeshRenderer();
	   builder.addRenderer( meshRenderer );
	   cameraRenderer.setDebugMeshRenderer(meshRenderer);
	   builder.setCameraRenderer(cameraRenderer);
	   
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
	   URL url = new URL("file:///Tenerife-256x256.bil", false);
	   ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
	   builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
	   builder.getPlanetRendererBuilder().setVerticalExaggeration(4.0f);

	   _g3mWidget = builder.createWidget();  

	   // set camera looking at Tenerife
	   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
	   _g3mWidget.setCameraPosition(position);
	   _g3mWidget.setCameraPitch(Angle.fromDegrees(-50.0));

	   _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
	   _placeHolder.addView(_g3mWidget);

	   /*
      super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);


      _g3mWidget = createWidget();

      final RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);


      placeHolder.addView(_g3mWidget);


      final Geodetic3D zurichPos = Geodetic3D.fromDegrees(40, -75, 80000);
      _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(5), zurichPos, Angle.zero(),
               Angle.fromDegrees(-90));*/

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
      final PointCloudMetadataListener metadataListener = null;
      final boolean deleteListener = true;

      pcr.addPointCloud( //
               serverURL, cloudName, //
               downloadPriority, timeToCache, readExpired, //
               pointSize, //
               verticalExaggeration, //
               metadataListener, deleteListener);


<<<<<<< HEAD
      builder.addRenderer(_weatherMarkers);

      return builder.createWidget();

   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);
=======
      builder.addRenderer(pcr);
>>>>>>> purgatory

      //      final TimeInterval connectTimeout = TimeInterval.fromSeconds(60);
      //      final TimeInterval readTimeout = TimeInterval.fromSeconds(65);
      //      final boolean saveInBackground = true;

      //      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);

      return builder.createWidget();
      
   }


<<<<<<< HEAD
	
		
      final LayerSet layerSet = new LayerSet();
      //layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

    private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));


      final BingMapsLayer rasterLayer = new BingMapsLayer( //
               BingMapType.AerialWithLabels(), //
               "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc", //
               TimeInterval.fromDays(30));
      layerSet.addLayer(rasterLayer);




      final String urlTemplate = "http://192.168.1.2/ne_10m_admin_0_countries-Levels10/{level}/{y}/{x}.geojson";

      //final String urlTemplate = "http://glob3mobile.dyndns.org/vectorial/swiss-buildings/{level}/{x}/{y}.geojson";

      final int firstLevel = 2;
      final int maxLevel = 17;

      final Geodetic2D lower = new Geodetic2D( //
               Angle.fromDegrees(45.8176852), //
               Angle.fromDegrees(5.956216));
      final Geodetic2D upper = new Geodetic2D( //
               Angle.fromDegrees(47.803029), //
               Angle.fromDegrees(10.492264));

      final Sector sector = new Sector(lower, upper);

      //final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();
/*
      final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
               symbolizer, //
               urlTemplate, //
               sector, // sector
               firstLevel, //
               maxLevel, //
               TimeInterval.fromDays(30), // timeToCache
               true, // readExpired
               1, // transparency
               new LevelTileCondition(15, 21), // condition
               "" // disclaimerInfo
      );
      layerSet.addLayer(tiledVectorLayer);*/


      return layerSet;
   }


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
   //   private static class SampleRasterSymbolizer
   //   extends
   //   GEORasterSymbolizer {
   //
   //      private static final Color FROM_COLOR = Color.fromRGBA(0.7f, 0, 0, 0.5f);
   //
   //
   //      private static GEO2DLineRasterStyle createPolygonLineRasterStyle(final GEOGeometry geometry) {
   //         final JSONObject properties = geometry.getFeature().getProperties();
   //         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
   //         final Color color = FROM_COLOR.wheelStep(7, colorIndex).muchLighter().muchLighter();
   //         final float dashLengths[] = {};
   //         final int dashCount = 0;
   //         return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
   //      }
   //
   //
   //      private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(final GEOGeometry geometry) {
   //         final JSONObject properties = geometry.getFeature().getProperties();
   //         final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
   //         final Color color = FROM_COLOR.wheelStep(7, colorIndex);
   //         return new GEO2DSurfaceRasterStyle(color);
   //      }
   //
   //
   //      @Override
   //      public GEORasterSymbolizer copy() {
   //         return new SampleRasterSymbolizer();
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPointGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
   //         return null;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
   //         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
   //         final GEOPolygonRasterSymbol symbol = new GEOPolygonRasterSymbol( //
   //                  geometry.getPolygonData(), //
   //                  createPolygonLineRasterStyle(geometry), //
   //                  createPolygonSurfaceRasterStyle(geometry) //
   //                  );
   //         symbols.add(symbol);
   //         return symbols;
   //      }
   //
   //
   //      @Override
   //      public ArrayList<GEORasterSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
   //         final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
   //
   //         final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
   //         final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);
   //
   //         for (final GEO2DPolygonData polygonData : geometry.getPolygonsData()) {
   //            symbols.add(new GEOPolygonRasterSymbol(polygonData, lineStyle, surfaceStyle));
   //         }
   //
   //
   //         layerSet.createLayerTilesRenderParameters(forceFirstLevelTilesRenderOnStart, new ArrayList<String>());
   //
   //
   //         final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false), WMSServerVersion.WMS_1_1_0,
   //                  Sector.fromDegrees(40.1640143280790858, -5.8564874640814313, 40.3323148480663158, -5.5216079822178570),
   //                  "image/png", "EPSG:4326", "", true, null, TimeInterval.fromDays(0), true, null, 1);
   //
   //
   //         final WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false),
   //                  WMSServerVersion.WMS_1_1_0, //
   //                  Sector.fromDegrees(40.1240143280790858, -5.8964874640814313, 40.3723148480663158, -5.4816079822178570),//
   //                  //               Sector.fromDegrees(0, -90, 45, 0),
   //                  "image/jpeg", "EPSG:4326", "", false, null, //new LevelTileCondition(0, 6),
   //                  //NULL,
   //                  TimeInterval.fromDays(0), true, null, 1);
   //         layerSet.addLayer(blueMarble);
   //         layerSet.addLayer(pnoa);
   //
   //         layerSet.setTileImageProvider(new DebugTileImageProvider());
   //
   //         builder.getPlanetRendererBuilder().setLayerSet(layerSet);
   //         builder.getPlanetRendererBuilder().setRenderDebug(true);
   //
   //         builder.getPlanetRendererBuilder().setDefaultTileBackGroundImage(
   //                  new DownloaderImageBuilder(new URL("http://www.freelogovectors.net/wp-content/uploads/2013/02/sheep-b.png")));
   //
   //         //  builder.getPlanetRendererBuilder()->setDefaultTileBackGroundImage(new DownloaderImageBuilder(URL("http://192.168.1.127:8080/web/img/tileNotFound.jpg")));
   //         //  const Sector sector = Sector::fromDegrees(40.1540143280790858, -5.8664874640814313,
   //         //                                                40.3423148480663158, -5.5116079822178570);
   //         //
   //         final Default_HUDRenderer hudRenderer = new Default_HUDRenderer();
   //         final InfoDisplay infoDisplay = new DefaultInfoDisplay(hudRenderer);
   //         //infoDisplay->showDisplay();
   //
   //         builder.setHUDRenderer(hudRenderer);
   //         builder.setInfoDisplay(infoDisplay);
   //
   //         final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
   //                  symbolizer, //
   //                  urlTemplate, //
   //                  sector, // sector
   //                  firstLevel, //
   //                  maxLevel, //
   //                  TimeInterval.fromDays(30), // timeToCache
   //                  true, // readExpired
   //                  1, // transparency
   //                  new LevelTileCondition(15, 21), // condition
   //                  "" // disclaimerInfo
   //                  );
   //         // layerSet.addLayer(tiledVectorLayer);
   //
   //         // builder.getPlanetRendererBuilder().setRenderDebug(true);
   //         // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);
   //
   //         return builder.createWidget();
   //      }
   //
   //
   //      @Override
   //      public boolean onCreateOptionsMenu(final Menu menu) {
   //         // Inflate the menu; this adds items to the action bar if it is present.
   //         getMenuInflater().inflate(R.menu.main, menu);
   //         return true;
   //      }
   //
   //   }
>>>>>>> purgatory

}
