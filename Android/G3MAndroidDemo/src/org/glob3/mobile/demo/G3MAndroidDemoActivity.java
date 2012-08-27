package org.glob3.mobile.demo;

import android.app.Activity;
import android.os.Bundle;

import org.glob3.mobile.demo.*;

public class G3MAndroidDemoActivity extends Activity {
	
	G3MWidget_Android _widget = null;
	
	void initWidgetDemo()
	{
	  LayerSet layerSet = new LayerSet();
	  
	  WMSLayer bing = new WMSLayer("ve",
	                                URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
	                                WMS_1_1_0,
	                                Sector::fullSphere(),
	                                "image/png",
	                                "EPSG:4326",
	                                "",
	                                false,
	                                NULL);
	  layerSet->addLayer(bing);

	  
	  if (false) {
	    WMSLayer *osm = new WMSLayer("osm",
	                                 URL("http://wms.latlon.org/"),
	                                 WMS_1_1_0,
	                                 Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
	                                 "image/jpeg",
	                                 "EPSG:4326",
	                                 "",
	                                 false,
	                                 NULL);
	    layerSet->addLayer(osm);
	  }
	  
	  
	  //  WMSLayer *pnoa = new WMSLayer("PNOA",
	  //                                "http://www.idee.es/wms/PNOA/PNOA",
	  //                                WMS_1_1_0,
	  //                                "image/png",
	  //                                Sector::fromDegrees(21, -18, 45, 6),
	  //                                "EPSG:4326",
	  //                                "",
	  //                                true,
	  //                                Angle::nan(),
	  //                                Angle::nan());
	  //  layerSet->addLayer(pnoa);
	  
	  //  WMSLayer *vias = new WMSLayer("VIAS",
	  //                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
	  //                                WMS_1_1_0,
	  //                                "image/gif",
	  //                                Sector::fromDegrees(22.5,-22.5, 33.75, -11.25),
	  //                                "EPSG:4326",
	  //                                "",
	  //                                true,
	  //                                Angle::nan(),
	  //                                Angle::nan());
	  //  layerSet->addLayer(vias);
	  
	  //  WMSLayer *osm = new WMSLayer("bing",
	  //                               "bing",
	  //                               "http://wms.latlon.org/",
	  //                               WMS_1_1_0,
	  //                               "image/jpeg",
	  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
	  //                               "EPSG:4326",
	  //                               "",
	  //                               false,
	  //                               Angle::nan(),
	  //                               Angle::nan());
	  //  layerSet->addLayer(osm);
	  
	  //  WMSLayer *osm = new WMSLayer("osm",
	  //                               "osm",
	  //                               "http://wms.latlon.org/",
	  //                               WMS_1_1_0,
	  //                               "image/jpeg",
	  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
	  //                               "EPSG:4326",
	  //                               "",
	  //                               false,
	  //                               Angle::nan(),
	  //                               Angle::nan());
	  //  layerSet->addLayer(osm);
	  
	  
	  
	  std::vector<Renderer*> renderers;

	  //  if (false) {
	  //    // dummy renderer with a simple box
	  //    DummyRenderer* dum = new DummyRenderer();
	  //    comp->addRenderer(dum);
	  //  }
	  
	  //  if (false) {
	  //    // simple planet renderer, with a basic world image
	  //    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
	  //    comp->addRenderer(spr);
	  //  }
	  
	  
	  if (true) {
	    // marks renderer
	    MarksRenderer* marks = new MarksRenderer();
	    renderers.push_back(marks);
	    
	    Mark* m1 = new Mark("Fuerteventura",
	                        "g3m-marker.png",
	                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
	    //m1->addTouchListener(listener);
	    marks->addMark(m1);
	    
	    
	    Mark* m2 = new Mark("Las Palmas",
	                        "g3m-marker.png",
	                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
	    //m2->addTouchListener(listener);
	    marks->addMark(m2);
	    
	    if (false) {
	      for (int i = 0; i < 500; i++) {
	        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
	        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
	        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
	        
	        marks->addMark(new Mark("Random",
	                                "g3m-marker.png",
	                                Geodetic3D(latitude, longitude, 0)));
	      }
	    }
	  }
	  
	  //  if (false) {
	  //    LatLonMeshRenderer *renderer = new LatLonMeshRenderer();
	  //    renderers.push_back(renderer);
	  //  }
	  
	  //  if (false) {
	  //    SceneGraphRenderer* sgr = new SceneGraphRenderer();
	  //    SGCubeNode* cube = new SGCubeNode();
	  //    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
	  //    sgr->getRootNode()->addChild(cube);
	  //    renderers.push_back(sgr);
	  //  }
	  
	  renderers.push_back(new GLErrorRenderer());
	  
	  std::vector <ICameraConstrainer*> cameraConstraints;
	  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
	  cameraConstraints.push_back(scc);
	  
	  UserData* userData = NULL;
	  [[self G3MWidget] initWidgetWithCameraConstraints: cameraConstraints
	                                           layerSet: layerSet
	                                          renderers: renderers
	                                           userData: userData];
	  
	}

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (_widget == null){ //Just the first time
        	_widget = new G3MWidget_Android(this);
        	setContentView(_widget);
        }
    }
}