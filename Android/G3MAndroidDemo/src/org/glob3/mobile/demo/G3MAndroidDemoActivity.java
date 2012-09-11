

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.GLErrorRenderer;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;


public class G3MAndroidDemoActivity
         extends
            Activity {

   G3MWidget_Android _widget = null;


   void initWidgetDemo() {
      final LayerSet layerSet = new LayerSet();

      final WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
               WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png", "EPSG:4326", "", false, null);
      layerSet.addLayer(bing);

      final boolean usePnoa = false;
      if (usePnoa) {
         final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA"), WMSServerVersion.WMS_1_1_0,
                  Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null);
         layerSet.addLayer(pnoa);
      }

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


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

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
         final MarksRenderer marks = new MarksRenderer();
         renderers.add(marks);

         final Mark m1 = new Mark("Fuerteventura", "g3m-marker.png", new Geodetic3D(Angle.fromDegrees(28.05),
                  Angle.fromDegrees(-14.36), 0));
         //m1->addTouchListener(listener);
         marks.addMark(m1);


         final Mark m2 = new Mark("Las Palmas", "g3m-marker.png", new Geodetic3D(Angle.fromDegrees(28.05),
                  Angle.fromDegrees(-15.36), 0));
         //m2->addTouchListener(listener);
         marks.addMark(m2);
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

      renderers.add(new GLErrorRenderer());

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      final SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
      cameraConstraints.add(scc);

      final UserData userData = null;

      _widget.initWidget(cameraConstraints, layerSet, renderers, userData);

   }


   /** Called when the activity is first created. */
   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (_widget == null) { //Just the first time
         _widget = new G3MWidget_Android(this);

         initWidgetDemo();

         setContentView(_widget);
      }
   }
}
