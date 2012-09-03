

package org.glob3.mobile.client;

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
         _widget.init();
      }

   }

}
