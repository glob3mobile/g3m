

package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BingMapType;
import org.glob3.mobile.generated.BingMapsLayer;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.ColumnLayoutImageBuilder;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.DownloaderImageBuilder;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LabelImageBuilder;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.NASAElevationDataProvider;
import org.glob3.mobile.generated.NonOverlappingMark;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.PointCloudsRenderer;
import org.glob3.mobile.generated.PointCloudsRenderer.ColorPolicy;
import org.glob3.mobile.generated.PointCloudsRenderer.PointCloudMetadataListener;
import org.glob3.mobile.generated.Sector;
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

   G3MBuilder_Android builder = null;
   MarksRenderer marksRenderer = new MarksRenderer(false);

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
	   _g3mWidget = createWidgetVR();  
	   //_g3mWidget = createWidgetStreamingElevations();
	   //_g3mWidget = createWidgetVR();
	   
	   // set camera looking at Tenerife
	   Geodetic3D position = new Geodetic3D(Angle.fromDegrees(27.60), Angle.fromDegrees(-16.54), 55000.0);
	   _g3mWidget.setCameraPosition(position);
	   _g3mWidget.setCameraPitch(Angle.fromDegrees(-50.0));

	   _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
	   //_placeHolder.addView(_g3mWidget);
	   _placeHolder.addView(_g3mWidget);
   }

   private G3MWidget_Android createWidgetVR() {
	      final G3MBuilder_Android builder = new G3MBuilder_Android(this);

	      final LayerSet layerSet = new LayerSet();
	      layerSet.addLayer(new OSMLayer(TimeInterval.fromDays(30)));
	      builder.getPlanetRendererBuilder().setLayerSet(layerSet);
	      
	      CameraRenderer cr = new CameraRenderer();
	      cr.addHandler(new DeviceAttitudeCameraHandler(true));
	      builder.setCameraRenderer(cr);

	      return builder.createWidget();
   }
  
}
