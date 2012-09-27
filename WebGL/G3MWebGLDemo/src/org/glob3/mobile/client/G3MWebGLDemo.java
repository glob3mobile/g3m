

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LayerSet;
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

   String          g3mWidgetHolderId = "g3mWidgetHolder";


   G3MWidget_WebGL _widget           = null;


   @Override
   public void onModuleLoad() {
      if (_widget == null) {
         final Panel g3mWidgetHolder = RootPanel.get(g3mWidgetHolderId);

         final int delayMillis = 10;
         final String proxy = jsDefineDefaultProxy();

         _widget = new G3MWidget_WebGL(delayMillis, proxy);
         g3mWidgetHolder.add(_widget);

         initWidgetDemo();
      }
   }


   public void initWidgetDemo() {

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      final SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
      cameraConstraints.add(scc);

      final LayerSet layerSet = new LayerSet();
      final WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
               WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png", "EPSG:4326", "", false, null);

      layerSet.addLayer(bing);


      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();
      //      if (true) {
      // dummy renderer with final a simple box
      //      final DummyRenderer dum = new DummyRenderer();
      //      renderers.add(dum);
      //
      //      }
      //      renderers.add(new GLErrorRenderer());

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
