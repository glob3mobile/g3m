

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Renderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.UserData;
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
         _widget = new G3MWidget_WebGL();
         g3mWidgetHolder.add(_widget);
         initWidgetDemo();
      }
   }


   public void initWidgetDemo() {

      final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
      final SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
      cameraConstraints.add(scc);

      final LayerSet layerSet = null;

      final ArrayList<Renderer> renderers = new ArrayList<Renderer>();
      if (true) {
         // dummy renderer with a simple box
         final DummyRenderer dum = new DummyRenderer();


         renderers.add(dum);

         //TODO CAN'T EXECUTE UNTIL LoadImageFromFileName IS IMPLEMENTED
         //         final String imageURL = "world.jpg";
         //         final SimplePlanetRenderer spr = new SimplePlanetRenderer(imageURL);
         //         renderers.add(spr);
      }

      final UserData userData = null;

      _widget.initWidget(cameraConstraints, layerSet, renderers, userData);

   }
}
