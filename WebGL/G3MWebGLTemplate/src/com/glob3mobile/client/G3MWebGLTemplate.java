

package com.glob3mobile.client;


import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class G3MWebGLTemplate
         implements
            EntryPoint {

   private final String    _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL _widget            = null;


   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad() {
      // initialize a default widget by using a builder
      initDefaultWithBuilder();
      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
      g3mWidgetHolder.add(_widget);


   }


   public void initDefaultWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
      _widget = builder.createWidget();

   }
}
