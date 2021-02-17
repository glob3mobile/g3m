

package com.g3m.hello.world.client;


import org.glob3.mobile.specific.*;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.ui.*;


public class HelloWorldEntryPoint
                                  implements
                                     EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _widget            = null;


   @Override
   public void onModuleLoad() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
      _widget = builder.createWidget();

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
      g3mWidgetHolder.add(_widget);
   }

}
