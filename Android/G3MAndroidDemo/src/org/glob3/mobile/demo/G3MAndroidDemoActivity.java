

package org.glob3.mobile.demo;

import java.util.ArrayList;
import java.util.Random;

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
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;


public class G3MAndroidDemoActivity
         extends
            G3MBaseActivity {

   @Override
   protected void initializeWidget(final G3MWidget_Android widget) {
      final LayerSet layerSet = new LayerSet();

      final boolean useBing = false;
      if (useBing) {
         final WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
                  WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png", "EPSG:4326", "", false, null);
         layerSet.addLayer(bing);
      }

      final boolean usePnoa = false;
      if (usePnoa) {
         final WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA"), WMSServerVersion.WMS_1_1_0,
                  Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null);
         layerSet.addLayer(pnoa);
      }

      final boolean useOSM = true;
      if (useOSM) {
         //         final WMSLayer osm = new WMSLayer( //
         //                  "osm", //
         //                  new URL("http://wms.latlon.org/"), //
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
                  new URL("http://129.206.228.72/cached/osm"), //
                  WMSServerVersion.WMS_1_1_0, //
                  Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                  "image/jpeg", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  null);
         layerSet.addLayer(osm);
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

      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();

      //  if (false) {
      //    // dummy renderer with a simple box
      //      final DummyRenderer dum = new DummyRenderer();
      //      renderers.add(dum);
      //  }

      //  if (false) {
      //    // simple planet renderer, with a basic world image
      //      final SimplePlanetRenderer spr = new SimplePlanetRenderer("world.jpg");
      //      renderers.add(spr);
      //  }


      final boolean useMarkers = true;
      if (useMarkers) {
         // marks renderer
         final boolean readyWhenMarksReady = false;
         final MarksRenderer marks = new MarksRenderer(readyWhenMarksReady);
         renderers.add(marks);

         final Mark m1 = new Mark(//
                  "Fuerteventura", //
                  new URL("http://www.glob3mobile.com/wp-content/themes/glob3mobile/images/logo_s.png"), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
         //m1->addTouchListener(listener);
         marks.addMark(m1);

         final Mark m2 = new Mark( //
                  "Las Palmas", //
                  new URL("http://www.glob3mobile.com/wp-content/themes/glob3mobile/images/logo_s.png"), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
         //m2->addTouchListener(listener);
         marks.addMark(m2);

         final boolean randomMarkers = false;
         if (randomMarkers) {
            final Random random = new Random();
            for (int i = 0; i < 500; i++) {
               final Angle latitude = Angle.fromDegrees((random.nextInt() % 180) - 90);
               final Angle longitude = Angle.fromDegrees((random.nextInt() % 360) - 180);
               //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());

               marks.addMark(new Mark("Random", new URL(
                        "http://www.glob3mobile.com/wp-content/themes/glob3mobile/images/logo_s.png"), new Geodetic3D(latitude,
                        longitude, 0)));
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

      renderers.add(new GLErrorRenderer());

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      final SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
      cameraConstraints.add(scc);

      final UserData userData = null;

      widget.initWidget(cameraConstraints, layerSet, renderers, userData);

   }

}
