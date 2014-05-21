

package com.glob3.mobile.g3mandroidtestingapplication;


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
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
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
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity
         extends
            Activity {

   private G3MWidget_Android _g3mWidget;
   private RelativeLayout    _placeHolder;


   //   private RelativeLayout    _placeHolder;

   G3MBuilder_Android builder = null;
   MarksRenderer marksRenderer = new MarksRenderer(false);

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
   }


   private G3MWidget_Android createWidget() {
      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

      builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
      // builder.getPlanetRendererBuilder().setRenderDebug(true);
      // builder.getPlanetRendererBuilder().setLogTilesPetitions(true);

      return builder.createWidget();
      
   }


	
		
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

}
