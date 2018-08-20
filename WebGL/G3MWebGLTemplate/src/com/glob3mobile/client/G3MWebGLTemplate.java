

package com.glob3mobile.client;


import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.OSMLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.core.*;

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
      Window.alert("HELLO WORLD 1.1");
   }


   public void initDefaultWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();
      
      
      LayerSet ls = new LayerSet();
      ls.addLayer(new OSMLayer(TimeInterval.forever()));
      builder.getPlanetRendererBuilder().setLayerSet(ls);
      builder.setDownloader(new Downloader_WebGL( 8, 10, "proxy.php?url="));

      _widget = builder.createWidget();

   }
}
