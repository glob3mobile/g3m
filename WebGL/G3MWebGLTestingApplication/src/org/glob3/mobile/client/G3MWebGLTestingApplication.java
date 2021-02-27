

package org.glob3.mobile.client;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLTestingApplication
         implements
            EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _g3mWidget         = null;


   @Override
   public void onModuleLoad() {
      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      _g3mWidget = createWidget();

      g3mWidgetHolder.add(_g3mWidget);

      _g3mWidget.setAnimatedCameraPosition(Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 1634079));
   }


   private static G3MWidget_WebGL createWidget() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final String proxy = null;
      builder.setDownloader(new Downloader_WebGL( //
               8, // maxConcurrentOperationCount
               10, // delayMillis
               proxy,
               true));


      final LayerTilesRenderParameters parameters = LayerTilesRenderParameters.createDefaultWGS84( //
               Sector.FULL_SPHERE, //
               1, //
               2, //
               1, //
               13);

      final URLTemplateLayer layer = new URLTemplateLayer( //
               "http://brownietech.ddns.net/maps/s2cloudless/{z}/{y}/{x}.jpg", //
               Sector.FULL_SPHERE, //
               false, //
               TimeInterval.fromDays(30), //
               true, // readExpired
               null, // condition
               parameters);

      final LayerSet layerSet = new LayerSet();
      layerSet.addLayer(layer);
      builder.getPlanetRendererBuilder().setLayerSet(layerSet);

      builder.addRenderer(createMarksRenderer());

      return builder.createWidget();
   }


   private static MarksRenderer createMarksRenderer() {
      final MarksRenderer marksRenderer = new MarksRenderer(false);

      marksRenderer.setMarkTouchListener( //
               new MarkTouchListener() {
                  @Override
                  public boolean touchedMark(final Mark touchedMark) {
                     Window.alert("click on mark: " + touchedMark);
                     return true;
                  }
               }, //
               true);

      final Mark mark = new Mark( //
               new URL("g3m-marker.png"), //
               Geodetic3D.fromDegrees(28.034468668529083146, -15.904092315837871752, 0), //
               AltitudeMode.ABSOLUTE, //
               0 // minDistanceToCamera
      );
      marksRenderer.addMark(mark);

      return marksRenderer;
   }


}
