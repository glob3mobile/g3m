package com.glob3.mobile.g3mandroidtestingapplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

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
import org.glob3.mobile.generated.EllipsoidShape;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GEOTileRasterizer;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GeoMeter;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.HUDQuadWidget;
import org.glob3.mobile.generated.HUDRelativePosition;
import org.glob3.mobile.generated.HUDRelativeSize;
import org.glob3.mobile.generated.HUDRenderer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.LineShape;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MapQuestLayer;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PointShape;
import org.glob3.mobile.generated.RasterLineShape;
import org.glob3.mobile.generated.RasterPolygonShape;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapeTouchListener;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.SQLiteStorage_Android;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
extends
Activity {

	private G3MWidget_Android _g3mWidget;
	private RelativeLayout    _placeHolder;


   G3MBuilder_Android builder = null;

	public CameraRenderer createCameraRenderer()
	{
	  CameraRenderer cameraRenderer = new CameraRenderer();
	  final boolean useInertia = true;
	  cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
	  //final boolean allowRotationInDoubleDrag = true;
	  //cameraRenderer.addHandler(new CameraDoubleDragHandler(allowRotationInDoubleDrag));
	  cameraRenderer.addHandler(new CameraZoomAndRotateHandler());

	  cameraRenderer.addHandler(new CameraRotationHandler());
	  cameraRenderer.addHandler(new CameraDoubleTapHandler());

	  return cameraRenderer;
	}

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   setContentView(R.layout.activity_main);
	   
	   // tests for Cotesa
	   // testElevationNavigation();
	   testingVectorialGeometry();
	   
		_placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		_placeHolder.addView(_g3mWidget);
   }
   
   public void testZrender() {

	   final G3MBuilder_Android builder = new G3MBuilder_Android(this);

	   //const Planet* planet = Planet::createEarth();
	   //const Planet* planet = Planet::createSphericalEarth();
	   final Planet planet = Planet.createFlatEarth();
	   builder.setPlanet(planet);

	   // set camera handlers
	   CameraRenderer cameraRenderer = createCameraRenderer();
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
	   
		//BEGINNING OF CODE FOR LOADING STORAGE
		if (true){

			AssetManager am = getAssets();

			try {
				//LEYENDO FICHERO DE ASSETS
				InputStream in = am.open("g3m.cache");

				//OBTENIENDO STREAM DE SALIDA
				File f = getExternalCacheDir();
				if ((f == null) || !f.exists()) {
					f = getCacheDir();
				}
				final String documentsDirectory = f.getAbsolutePath();
				final File f2 = new File(new File(documentsDirectory), "g3m.cache");
				final String path = f2.getAbsolutePath();
				OutputStream out = new FileOutputStream(path);

				//COPIANDO FICHERO
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();


				SQLiteStorage_Android storage = new SQLiteStorage_Android("g3m.cache", this.getApplicationContext());

				builder.setStorage(storage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//END OF CODE FOR LOADING STORAGE


		//BEGINNING OF CODE FOR PRECACHING AREA
		boolean precaching = false;
		PrecacherInitializationTask pit = null;
		if (precaching){
			//Las Palmas de GC
			Geodetic2D upper = Geodetic2D.fromDegrees(28.20760859532738, -15.3314208984375);
			Geodetic2D lower = Geodetic2D.fromDegrees(28.084096949164735, -15.4852294921875);

			pit = new PrecacherInitializationTask(null, upper, lower, 6);
			builder.setInitializationTask(pit);
		}

		_g3mWidget = builder.createWidget();  

		// set frustumCullingFactor
		_g3mWidget.getPlanetRenderer().setFrustumCullingFactor(2.0f);

		// set camera looking at Tenerife
		Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
		_g3mWidget.setCameraPosition(position);
		_g3mWidget.setCameraPitch(Angle.fromDegrees(-50.0));



		if (precaching){
			pit.setWidget(_g3mWidget);
		}

		//END OF CODE FOR PRECACHING AREA

	}

   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);

      return builder.createWidget();
      
   }

		// final ShapesRenderer shapesRenderer = new ShapesRenderer();
		// builder.addRenderer(shapesRenderer);
	
		
      final LayerSet layerSet = new LayerSet();
      //layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

    private LayerSet createLayerSet() {
      final LayerSet layerSet = new LayerSet();
      //      layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

      layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));



      final String urlTemplate = "http://192.168.1.2/ne_10m_admin_0_countries-Levels10/{level}/{y}/{x}.geojson";
      final int firstLevel = 2;
      final int maxLevel = 10;

 

      return layerSet;
   }

		/*// set elevations
		      final Sector sector = Sector.fromDegrees(27.967811065876, -17.0232177085356, 28.6103464294992, -16.0019401695656);
		      final Vector2I extent = new Vector2I(256, 256);
		      final URL url = NasaBillElevationDataURL.compoundURL(sector, extent);
		      final ElevationDataProvider elevationDataProvider = new SingleBillElevationDataProvider(url, sector, extent);
		      builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
		      builder.getPlanetRendererBuilder().setVerticalExaggeration(2.0f);
*/





	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	public void testingGeometer()
	{
		// measure distance from Las Palmas to Madrid
		Geodetic2D g1 = Geodetic2D.fromDegrees(28.129064150616994, -15.423265639110468); //LP
		Geodetic2D g2 = Geodetic2D.fromDegrees(40.41677540051771, -3.7037901976145804); //MADRID
		double dist = GeoMeter.getDistance(g1, g2);
		ILogger.instance().logInfo("Distance Las Palmas - Madrid: %.3f Km\n", dist*1e-3);

		// measure area of Gran Canaria island
		java.util.ArrayList<Geodetic2D> polygon = new java.util.ArrayList<Geodetic2D>();
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.1801128508277), Angle.fromDegrees(-15.401893797679804)));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.0468737992174), Angle.fromDegrees(-15.412880125804804)));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.993531872334557), Angle.fromDegrees(-15.368934813304804)));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.864917930859935), Angle.fromDegrees(-15.38404101447668)));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.80419790643659), Angle.fromDegrees(-15.43347949103918)));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.743443929681458), Angle.fromDegrees(-15.569435301586054)));		
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.75559743885222), Angle.fromDegrees( -15.687538328929804 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.835776559501845), Angle.fromDegrees( -15.791908446117304 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(27.91468298115362), Angle.fromDegrees( -15.83722704963293 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.021418448958062), Angle.fromDegrees( -15.81525439338293 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.088074450524935), Angle.fromDegrees( -15.71363085822668 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.17406012312459), Angle.fromDegrees( -15.70264453010168 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.172849536482957), Angle.fromDegrees( -15.629860106273554 ) ));
		polygon.add(new Geodetic2D(Angle.fromDegrees(28.149845787838487), Angle.fromDegrees( -15.47193163947668 ) ));
		double area = GeoMeter.getArea(polygon);
		ILogger.instance().logInfo("Gran Canaria area: %.3f Km2\n", area*1e-6);
	}

	public void testElevationNavigation() {
		final G3MBuilder_Android builder = new G3MBuilder_Android(this);

		//const Planet* planet = Planet::createEarth();
		//const Planet* planet = Planet::createSphericalEarth();
		final Planet planet = Planet.createFlatEarth();
		builder.setPlanet(planet);
		
		// set camera handlers
		CameraRenderer cameraRenderer = createCameraRenderer();
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

		// create wmslayer from Grafcan
		LayerSet layerSet = new LayerSet();
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
				true);
		layerSet.addLayer(grafcanLIDAR);
		builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		// create elevations for Tenerife from bil file
		Sector sector = Sector.fromDegrees (27.967811065876,                  // min latitude
				-17.0232177085356,                // min longitude
				28.6103464294992,                 // max latitude
				-16.0019401695656);               // max longitude
		Vector2I extent = new Vector2I(256, 256);                             // image resolution
		URL url = new URL("http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Tenerife-256x256.bil", false);
		ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(url, sector, extent);
		builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);	  
		builder.getPlanetRendererBuilder().setVerticalExaggeration(2.0f);

		_g3mWidget = builder.createWidget(); 

		// set frustumCullingFactor
		_g3mWidget.getPlanetRenderer().setFrustumCullingFactor(2.0f);

		// set camera looking at Tenerife
		Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
		_g3mWidget.setCameraPosition(position);
		_g3mWidget.setCameraPitch(Angle.fromDegrees(-50.0));
	}	
	
	
	public void testingVectorialGeometry() {
		final G3MBuilder_Android builder = new G3MBuilder_Android(this);
		// builder.getPlanetRendererBuilder().setRenderDebug(true);

		// GeoTileRasterizer is needed to draw RasterShapes
		GEOTileRasterizer geoTileRasterizer = new GEOTileRasterizer();
		builder.getPlanetRendererBuilder().addTileRasterizer(geoTileRasterizer);
		
		// testing Geometer
		testingGeometer();
		
		// set camera handlers
		CameraRenderer cameraRenderer = createCameraRenderer();
		builder.setCameraRenderer(cameraRenderer);

		final ShapesRenderer shapesRenderer = new ShapesRenderer(geoTileRasterizer);
		builder.addRenderer(shapesRenderer);

		final LayerSet layerSet = new LayerSet();
		//layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval.fromDays(30)));
		layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
		builder.getPlanetRendererBuilder().setLayerSet(layerSet);

		if (true) {      
			// testing selecting shapes
			final double factor = 3000;
			final Vector3D radius1 = new Vector3D(factor, factor, factor);
			final Vector3D radius2 = new Vector3D(factor*1.5, factor*1.5, factor*1.5);
			final Vector3D radiusBox = new Vector3D(factor, factor*1.5, factor*2);

			Shape box1 = new BoxShape(new Geodetic3D(Angle.fromDegrees(39.70),
					Angle.fromDegrees(2.80),
					radiusBox._z/2),
					AltitudeMode.ABSOLUTE,
					radiusBox,
					0,
					Color.fromRGBA(0,    1, 0, 1));
			shapesRenderer.addShape(box1);

			Shape ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.80),
					Angle.fromDegrees(2.90),
					radius1._z),
					AltitudeMode.ABSOLUTE,
					new URL("file:///world.jpg", false),
					radius1,
					(short)32,
					(short)0,
					false,
					false);
			shapesRenderer.addShape(ellipsoid1);

			Shape mercator1 = new EllipsoidShape(new Geodetic3D(Angle.fromDegrees(39.60),
					Angle.fromDegrees(3),
					radius2._x),
					AltitudeMode.ABSOLUTE,
					new URL("file:///mercator_debug.png", false),
					radius2,
					(short)32,
					(short)0,
					false,
					true);
			shapesRenderer.addShape(mercator1);

			// DRAWING POINTS
			{
				Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
						Angle.fromDegrees(3.30),
						radiusBox._z),
						AltitudeMode.ABSOLUTE,
						8,
						Color.fromRGBA(0, 0, 1, 1));
				shapesRenderer.addShape(point);
			}
			{
				Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.55),
						Angle.fromDegrees(3.40),
						radiusBox._z),
						AltitudeMode.ABSOLUTE,
						6,
						Color.fromRGBA(0, 0, 1, 1));
				shapesRenderer.addShape(point);
			}
			{
				Shape point = new PointShape(new Geodetic3D(Angle.fromDegrees(39.70),
						Angle.fromDegrees(3.50),
						radiusBox._z),
						AltitudeMode.ABSOLUTE,
						4,
						Color.fromRGBA(0, 0, 1, 1));
				shapesRenderer.addShape(point);
			}

			// DRAWING LINES
			{
				Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.69),
						Angle.fromDegrees(3.31),
						radiusBox._z),
						new Geodetic3D(Angle.fromDegrees(39.56),
								Angle.fromDegrees(3.39),
								radiusBox._z),
								AltitudeMode.ABSOLUTE,
								5,
								Color.fromRGBA(1, 0.5f, 0, 1));
				shapesRenderer.addShape(line);
			}
			{
				Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.56),
						Angle.fromDegrees(3.41),
						radiusBox._z),
						new Geodetic3D(Angle.fromDegrees(39.69),
								Angle.fromDegrees(3.49),
								radiusBox._z),   		                                
								AltitudeMode.ABSOLUTE,
								5,
								Color.fromRGBA(1, 0.5f, 0, 1));
				shapesRenderer.addShape(line);
			}
			{
				Shape line = new LineShape(new Geodetic3D(Angle.fromDegrees(39.70),
						Angle.fromDegrees(3.31),
						radiusBox._z),
						new Geodetic3D(Angle.fromDegrees(39.70),
								Angle.fromDegrees(3.49),
								radiusBox._z),
								AltitudeMode.ABSOLUTE,
								5,
								Color.fromRGBA(1, 0.5f, 0, 1));
				shapesRenderer.addShape(line);
			}

			// DRAWING RASTER LINES
			{
				Shape rasterLine = new RasterLineShape(new Geodetic2D(Angle.fromDegrees(39.40),
						Angle.fromDegrees(2.70)),
						new Geodetic2D(Angle.fromDegrees(39.40),
								Angle.fromDegrees(3.00)),
								2,
								Color.fromRGBA(0, 0, 1, 1));
				shapesRenderer.addShape(rasterLine);
			}

			// DRAWING RASTER POLYGON
			{
				java.util.ArrayList<Geodetic2D> vertices = new java.util.ArrayList<Geodetic2D>();
				vertices.add(new Geodetic2D(Angle.fromDegrees(39.50), Angle.fromDegrees(3.10)));
				vertices.add(new Geodetic2D(Angle.fromDegrees(39.38), Angle.fromDegrees(3.20)));
				vertices.add(new Geodetic2D(Angle.fromDegrees(39.40), Angle.fromDegrees(3.28)));
				vertices.add(new Geodetic2D(Angle.fromDegrees(39.60), Angle.fromDegrees(3.25)));

				Shape pol1 = new RasterPolygonShape(vertices,
						2,
						Color.green(),
						Color.fromRGBA(1, 1, 1, 0.6f));
				shapesRenderer.addShape(pol1);
			}

			// DRAWING JSON
			/* shapesRenderer.loadJSONSceneJS(new URL("file:///seymour-plane.json", false), 
      		  "file:////", 
      		  false,
  			  new Geodetic3D(Angle.fromDegrees(39.70),
  					  Angle.fromDegrees(2.60),
  					  7*factor),
  					  AltitudeMode.ABSOLUTE, new ShapeLoadListener() {

                   @Override
                   public void onBeforeAddShape(final SGShape shape) {
                  	 double scale = factor/5;
                 	  shape.setScale(scale, scale, scale);
                 	  shape.setPitch(Angle.fromDegrees(120));
                 	  shape.setHeading(Angle.fromDegrees(-110));
                   }


                   @Override
                   public void onAfterAddShape(final SGShape shape) {
                   }


                   @Override
                   public void dispose() {
                      // TODO Auto-generated method stub

                   }
                });*/
			/*
  	  Shape plane = SceneJSShapesParser.parseFromJSON("file:///seymour-plane.json", 
  			  "file:////", 
  			  false,
  			  new Geodetic3D(Angle.fromDegrees(39.70),
  					  Angle.fromDegrees(2.60),
  					  7*factor),
  					  AltitudeMode.ABSOLUTE);
  	  double scale = factor/5;
  	  plane.setScale(scale, scale, scale);
  	  plane.setPitch(Angle.fromDegrees(120));
  	  plane.setHeading(Angle.fromDegrees(-110));
  	  shapesRenderer.addShape(plane);*/

			// adding touch listener
			ShapeTouchListener myShapeTouchListener = new ShapeTouchListener() {
				Shape _selectedShape = null;

				public boolean touchedShape(Shape shape) {

					if (_selectedShape == null) {
						shape.select();
						_selectedShape = shape;
					} else {
						if (_selectedShape==shape) {
							shape.unselect();
							_selectedShape = null;
						} else {
							_selectedShape.unselect();
							_selectedShape = shape;
							shape.select();
						}
					}

					if (_selectedShape != null)
						ILogger.instance().logInfo("Shape length = %.3f Km.   Shape area = %.3f Km2\n", 
								shape.getLength()*1e-3, shape.getArea()*1e-6);

					return true;
				}
			};

			shapesRenderer.setShapeTouchListener(myShapeTouchListener, true);
		}

		_g3mWidget = builder.createWidget();

		if (true) {
			Geodetic3D position = new Geodetic3D(Angle.fromDegrees(39.08), Angle.fromDegrees(2.90), 113000);
			_g3mWidget.setCameraPosition(position);
			_g3mWidget.setCameraHeading(Angle.fromDegrees(-5.0));
			_g3mWidget.setCameraPitch(Angle.fromDegrees(24.0-90.0));
		}
	}

}


//BEGINNING OF CODE FOR PRECACHING AREA

class PrecacherInitializationTask extends GInitializationTask {

	private class PrecacherDownloadListener extends IImageDownloadListener {

		PrecacherInitializationTask _task;

		public PrecacherDownloadListener(PrecacherInitializationTask task) {
			_task = task;
		}

		@Override
		public void onDownload(URL url, IImage image, boolean expired) {
			// TODO Auto-generated method stub
			_task.imageDownloaded();

		}

		@Override
		public void onError(URL url) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel(URL url) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCanceledDownload(URL url, IImage image, boolean expired) {
			// TODO Auto-generated method stub

		}

	}

	private int _nImagesDownloaded = 0;
	private LinkedList<String> _urls;
	private boolean _done = false;
	private G3MWidget_Android _widget = null;

	private Geodetic2D _upper, _lower;
	private int _level;

	public void setWidget(G3MWidget_Android widget){
		_widget = widget;
	}

	public PrecacherInitializationTask(G3MWidget_Android widget, Geodetic2D upper, Geodetic2D lower, int level) {
		_widget = widget;
		_upper = upper;
		_lower = lower;
		_level = level;
	}

	public void imageDownloaded() {
		_nImagesDownloaded++;
		if (_nImagesDownloaded % 10 == 0) {
			ILogger.instance().logInfo("IMAGE DOWNLOADED %d\n",
					_nImagesDownloaded);
		}
		int size = _urls.size();
		if (_nImagesDownloaded == size) {
			ILogger.instance().logInfo("ALL IMAGES DOWNLOADED \n");
			_done = true;
		}
	}

	@Override
	public void run(G3MContext context) {

		G3MWidget widget = _widget.getG3MWidget();

		_urls = widget.getPlanetRenderer().getTilesURL(_lower, _upper, _level);

		IDownloader downloader = context.getDownloader();

		for (int i = 0; i < _urls.size(); i++) {
			String url = _urls.get(i);
			downloader.requestImage(new URL(url), 1000,
					TimeInterval.fromSeconds(0), false,
					new PrecacherDownloadListener(this), true);
		}

	}

	@Override
	public boolean isDone(G3MContext context) {
		return _done;
	}
	
}

//END OF CODE FOR PRECACHING AREA
