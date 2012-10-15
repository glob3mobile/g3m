

package org.glob3.mobile.client;

import java.util.ArrayList;
import java.util.Random;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLDemo
         implements
            EntryPoint {

   private final String    g3mWidgetHolderId = "g3mWidgetHolder";


   private G3MWidget_WebGL _widget           = null;


   @Override
   public void onModuleLoad() {
      if (_widget == null) {
         final Panel g3mWidgetHolder = RootPanel.get(g3mWidgetHolderId);

         final int delayMillis = 10;
         final String proxy = jsDefineDefaultProxy();

         _widget = new G3MWidget_WebGL(delayMillis, proxy);
         g3mWidgetHolder.add(_widget);

         initWidgetDemo();

         ILogger.instance().logInfo("** Using proxy=" + proxy);
      }
   }


   public void initWidgetDemo() {

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      final SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      final boolean useBing = true;
      if (useBing) {
         final WMSLayer bing = new WMSLayer( //
                  "ve", //
                  new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"), //
                  WMSServerVersion.WMS_1_1_0, //
                  Sector.fullSphere(), //
                  "image/png", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  new LevelTileCondition(0, 2));

         layerSet.addLayer(bing);
      }

      final boolean usePnoa = false;
      if (usePnoa) {
         final WMSLayer pnoa = new WMSLayer( //
                  "PNOA", //
                  new URL("http://www.idee.es/wms/PNOA/PNOA"), //
                  WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), //
                  "image/png", //
                  "EPSG:4326", //
                  "", //
                  true, //
                  null);
         layerSet.addLayer(pnoa);
      }

      final boolean useOSMLatLon = true;
      if (useOSMLatLon) {
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
                  new LevelTileCondition(3, 100));
         layerSet.addLayer(osm);
      }


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();
      //      if (true) {
      // dummy renderer with final a simple box
      //      final DummyRenderer dum = new DummyRenderer();
      //      renderers.add(dum);
      //
      //      }
      //      renderers.add(new GLErrorRenderer());


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


      final UserData userData = null;

      //      _widget.initWidget(cameraConstraints, layerSet, renderers, userData);

      final ArrayList<String> imagesToPreload = new ArrayList<String>();
      //      imagesToPreload.add("../images/world.jpg");
      _widget.initWidget(cameraConstraints, layerSet, renderers, userData, imagesToPreload);
   }


   private native String jsDefineDefaultProxy() /*-{
		//		debugger;
		var hostedMode = false;
		var proxy = "";
		try {
			var query = $wnd.location.search;
			hostedMode = (query.indexOf('gwt.codesvr=') != -1 || (query
					.indexOf('gwt.hosted=') != -1 || $wnd.external
					&& $wnd.external.gwtOnLoad))
					&& query.indexOf('gwt.hybrid') == -1;
			hostedModeNoDebug = query.indexOf("G3MWebGLDemo") != -1;
		} catch (e) {
		}

		proxy = $wnd.location.protocol + "//" + $wnd.location.hostname + ":"
				+ $wnd.location.port
				+ ((!hostedModeNoDebug) ? "" : "/G3MWebGLDemo") + "/proxy?url=";
		return proxy;
   }-*/;
}
